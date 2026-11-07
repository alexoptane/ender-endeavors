package com.optane.block;

import com.optane.EnderEndeavors;
import com.optane.block.blocks.ChargedEndcloudBlock;
import com.optane.block.blocks.EndcloudBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

/** Registers blocks with items and standalone blocks. */
public final class EnderEndeavorsBlocks {
    /** Prevents instantiation. */
    private EnderEndeavorsBlocks() {}
    /** Triggers static block and item registration. Must be called during mod initialization. */
    public static void register() {
        EnderEndeavors.LOGGER.info("Registering blocks for " + EnderEndeavors.MOD_ID);
    }

    //region Block Registration
    public static final Block ENDCLOUD = register(
            EnderEndeavorsBlockItemIds.ENDCLOUD,
            EndcloudBlock::new,
            BlockBehaviour.Properties.of()
                    .sound(SoundType.POWDER_SNOW)
                    .noOcclusion()
                    .lightLevel(state -> 3)
    );


    public static final Block CHARGED_ENDCLOUD = register(
            EnderEndeavorsBlockItemIds.CHARGED_ENDCLOUD,
            ChargedEndcloudBlock::new,
            BlockBehaviour.Properties.of()
                    .sound(SoundType.WOOL)
                    .noOcclusion()
    );


    //endregion

    //region Helper Methods
    /**
     * Creates and registers a block and its item.
     *
     * @param id paired registry keys
     * @param blockFactory block constructor
     * @param properties block settings
     * @return registered block
     */
    private static Block register(
            BlockItemId id,
            Function<BlockBehaviour.Properties, Block> blockFactory,
            BlockBehaviour.Properties properties
    ) {
        Block block = register(id.block(), blockFactory, properties);
        BlockItem blockItem = new BlockItem(block,
                new Item.Properties().useBlockDescriptionPrefix().setId(id.item()));
        Registry.register(BuiltInRegistries.ITEM, id.item(), blockItem);
        return block;
    }

    /**
     * Creates and registers a block without an item.
     *
     * @param key block registry key
     * @param blockFactory block constructor
     * @param properties block settings
     * @return registered block
     */
    private static Block register(
            ResourceKey<Block> key,
            Function<BlockBehaviour.Properties, Block> blockFactory,
            BlockBehaviour.Properties properties
    ) {
        Block block = blockFactory.apply(properties.setId(key));
        return Registry.register(BuiltInRegistries.BLOCK, key, block);
    }
    //endregion
}
