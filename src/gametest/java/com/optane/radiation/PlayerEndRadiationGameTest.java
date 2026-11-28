package com.optane.radiation;

import com.optane.effect.EnderEndeavorsEffects;
import com.optane.event.events.EndFlashEvent;
import com.optane.event.events.EndFlashEvent.FlashWindow;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.clock.ClockState;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRules;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.IntConsumer;

/** Checks radiation through the registered flash and player lifecycle events. */
public final class PlayerEndRadiationGameTest {
    // Cycle 25 starts its 100-tick flash at 15096. Cycle 1 has a 311-tick flash at 733.
    private static final long MINIMUM_FLASH_START = 15096;
    private static final long LONG_FLASH_START = 733;

    private static RadiationScenario activeScenario;

    static {
        EndFlashEvent.FLASH_TICK.register((level, flash, clockTime) -> {
            RadiationScenario scenario = activeScenario;
            if (scenario != null && level == scenario.end) {
                scenario.observedFlash = flash;
                scenario.lastFlashClockTime = clockTime;
            }
        });
    }

    /** Checks both sides of the exposure threshold and persistence after the shortest flash. */
    @GameTest(environment = "ender_endeavors_gametest:radiation_application", maxTicks = 140)
    public void minimumFlashAppliesRadiationOnItsLastTick(GameTestHelper helper) {
        RadiationScenario scenario = new RadiationScenario(helper, MINIMUM_FLASH_START);
        ServerPlayer player = scenario.addPlayer();
        scenario.onEachTick(tick -> {
            switch (tick) {
                case 99 -> assertNoRadiation(helper, player);
                case 100 -> {
                    scenario.assertFlash(100);
                    MobEffectInstance radiation = assertRadiationDuration(helper, player, 900);
                    helper.assertValueEqual(radiation.getAmplifier(), 0, "radiation amplifier");
                    helper.assertTrue(radiation.isAmbient(), "radiation should be ambient");
                    helper.assertTrue(radiation.showIcon(), "radiation should show its icon");
                }
                case 101 -> {
                    helper.assertValueEqual(scenario.lastFlashClockTime, MINIMUM_FLASH_START + 100,
                            "the minimum flash should have ended");
                    assertRadiationDuration(helper, player, 899);
                    scenario.succeed();
                }
            }
        });
    }

    /** Checks that another 100 ticks in the same flash renew the effect to 45 seconds. */
    @GameTest(environment = "ender_endeavors_gametest:radiation_renewal", maxTicks = 240)
    public void continuousExposureRenewsRadiationInTheSameFlash(GameTestHelper helper) {
        RadiationScenario scenario = new RadiationScenario(helper, LONG_FLASH_START);
        ServerPlayer player = scenario.addPlayer();
        scenario.onEachTick(tick -> {
            switch (tick) {
                case 99 -> assertNoRadiation(helper, player);
                case 100 -> {
                    scenario.assertFlash(311);
                    assertRadiationDuration(helper, player, 900);
                }
                case 199 -> assertRadiationDuration(helper, player, 801);
                case 200 -> {
                    scenario.assertFlash(311);
                    assertRadiationDuration(helper, player, 900);
                    scenario.succeed();
                }
            }
        });
    }

    /** Checks reentry during the same tick and the following tick, with fresh exposure in both cases. */
    @GameTest(environment = "ender_endeavors_gametest:radiation_reentry", maxTicks = 190)
    public void leavingTheEndRestartsExposure(GameTestHelper helper) {
        RadiationScenario scenario = new RadiationScenario(helper, LONG_FLASH_START);
        ServerPlayer immediateReturn = scenario.addPlayer();
        ServerPlayer nextTickReturn = scenario.addPlayer();
        scenario.onEachTick(tick -> {
            switch (tick) {
                case 50 -> {
                    assertNoRadiation(helper, immediateReturn);
                    assertNoRadiation(helper, nextTickReturn);
                    teleport(immediateReturn, scenario.overworld);
                    teleport(immediateReturn, scenario.end);
                    teleport(nextTickReturn, scenario.overworld);
                }
                case 51 -> teleport(nextTickReturn, scenario.end);
                case 100, 101, 149 -> {
                    assertNoRadiation(helper, immediateReturn);
                    assertNoRadiation(helper, nextTickReturn);
                }
                case 150 -> {
                    assertRadiationDuration(helper, immediateReturn, 900);
                    assertNoRadiation(helper, nextTickReturn);
                }
                case 151 -> {
                    assertRadiationDuration(helper, immediateReturn, 899);
                    assertRadiationDuration(helper, nextTickReturn, 900);
                    scenario.succeed();
                }
            }
        });
    }

