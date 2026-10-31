package com.optane.item;

import com.optane.EnderEndeavors;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

/**
 * Registers all custom items for Ender Endeavors.
 */
public final class EnderEndeavorsItems {

    /** Prevents instantiating */
    private EnderEndeavorsItems() {}

    /**
     * ResourceKey identifying the Endcloud Dust item within the item registry.
     */
    public static final ResourceKey<Item> ENDCLOUD_DUST_KEY =
            ResourceKey.create(Registries.ITEM, EnderEndeavors.id("endcloud_dust"));

    /**
     * The registered Endcloud Dust item instance.
     */
    public static final Item ENDCLOUD_DUST = Registry.register(
            BuiltInRegistries.ITEM,
            ENDCLOUD_DUST_KEY,
            new Item(new Item.Properties().setId(ENDCLOUD_DUST_KEY))
    );

    /**
     * Triggers static initialization of mod items.
     * Must be called during mod initialization.
     */
    public static void register() {
        EnderEndeavors.LOGGER.info("Registering items for " + EnderEndeavors.MOD_ID);
        EnderEndeavors.LOGGER.info("Registered item key: " + ENDCLOUD_DUST_KEY.identifier());
    }
}

