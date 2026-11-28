package com.optane.radiation;

import com.optane.effect.EnderEndeavorsEffects;
import com.optane.event.events.EndFlashEvent;
import com.optane.event.events.EndFlashEvent.FlashWindow;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.HashMap;
import java.util.Map;

/** Applies End Radiation after sustained exposure to an End flash. */
final class PlayerEndRadiationHandler {
    private static final int REQUIRED_EXPOSURE_TICKS = 100;
    private static final int RADIATION_DURATION_TICKS = 45 * 20;
    private static final Map<ServerPlayer, RadiationExposure> RADIATION_EXPOSURES = new HashMap<>();

    /** Prevents instantiation. */
    private PlayerEndRadiationHandler() {}

    /** Tracks flash exposure and resets it when a player leaves the level. */
    static void register() {
        EndFlashEvent.FLASH_TICK.register((level, flash, _) -> tick(level, flash));
        ServerEntityEvents.ENTITY_UNLOAD.register((entity, _) -> {
            if (entity instanceof ServerPlayer player) {
                RADIATION_EXPOSURES.remove(player);
            }
        });
    }

    /**
     * Applies or refreshes radiation every 100 ticks of continuous exposure to the same flash.
     * Uses server ticks so changes to the End clock only affect when the flash is active.
     *
     * @param level level containing the players
     * @param activeFlash current active flash window
     */
    private static void tick(ServerLevel level, FlashWindow activeFlash) {
        int serverTick = level.getServer().getTickCount();
        for (ServerPlayer player : level.players()) {
            if (!player.isAlive()) {
                continue;
            }

            RadiationExposure exposure = RADIATION_EXPOSURES.get(player);
            if (exposure == null || !exposure.flash.equals(activeFlash) || exposure.lastExposureTick != serverTick - 1) {
                exposure = new RadiationExposure(activeFlash, serverTick);
                RADIATION_EXPOSURES.put(player, exposure);
            } else {
                exposure.lastExposureTick = serverTick;
            }

            if (serverTick == exposure.nextApplicationTick) {
                player.addEffect(new MobEffectInstance(
                        EnderEndeavorsEffects.END_RADIATION,
                        RADIATION_DURATION_TICKS,
                        0,
                        false,
                        true,
                        true
                ));
                exposure.nextApplicationTick += REQUIRED_EXPOSURE_TICKS;
            }
        }
    }

    /** Keeps the next application and the last tick of exposure to the same flash. */
    private static final class RadiationExposure {
        private final FlashWindow flash;
        private int nextApplicationTick;
        private int lastExposureTick;

        private RadiationExposure(FlashWindow flash, int firstTick) {
            this.flash = flash;
            // The first tick counts toward the threshold.
            this.nextApplicationTick = firstTick + REQUIRED_EXPOSURE_TICKS - 1;
            this.lastExposureTick = firstTick;
        }
    }
}
