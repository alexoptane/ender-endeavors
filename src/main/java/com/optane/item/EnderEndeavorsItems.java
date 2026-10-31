package com.optane.item;

import com.optane.EnderEndeavors;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;

/**
 * Registers all custom items and block items for Ender Endeavors.
 */
public final class EnderEndeavorsItems {

    private EnderEndeavorsItems() {
        /* Utility class — prevents instantiation. */
    }
  
    //Region Item Registration
  
    /**
     * The registered Endcloud Dust item instance.
     */
    public static final Item ENDCLOUD_DUST = register(
            "endcloud_dust",
            Item::new,
            new Item.Properties()
    );

    //Region Helper Methods
  
    /**
     * Registers a custom item in the game item registry.
     *
     * @param name        the registry path name of the item
     * @param itemFactory factory function used to instantiate the item
     * @param settings    item properties configuration
     * @return the registered {@link Item} instance
     */
    public static Item register(String name,
                                Function<Item.Properties, Item> itemFactory,
                                Item.Properties settings) {
        ResourceKey<Item> itemKey = createItemKey(name);
        Item item = itemFactory.apply(settings.setId(itemKey));
        return Registry.register(BuiltInRegistries.ITEM, itemKey, item);
    }

    /**
     * Creates and registers a {@link BlockItem} associated with the given block.
     *
     * @param name  the registry path name of the block item
     * @param block the block to associate with the item
     * @return the registered {@link Item} instance
     */
    public static Item registerBlockItem(String name, Block block) {
        return register(
                name,
                properties -> new BlockItem(block, properties),
                new Item.Properties().useBlockDescriptionPrefix()
        );
    }

    /**
     * Creates an item registry key within the mod namespace.
     *
     * @param name the registry path name of the item
     * @return the corresponding {@link ResourceKey} for the item registry
     */
    private static ResourceKey<Item> createItemKey(String name) {
        return ResourceKey.create(Registries.ITEM, EnderEndeavors.id(name));
    }

    /**
     * Triggers static initialization of the item registrations.
     * Must be called during mod initialization.
     */
    public static void register() {
        EnderEndeavors.LOGGER.info("Registering items for " + EnderEndeavors.MOD_ID);
    }
}
