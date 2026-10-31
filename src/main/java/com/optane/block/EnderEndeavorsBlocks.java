package com.optane.block;

import com.optane.EnderEndeavors;
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
            BlockBehaviour.Properties.of()
                    .sound(SoundType.POWDER_SNOW)
                    .noOcclusion()
                    .noCollision(),
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
