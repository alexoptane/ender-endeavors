package com.optane.item;

import com.optane.EnderEndeavors;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

/** Defines standalone item keys. */
public final class EnderEndeavorsItemIds {
    /** Prevents instantiation. */
    private EnderEndeavorsItemIds() {}

    public static final ResourceKey<Item> ENDCLOUD_DUST = create("endcloud_dust");

    /**
     * Creates an item key in the mod namespace.
     *
     * @param name registry path
     * @return item registry key
     */
    private static ResourceKey<Item> create(String name) {
        return ResourceKey.create(Registries.ITEM, EnderEndeavors.id(name));
    }
}
