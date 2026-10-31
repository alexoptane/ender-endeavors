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
import com.optane.block.blocks.ChargedEndcloudBlock;
import com.optane.item.EnderEndeavorsItems;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

/**
 * Registers all custom blocks for Ender Endeavors.
 */
public final class EnderEndeavorsBlocks {

    /**
     * Energized variant of Endcloud that surrounds End Singularities.
     */
    public static final Block CHARGED_ENDCLOUD = register(
            "charged_endcloud",
            ChargedEndcloudBlock::new,
            BlockBehaviour.Properties.of().sound(SoundType.WOOL),
            true
    );

    private EnderEndeavorsBlocks() {
        /* Utility class — prevents instantiation. */
    }

    /**
     * Registers a block and optionally its corresponding block item in the game registries.
     *
     * @param name               the registry path name of the block
     * @param blockFactory       factory function used to instantiate the block
     * @param settings           block behavior properties
     * @param shouldRegisterItem whether a corresponding BlockItem should also be registered
     * @return the registered {@link Block} instance
     */
    public static Block register(String name,
                                 Function<BlockBehaviour.Properties, Block> blockFactory,
                                 BlockBehaviour.Properties settings,
                                 boolean shouldRegisterItem) {
        ResourceKey<Block> blockKey = createBlockKey(name);
        Block block = blockFactory.apply(settings.setId(blockKey));

        if (shouldRegisterItem) {
            EnderEndeavorsItems.registerBlockItem(name, block);
        }

        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }

    /**
     * Creates a block registry key within the mod namespace.
     *
     * @param name the registry path name of the block
     * @return the corresponding {@link ResourceKey} for the block registry
     */
    private static ResourceKey<Block> createBlockKey(String name) {
        return ResourceKey.create(Registries.BLOCK, EnderEndeavors.id(name));
    }

    /**
     * Triggers static initialization of the block registrations.
     * Must be called during mod initialization.
     */
    public static void register() {
        EnderEndeavors.LOGGER.info("Registering blocks for " + EnderEndeavors.MOD_ID);
    }
}
