package com.optane.event.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLevelEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

import java.util.HashMap;
import java.util.Map;

/** Simulates vanilla End flashes on the server side. */
public final class EndFlashEvent {
    /** Prevents instantiation. */
    private EndFlashEvent() {}

    /**
     * Registers a lambda that runs at the end of each server level tick.
     *
     * <p>If the level supports End flashes, the lambda gets or creates its
     * {@link FlashState} that uses the level's current clock tick to simulate vanilla's flash timing.
     * It fires {@link #FLASH_TICK} once per tick while the simulated flash is active,
     * passing the level, the {@link FlashWindow}, and the current clock tick.
     * The state is removed when the level unloads.
     */
    public static void register() {
        Map<ServerLevel, FlashState> states = new HashMap<>();
        ServerLevelEvents.UNLOAD.register((server, level) -> states.remove(level));
        ServerTickEvents.END_LEVEL_TICK.register(level -> {
            if (!level.dimensionType().hasEndFlashes()) {
                return;
            }

            FlashState state = states.computeIfAbsent(level, ignored -> new FlashState());
            long clockTime = level.getDefaultClockTime();
            FlashWindow activeFlash = state.tick(clockTime);
            if (activeFlash != null) {
                FLASH_TICK.invoker().onTick(level, activeFlash, clockTime);
            }
        });
    }

    /**
     * Event that fires on the server thread at the end of each level tick with an active simulated End flash.
     *
     * <p>Register a {@link Tick} callback to receive the event. The invoker calls
     * {@link Tick#onTick} on each registered callback when the event fires.
     */
    public static final Event<Tick> FLASH_TICK = EventFactory.createArrayBacked(Tick.class,
            listeners -> (level, activeFlash, clockTime) -> {
                for (Tick listener : listeners) {
                    listener.onTick(level, activeFlash, clockTime);
                }
            });

    /**
     * Defines the callback signature for {@link #FLASH_TICK}.
     *
     * <p>When you register a method reference such as {@code FLASH_TICK.register(Handler::handleFlash)},
     * Java adapts it to a {@code Tick} callback if the method is compatible with {@link #onTick}.
     * The referenced method can have a different name. Calling {@code onTick} on the callback
     * invokes that method with the event's arguments.
     */
    @FunctionalInterface
    public interface Tick {
        /**
         * Runs after the level's flash simulation finds an active flash for this tick.
         *
         * @param level level that just finished its tick
         * @param activeFlash current active flash window
         * @param clockTime current time on the level's default clock in ticks
         */
        void onTick(ServerLevel level, FlashWindow activeFlash, long clockTime);
    }

    /** Describes a simulated flash in ticks of the level's default clock. */
    public record FlashWindow(long startClockTime, int duration) {
        /** Returns the clock time at the end of the simulated flash interval. */
        public long endClockTime() {
            return startClockTime + duration;
        }

        /**
         * Checks whether vanilla's simulated intensity is positive at this time.
         *
         * @param clockTime current time on the level's default clock in ticks
         * @return {@code true} if the simulated flash is active at that time
         */
        boolean isActive(long clockTime) {
            long elapsed = clockTime - startClockTime;
            return elapsed >= 0 && elapsed <= duration
                    && Mth.sin((float) elapsed * (float) Math.PI / duration) > 0.0F;
        }
    }

    /** Simulates vanilla's client-side End flash timing for one server level. */
    private static final class FlashState {
        private static final int INTERVAL = 600;

        private long cycle;
        private FlashWindow flash;

        /**
         * Finds the active End flash window at the given clock time replicating vanilla's logic.
         *
         * <p>When the clock enters a different 600-tick cycle, the method uses
         * the cycle number as a random seed. It discards the first draw to
         * match vanilla, then calculates the flash's offset and duration.
         * The same window is reused throughout the cycle.
         *
         * @param clockTime current time on the level's default clock in ticks
         * @return the flash window if its intensity is above zero, or {@code null}
         */
        private FlashWindow tick(long clockTime) {
            long nextCycle = clockTime / INTERVAL;
            if (nextCycle != cycle) {
                RandomSource random = RandomSource.createThreadLocalInstance(nextCycle);
                random.nextFloat();
                int offset = Mth.randomBetweenInclusive(random, 0, 200);
                int duration = Mth.randomBetweenInclusive(random, 100, Math.min(380, INTERVAL - offset));
                flash = new FlashWindow(nextCycle * INTERVAL + offset, duration);
                cycle = nextCycle;
            }

            return flash != null && flash.isActive(clockTime) ? flash : null;
        }
    }
}
