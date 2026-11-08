package com.optane.radiation;

import com.optane.event.events.EndFlashEvent;
import com.optane.event.events.EndFlashEvent.FlashWindow;
import net.minecraft.server.level.ServerLevel;

/** Coordinates flash exposure for players. */
final class PlayerEndRadiationHandler {
    private PlayerEndRadiationHandler() {}

    /** Subscribes the player handler to flash ticks. */
    static void register() {
        EndFlashEvent.FLASH_TICK.register(PlayerEndRadiationHandler::tick);
    }

    /**
     * Receives the current flash window.
     *
     * @param level level containing the players
     * @param activeFlash current active flash window
     * @param clockTime current time on the level's default clock in ticks
     */
    private static void tick(ServerLevel level, FlashWindow activeFlash, long clockTime) {
        // TODO: Apply radiation to exposed players.
    }
}
