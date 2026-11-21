package com.optane.block.blocks;

import com.optane.EnderEndeavors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/** Defines the transparent Endcloud block. */
public final class EndcloudBlock extends TransparentBlock {
    private static final double REVERSE_PORTAL_TRAVEL_TICKS = 31.0;

    /**
     * Creates an Endcloud block.
     *
     * @param properties block settings
     */
    public EndcloudBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    /** Emits vanilla reverse-portal motes that drift into the block's underside. */
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        BlockPos belowPos = pos.below();
        if (level.getBlockState(belowPos).isCollisionShapeFullBlock(level, belowPos)) {
            return;
        }

        if (random.nextFloat() >= 0.725F) {
            return;
        }

        double targetX = pos.getX() + 0.08 + random.nextDouble() * 0.84;
        double targetY = pos.getY() + 0.75;
        double targetZ = pos.getZ() + 0.08 + random.nextDouble() * 0.84;

        double angle = random.nextDouble() * Math.PI * 2.0;
        double radius = 0.08 + random.nextDouble() * 0.24;
        double startX = targetX + Math.cos(angle) * radius;
        double startY = pos.getY() - 0.25 - random.nextDouble() * 0.45;
        double startZ = targetZ + Math.sin(angle) * radius;
        SimpleParticleType particleType = random.nextFloat() < 0.4F
                ? EnderEndeavors.ENDCLOUD_DUST_PALE
                : ParticleTypes.REVERSE_PORTAL;

        level.addParticle(
                particleType,
                startX,
                startY,
                startZ,
                (targetX - startX) / REVERSE_PORTAL_TRAVEL_TICKS,
                (targetY - startY) / REVERSE_PORTAL_TRAVEL_TICKS,
                (targetZ - startZ) / REVERSE_PORTAL_TRAVEL_TICKS
        );
    }
}
