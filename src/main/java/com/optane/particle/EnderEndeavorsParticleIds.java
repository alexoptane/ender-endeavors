package com.optane.particle;

import com.optane.EnderEndeavors;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;

/** Defines particle type keys. */
public final class EnderEndeavorsParticleIds {
    /** Prevents instantiation. */
    private EnderEndeavorsParticleIds() {}

    public static final ResourceKey<ParticleType<?>> ENDCLOUD_DUST_PALE =
            create("endcloud_dust_pale");
    public static final ResourceKey<ParticleType<?>> CHARGED_ENDCLOUD_DUST =
            create("charged_endcloud_dust");
    public static final ResourceKey<ParticleType<?>> CHARGED_ENDCLOUD_DUST_PALE =
            create("charged_endcloud_dust_pale");

    /**
     * Creates a particle type key in the mod namespace.
     *
     * @param name registry path
     * @return particle type registry key
     */
    private static ResourceKey<ParticleType<?>> create(String name) {
        return ResourceKey.create(Registries.PARTICLE_TYPE, EnderEndeavors.id(name));
    }
}