package com.optane.block.blocks;

import com.optane.particle.EndcloudAmbientParticles;
import com.optane.particle.EnderEndeavorsParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/** Defines the transparent charged Endcloud block. */
public final class ChargedEndcloudBlock extends TransparentBlock {

    public static final EndcloudAmbientParticles.Profile PARTICLE_PROFILE = new EndcloudAmbientParticles.Profile(
            EndcloudAmbientParticles.DEFAULT_EMIT_CHANCE,
            1.5,
            EndcloudAmbientParticles.CHARGED_TRAVEL_FACTOR,
            false,
            EnderEndeavorsParticles.CHARGED_ENDCLOUD_DUST,
            EnderEndeavorsParticles.CHARGED_ENDCLOUD_DUST_PALE
    );

    /**
     * Creates a charged Endcloud block.
     *
     * @param properties block settings
     */
    public ChargedEndcloudBlock(BlockBehaviour.Properties properties) { super(properties); }

    /** Checks whether a Charged Endcloud block at {@code pos} can emit ambient particles. */
    static boolean canEmitAmbientParticles(BlockGetter level, BlockPos pos) {
        return EndcloudAmbientParticles.canEmitFromBlock(level, pos, PARTICLE_PROFILE);
    }

    /** Emits extended-lifetime reverse-portal motes from the block below to the center of the block above. */
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        EndcloudAmbientParticles.emitFromBlock(level, pos, random, PARTICLE_PROFILE);
    }
}