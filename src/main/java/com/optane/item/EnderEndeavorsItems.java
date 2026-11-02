package com.optane.item;

import com.optane.EnderEndeavors;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.function.Function;

/** Registers standalone items. */
public final class EnderEndeavorsItems {
    /** Prevents instantiation. */
    private EnderEndeavorsItems() {}
    /** Triggers static item registration. Must be called during mod initialization. */
    public static void register() {
        EnderEndeavors.LOGGER.info("Registering items for " + EnderEndeavors.MOD_ID);
    }
  
    //region Item Registration
    public static final Item ENDCLOUD_DUST = register(
            EnderEndeavorsItemIds.ENDCLOUD_DUST,
            Item::new,
            new Item.Properties()
    );
    //endregion

    //region Helper Methods
    /**
     * Creates and registers an item.
     *
     * @param itemKey item registry key
     * @param itemFactory item constructor
     * @param settings item settings
     * @return registered item
     */
    public static Item register(ResourceKey<Item> itemKey,
                                Function<Item.Properties, Item> itemFactory,
                                Item.Properties settings) {
        Item item = itemFactory.apply(settings.setId(itemKey));
        return Registry.register(BuiltInRegistries.ITEM, itemKey, item);
    }
    //endregion
}
