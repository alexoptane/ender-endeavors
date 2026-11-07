package com.optane.effect;

import com.optane.EnderEndeavors;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;

/** Defines effect keys. */
public final class EnderEndeavorsEffectIds {
    /** Prevents instantiation. */
    private EnderEndeavorsEffectIds() {}

    public static final ResourceKey<MobEffect> END_RADIATION = create("end_radiation");

    /**
     * Creates an effect key in the mod namespace.
     *
     * @param name registry path
     * @return effect registry key
     */
    private static ResourceKey<MobEffect> create(String name) {
        return ResourceKey.create(Registries.MOB_EFFECT, EnderEndeavors.id(name));
    }
}
