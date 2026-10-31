package com.optane.block.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PowderSnowBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;

/** Registers the basic Endcloud block type. */
public final class EndcloudBlock extends PowderSnowBlock {
    /** @param properties the registered block's physical and visual properties */
    public EndcloudBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    /** Keeps Powder Snow movement without applying its freezing effect. */
    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity,
                                InsideBlockEffectApplier effectApplier, boolean isMoving) {
    }
}
