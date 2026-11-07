package com.optane.effect;

import com.optane.EnderEndeavors;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;

import java.util.function.Supplier;

/** Registers mob effects. */
public final class EnderEndeavorsEffects {
    /** Prevents instantiation. */
    private EnderEndeavorsEffects() {}
    /** Triggers static effect registration. Must be called during mod initialization. */
    public static void register() {
        EnderEndeavors.LOGGER.info("Registering effects for " + EnderEndeavors.MOD_ID);
    }

    //region Effect Registration
    public static final Holder<MobEffect> END_RADIATION = register(
            EnderEndeavorsEffectIds.END_RADIATION,
            EndRadiation::new
    );
    //endregion

    //region Helper Methods
    /**
     * Creates and registers an effect.
     *
     * @param effectKey effect registry key
     * @param effectFactory effect constructor
     * @return holder for the registered effect
     */
    private static Holder<MobEffect> register(
            ResourceKey<MobEffect> effectKey,
            Supplier<MobEffect> effectFactory
    ) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, effectKey, effectFactory.get());
    }
    //endregion
}
