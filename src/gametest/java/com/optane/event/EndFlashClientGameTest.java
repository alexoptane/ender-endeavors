package com.optane.event;

import com.optane.event.events.EndFlashEvent;
import com.optane.event.events.EndFlashEvent.FlashWindow;
import com.optane.event.mixin.EndFlashStateAccessor;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestDedicatedServerContext;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.EndFlashState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

/**
 * Compares vanilla's client flash state with server events as the End clock runs normally.
 *
 * <p>Run with {@code gradlew runClientGameTest -PacceptMinecraftEula=true}
 * after accepting the Minecraft EULA for the test's dedicated server.
 */
@SuppressWarnings("UnstableApiUsage")
public final class EndFlashClientGameTest implements FabricClientGameTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(EndFlashClientGameTest.class);
    private static final int FLASH_CYCLE_TICKS = 600;
    private static final int MAX_FLASH_ATTEMPTS = 3;
    private static final int OBSERVATION_TIMEOUT = MAX_FLASH_ATTEMPTS * FLASH_CYCLE_TICKS;
    private static final int LATE_JOIN_MARGIN_TICKS = 80;

    private static volatile FlashObservation activeObservation;

    static {
        EndFlashEvent.FLASH_TICK.register((level, flash, clockTime) -> {
            FlashObservation observation = activeObservation;
            if (observation != null && level.dimension().equals(Level.END)) {
                observation.recordEmission(flash, clockTime);
            }
        });
        ServerTickEvents.END_LEVEL_TICK.register(level -> {
            FlashObservation observation = activeObservation;
            if (observation != null && level.dimension().equals(Level.END)) {
                observation.recordServerTick(level.getDefaultClockTime());
            }
        });
        ClientTickEvents.END_LEVEL_TICK.register(level -> {
            FlashObservation observation = activeObservation;
            if (observation != null && level.dimension().equals(Level.END)) {
                observation.recordClientTick(level);
            }
        });
    }

    /**
     * Checks both boundaries of a flash and reconnects to the same server during a later flash.
     *
     * <p>Fabric's default test world freezes time. This scenario uses the
     * normal world settings so vanilla advances and synchronizes the clock
     * throughout both connections. The probes only read each side's state.
     *
     * @param context Fabric's client game test context
     */
    @Override
    public void runTest(ClientGameTestContext context) {
        FlashObservation observation = new FlashObservation();
        activeObservation = observation;
        Properties properties = new Properties();
        properties.setProperty("level-name", "end-flash-test");

        try (TestDedicatedServerContext server = context.worldBuilder().setUseConsistentSettings(false)
                .createServer(properties)) {
            EndFlashState earlyState;
            try (var connection = server.connect()) {
                enterEnd(server);
                context.waitFor(client -> observation.firstClientTick != null);
                ClientTick first = context.computeOnClient(client -> observation.firstClientTick);
                if (first.flash() != null) {
                    throw new AssertionError("The early connection entered during a flash at tick " + first.clockTime());
                }
                earlyState = context.computeOnClient(client -> client.level.endFlashState());

                observeFullFlash(context, server, observation, first.clockTime());
            }

            observeReconnect(context, server, observation, earlyState);
        } finally {
            activeObservation = null;
        }
    }

    /**
     * Observes a complete cycle and checks both boundaries of the same flash.
     * If a gap hides a boundary, the client stays connected to observe another cycle.
     *
     * @param context client test context
     * @param server dedicated test server with a connected client
     * @param observation trace recorded by the client and server probes
     * @param firstTick first End clock tick observed by the client
     */
    private static void observeFullFlash(ClientGameTestContext context, TestDedicatedServerContext server,
                                         FlashObservation observation, long firstTick) {
        long nextCycle = (firstTick / FLASH_CYCLE_TICKS + 1) * FLASH_CYCLE_TICKS;
        long lastTick = nextCycle + FLASH_CYCLE_TICKS - 1;
        for (int attempt = 0; attempt < MAX_FLASH_ATTEMPTS; attempt++) {
            if (assertMatchingTicks(context, server, observation, firstTick, lastTick).completeFlash()) {
                return;
            }
            firstTick = lastTick;
            lastTick += FLASH_CYCLE_TICKS;
        }
        throw new AssertionError("Insufficient coverage: the client and server did not both observe"
                + " the start and end of one flash");
    }

    /**
     * Reconnects during a later flash and checks the first client sample and the end of that flash.
     * If either check lacks a sample, reconnects during another flash.
     *
     * @param context client test context
     * @param server dedicated test server that the client disconnected from
     * @param observation trace recorded by the client and server probes
     * @param earlyState vanilla flash state from the first connection
     */
    private static void observeReconnect(ClientGameTestContext context, TestDedicatedServerContext server,
                                         FlashObservation observation, EndFlashState earlyState) {
        for (int attempt = 0; attempt < MAX_FLASH_ATTEMPTS; attempt++) {
            context.runOnClient(client -> observation.clearClientTicks());
            server.waitFor(game -> {
                long clockTime = endLevel(game).getDefaultClockTime();
                ServerTick sample = observation.serverTicks.get(clockTime);
                return sample != null && sample.flash() != null
                        && sample.flash().endClockTime() - clockTime >= LATE_JOIN_MARGIN_TICKS;
            }, OBSERVATION_TIMEOUT);

            try (var connection = server.connect()) {
                enterEnd(server);
                context.waitFor(client -> observation.firstClientTick != null);
                ClientTick first = context.computeOnClient(client -> observation.firstClientTick);
                EndFlashState lateState = context.computeOnClient(client -> client.level.endFlashState());
                if (lateState == earlyState) {
                    throw new AssertionError("The late connection reused the early client's flash state");
                }
                if (first.flash() == null) {
                    throw new AssertionError("The late connection saw no flash at End clock " + first.clockTime());
                }

                FlashCoverage coverage = assertMatchingTicks(context, server, observation,
                        first.clockTime(), first.flash().endClockTime() + 1);
                if (coverage.firstTickCompared() && first.flash().equals(coverage.endedFlash())) {
                    return;
                }
            }
        }
        throw new AssertionError("Insufficient coverage: the client and server did not both observe"
                + " the first tick after reconnecting and the end of its flash");
    }

    /**
     * Compares every clock tick observed by both sides in an inclusive range.
     *
     * <p>The server must emit one event when the client sees an active flash
     * and zero otherwise. When it emits, the event's clock, start and duration
     * must match the client's sample. A gap breaks the chain of consecutive
     * samples, so a transition across a gap cannot establish an exact boundary.
     *
     * @param context client test context
     * @param server dedicated test server with a connected client
     * @param observation trace recorded by the client and server probes
     * @param firstTick first End clock tick to compare
     * @param lastTick last End clock tick to compare
     * @return whether the first tick was compared and which flash boundaries were covered
     */
    private static FlashCoverage assertMatchingTicks(ClientGameTestContext context, TestDedicatedServerContext server,
                                                     FlashObservation observation, long firstTick, long lastTick) {
        context.waitFor(client -> client.level != null && client.level.getDefaultClockTime() > lastTick,
                OBSERVATION_TIMEOUT);
        server.waitFor(game -> endLevel(game).getDefaultClockTime() > lastTick, OBSERVATION_TIMEOUT);
        Map<Long, ServerTick> serverTicks = server.computeOnServer(game -> Map.copyOf(observation.serverTicks));

        return context.computeOnClient(client -> {
            ClientTick previous = null;
            FlashWindow startedFlash = null;
            FlashWindow endedFlash = null;
            boolean completeFlash = false;
            boolean firstTickCompared = false;
            int missingTicks = 0;
            for (long clockTime = firstTick; clockTime <= lastTick; clockTime++) {
                ServerTick serverTick = serverTicks.get(clockTime);
                ClientTick clientTick = observation.clientTicks.get(clockTime);
                if (serverTick == null || clientTick == null) {
                    missingTicks++;
                    previous = null;
                    continue;
                }

                FlashWindow expectedFlash = clientTick.flash();
                int expectedEmissions = expectedFlash == null ? 0 : 1;
                if (serverTick.emissions() != expectedEmissions || !Objects.equals(serverTick.flash(), expectedFlash)
                        || (expectedFlash != null && serverTick.eventClockTime() != clockTime)) {
                    throw new AssertionError("End clock " + clockTime + " expected " + expectedEmissions
                            + " emission(s) with " + expectedFlash + ", got " + serverTick);
                }
                if (clockTime == firstTick) {
                    firstTickCompared = true;
                }
                if (previous != null) {
                    if (previous.flash() == null && expectedFlash != null) {
                        startedFlash = expectedFlash;
                    } else if (previous.flash() != null && expectedFlash == null) {
                        endedFlash = previous.flash();
                        completeFlash |= endedFlash.equals(startedFlash);
                    }
                }
                previous = clientTick;
            }
            LOGGER.info("Compared End clock range {} to {} with {} ticks missing a sample",
                    firstTick, lastTick, missingTicks);
            return new FlashCoverage(firstTickCompared, completeFlash, endedFlash);
        });
    }

    /** Places the test player in the End before the timing comparison. */
    private static void enterEnd(TestDedicatedServerContext server) {
        server.runOnServer(game -> {
            var player = game.getPlayerList().getPlayers().getFirst();
            ServerLevel end = endLevel(game);
            if (player.level() != end) {
                player.setGameMode(GameType.SPECTATOR);
                player.teleportTo(end, 0.5, 80, 0.5, Set.of(), 0, 0, true);
            }
        });
    }

    private static ServerLevel endLevel(MinecraftServer server) {
        return Objects.requireNonNull(server.getLevel(Level.END), "The dedicated server must load the End");
    }

    private record ClientTick(long clockTime, FlashWindow flash) {}

    private record ServerTick(int emissions, FlashWindow flash, long eventClockTime) {}

    private record FlashCoverage(boolean firstTickCompared, boolean completeFlash, FlashWindow endedFlash) {}

    /** Holds one run's observations. Each trace is written and read on its own game thread. */
    private static final class FlashObservation {
        private final Map<Long, ServerTick> serverTicks = new HashMap<>();
        private final Map<Long, ClientTick> clientTicks = new HashMap<>();
        private int emissions;
        private FlashWindow emittedFlash;
        private long eventClockTime;
        private ClientTick firstClientTick;

        private void recordEmission(FlashWindow flash, long clockTime) {
            emissions++;
            emittedFlash = flash;
            eventClockTime = clockTime;
        }

        private void recordServerTick(long clockTime) {
            serverTicks.put(clockTime, new ServerTick(emissions, emittedFlash, eventClockTime));
            emissions = 0;
            emittedFlash = null;
        }

        private void recordClientTick(ClientLevel level) {
            long clockTime = level.getDefaultClockTime();
            EndFlashState state = Objects.requireNonNull(level.endFlashState(), "Expected a vanilla End flash state");
            EndFlashStateAccessor values = (EndFlashStateAccessor) state;
            FlashWindow flash = null;
            if (values.enderEndeavors$getIntensity() > 0.0F) {
                long cycleStart = clockTime - clockTime % FLASH_CYCLE_TICKS;
                flash = new FlashWindow(cycleStart + values.enderEndeavors$getOffset(), values.enderEndeavors$getDuration());
            }

            ClientTick sample = new ClientTick(clockTime, flash);
            clientTicks.putIfAbsent(clockTime, sample);
            if (firstClientTick == null) {
                firstClientTick = sample;
            }
        }

        private void clearClientTicks() {
            clientTicks.clear();
            firstClientTick = null;
        }
    }
}
