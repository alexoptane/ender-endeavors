package com.optane.block.blocks;

import com.optane.particle.EndcloudAmbientParticles;
import com.optane.particle.EnderEndeavorsParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/** Defines the transparent Endcloud block. */
public final class EndcloudBlock extends TransparentBlock {

    /**
     * Creates an Endcloud block.
     *
     * @param properties block settings
     */
    public EndcloudBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public static final EndcloudAmbientParticles.Profile PARTICLE_PROFILE = new EndcloudAmbientParticles.Profile(
            EndcloudAmbientParticles.DEFAULT_EMIT_CHANCE,
            0.75,
            EndcloudAmbientParticles.VANILLA_TRAVEL_FACTOR,
            true,
            ParticleTypes.REVERSE_PORTAL,
            EnderEndeavorsParticles.ENDCLOUD_DUST_PALE
    );

    /** Checks whether an Endcloud block at {@code pos} can emit ambient particles. */
    static boolean canEmitAmbientParticles(BlockGetter level, BlockPos pos) {
        return EndcloudAmbientParticles.canEmitFromBlock(level, pos, PARTICLE_PROFILE);
    }

    /** Emits reverse-portal motes that drift from the block below into this block. */
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        EndcloudAmbientParticles.emitFromBlock(level, pos, random, PARTICLE_PROFILE);
    }
}