    private static void assertNoRadiation(GameTestHelper helper, ServerPlayer player) {
        helper.assertFalse(player.hasEffect(EnderEndeavorsEffects.END_RADIATION),
                "expected no radiation for " + player.getUUID());
    }

    private static MobEffectInstance assertRadiationDuration(GameTestHelper helper, ServerPlayer player,
                                                            int expectedTicks) {
        MobEffectInstance radiation = player.getEffect(EnderEndeavorsEffects.END_RADIATION);
        helper.assertTrue(radiation != null, "expected radiation for " + player.getUUID());
        helper.assertValueEqual(radiation.getDuration(), expectedTicks, "remaining radiation ticks");
        return radiation;
    }

    private static void teleport(ServerPlayer player, ServerLevel destination) {
        player.teleportTo(destination, 0.5, 200, 0.5, Set.of(), 0, 0, true);
    }

    /** Owns the test players and restores the shared clock and game rule after each scenario. */
    private static final class RadiationScenario {
        private final GameTestHelper helper;
        private final MinecraftServer server;
        private final ServerLevel overworld;
        private final ServerLevel end;
        private final Holder<WorldClock> endClock;
        private final ClockState originalClock;
        private final boolean originalAdvanceTime;
        private final long flashStart;
        private final List<ServerPlayer> players = new ArrayList<>();
        private FlashWindow observedFlash;
        private long lastFlashClockTime;
        private boolean closed;

        private RadiationScenario(GameTestHelper helper, long flashStart) {
            this.helper = helper;
            this.flashStart = flashStart;
            server = helper.getLevel().getServer();
            overworld = server.overworld();
            end = Objects.requireNonNull(server.getLevel(Level.END), "The test server must load the End");
            endClock = end.dimensionType().defaultClock().orElseThrow();
            originalClock = end.clockManager().getInstance(endClock).packState();
            originalAdvanceTime = overworld.getGameRules().get(GameRules.ADVANCE_TIME);
            overworld.getGameRules().set(GameRules.ADVANCE_TIME, true, server);
            end.clockManager().getInstance(endClock).loadFrom(new ClockState(flashStart, 0, 1, false));
            activeScenario = this;
            helper.runBeforeTestEnd(this::close);
        }

        @SuppressWarnings("removal") // Vanilla's helper supplies a connected mock server player.
        private ServerPlayer addPlayer() {
            ServerPlayer player = helper.makeMockServerPlayerInLevel();
            players.add(player);
            player.getAbilities().flying = true;
            teleport(player, end);
            return player;
        }

        private void onEachTick(IntConsumer assertions) {
            helper.onEachTick(() -> {
                if (closed) {
                    return;
                }
                try {
                    int tick = Math.toIntExact(helper.getTick());
                    helper.assertValueEqual(end.getDefaultClockTime() - flashStart, (long) tick,
                            "End clock should advance once per server tick");
                    assertions.accept(tick);
                    if (!closed) {
                        // Mock connections send no client tick packets. Advance their effects after
                        // assertions so each sample observes the result of the real flash handler.
                        for (ServerPlayer player : players) {
                            player.doTick();
                        }
                    }
                } catch (RuntimeException | Error failure) {
                    close();
                    throw failure;
                }
            });
        }

        private void succeed() {
            close();
            helper.succeed();
        }

        private void assertFlash(int expectedDuration) {
            helper.assertTrue(observedFlash != null, "expected a real flash event");
            helper.assertValueEqual(observedFlash.startClockTime(), flashStart, "flash start clock time");
            helper.assertValueEqual(observedFlash.duration(), expectedDuration, "flash duration");
            helper.assertValueEqual(lastFlashClockTime, end.getDefaultClockTime(), "flash event clock time");
        }

        private void close() {
            if (closed) {
                return;
            }
            closed = true;
            activeScenario = null;
            for (ServerPlayer player : players) {
                server.getPlayerList().remove(player);
            }
            end.clockManager().getInstance(endClock).loadFrom(originalClock);
            overworld.getGameRules().set(GameRules.ADVANCE_TIME, originalAdvanceTime, server);
        }
    }
}
