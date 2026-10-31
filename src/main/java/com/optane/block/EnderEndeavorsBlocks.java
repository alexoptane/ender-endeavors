package com.optane.block;

import com.optane.EnderEndeavors;
import com.optane.block.blocks.EndcloudBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

/** Registers Ender Endeavors blocks and their inventory items. */
public final class EnderEndeavorsBlocks {
    public static final ResourceKey<Block> ENDCLOUD_KEY =
            ResourceKey.create(BuiltInRegistries.BLOCK.key(), EnderEndeavors.id("endcloud"));
    public static final ResourceKey<Item> ENDCLOUD_ITEM_KEY =
            ResourceKey.create(BuiltInRegistries.ITEM.key(), EnderEndeavors.id("endcloud"));
    public static final Block ENDCLOUD = Registry.register(
            BuiltInRegistries.BLOCK,
            ENDCLOUD_KEY,
            new EndcloudBlock(BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.POWDER_SNOW)
                    .setId(ENDCLOUD_KEY))
    );
    public static final Item ENDCLOUD_ITEM = Registry.register(
            BuiltInRegistries.ITEM,
            ENDCLOUD_ITEM_KEY,
            new BlockItem(ENDCLOUD, new Item.Properties().useBlockDescriptionPrefix().setId(ENDCLOUD_ITEM_KEY))
    );

    private EnderEndeavorsBlocks() {
        /* Prevents utility class instantiation. */
    }

    /** Initializes block and dust registrations. */
    public static void register() {
        EnderEndeavors.LOGGER.info("Registered Endcloud block");
    }
}
