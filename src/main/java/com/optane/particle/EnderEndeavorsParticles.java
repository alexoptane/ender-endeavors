package com.optane.particle;

import com.optane.EnderEndeavors;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;

/** Registers custom particle types. */
public final class EnderEndeavorsParticles {
    /** Prevents instantiation. */
    private EnderEndeavorsParticles() {}

    /** Triggers static particle registration. Must be called during mod initialization. */
    public static void register() {
        EnderEndeavors.LOGGER.info("Registering particles for " + EnderEndeavors.MOD_ID);
    }

    public static final SimpleParticleType ENDCLOUD_DUST_PALE =
            register(EnderEndeavorsParticleIds.ENDCLOUD_DUST_PALE);

    public static final SimpleParticleType CHARGED_ENDCLOUD_DUST =
            register(EnderEndeavorsParticleIds.CHARGED_ENDCLOUD_DUST);

    public static final SimpleParticleType CHARGED_ENDCLOUD_DUST_PALE =
            register(EnderEndeavorsParticleIds.CHARGED_ENDCLOUD_DUST_PALE);

    /**
     * Creates and registers a simple particle type.
     *
     * @param particleKey particle registry key
     * @return registered simple particle type
     */
    private static SimpleParticleType register(ResourceKey<ParticleType<?>> particleKey) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, particleKey, FabricParticleTypes.simple());
    }
}
