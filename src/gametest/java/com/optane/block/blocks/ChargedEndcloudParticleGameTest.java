package com.optane.block.blocks;

import com.optane.block.EnderEndeavorsBlocks;
import com.optane.particle.EndcloudAmbientParticles;
import com.optane.particle.EnderEndeavorsParticleIds;
import com.optane.particle.EnderEndeavorsParticles;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/** Game tests for Charged Endcloud and shared Endcloud ambient particle behavior. */
public class ChargedEndcloudParticleGameTest {
    private static final BlockPos TEST_POS = new BlockPos(3, 3, 3);
    private static final int SAMPLE_SIZE = 128;

    @GameTest(maxTicks = 600)
    public void customParticleTypesAreRegistered(GameTestHelper helper) {
        helper.assertTrue(
                BuiltInRegistries.PARTICLE_TYPE.containsKey(EnderEndeavorsParticleIds.ENDCLOUD_DUST_PALE),
                "ENDCLOUD_DUST_PALE should be registered"
        );
        helper.assertTrue(
                BuiltInRegistries.PARTICLE_TYPE.containsKey(EnderEndeavorsParticleIds.CHARGED_ENDCLOUD_DUST),
                "CHARGED_ENDCLOUD_DUST should be registered"
        );
        helper.assertTrue(
                BuiltInRegistries.PARTICLE_TYPE.containsKey(EnderEndeavorsParticleIds.CHARGED_ENDCLOUD_DUST_PALE),
                "CHARGED_ENDCLOUD_DUST_PALE should be registered"
        );
        helper.succeed();
    }

    @GameTest(maxTicks = 600)
    public void chargedEndcloudEmitsRegardlessOfBlockBelow(GameTestHelper helper) {
        helper.setBlock(TEST_POS, EnderEndeavorsBlocks.CHARGED_ENDCLOUD);
        BlockPos absolutePos = helper.absolutePos(TEST_POS);

        for (Block blockBelow : new Block[]{
                Blocks.STONE, Blocks.GLASS, Blocks.IRON_BLOCK,
                Blocks.AIR, Blocks.OAK_SLAB, Blocks.OAK_FENCE
        }) {
            helper.setBlock(TEST_POS.below(), blockBelow);
            helper.assertTrue(
                    ChargedEndcloudBlock.canEmitAmbientParticles(helper.getLevel(), absolutePos),
                    "Charged Endcloud should emit particles regardless of block below: " + blockBelow
            );
        }

        helper.succeed();
    }

    @GameTest(maxTicks = 600)
    public void endcloudIsBlockedOnlyByFullBlocksBelow(GameTestHelper helper) {
        helper.setBlock(TEST_POS, EnderEndeavorsBlocks.ENDCLOUD);
        BlockPos absolutePos = helper.absolutePos(TEST_POS);

        for (Block fullBlock : new Block[]{Blocks.STONE, Blocks.GLASS, Blocks.IRON_BLOCK}) {
            helper.setBlock(TEST_POS.below(), fullBlock);
            helper.assertTrue(
                    !EndcloudBlock.canEmitAmbientParticles(helper.getLevel(), absolutePos),
                    "Full collision block below should prevent Endcloud particles: " + fullBlock
            );
        }

        for (Block openBlock : new Block[]{Blocks.AIR, Blocks.OAK_SLAB, Blocks.OAK_FENCE}) {
            helper.setBlock(TEST_POS.below(), openBlock);
            helper.assertTrue(
                    EndcloudBlock.canEmitAmbientParticles(helper.getLevel(), absolutePos),
                    "Non-full block below should allow Endcloud particles: " + openBlock
            );
        }

        helper.succeed();
    }

    @GameTest(maxTicks = 600)
    public void chargedEndcloudParticlesTargetAboveCenterAtEndcloudSpeed(GameTestHelper helper) {
        BlockPos absolutePos = helper.absolutePos(TEST_POS);
        double expectedTargetY = absolutePos.getY() + 1.5;
        double minStartY = absolutePos.getY() - 0.70 - 1.0e-6;
        double maxStartY = absolutePos.getY() - 0.25 + 1.0e-6;

        boolean sawPrimary = false;
        boolean sawPale = false;
        double chargedVelocityYSum = 0.0;
        double endcloudVelocityYSum = 0.0;

        for (long seed = 0; seed < SAMPLE_SIZE; seed++) {
            EndcloudAmbientParticles.Spawn chargedSpawn = EndcloudAmbientParticles.createBlockSpawn(
                    absolutePos,
                    RandomSource.create(seed),
                    ChargedEndcloudBlock.PARTICLE_PROFILE
            );
            EndcloudAmbientParticles.Spawn endcloudSpawn = EndcloudAmbientParticles.createBlockSpawn(
                    absolutePos,
                    RandomSource.create(seed),
                    EndcloudBlock.PARTICLE_PROFILE
            );

            helper.assertTrue(
                    chargedSpawn.startY() >= minStartY && chargedSpawn.startY() <= maxStartY,
                    "Particle should start around the center of the block below; got startY=" + chargedSpawn.startY()
            );
            helper.assertTrue(
                    chargedSpawn.velocityY() > 0.0,
                    "Particle should travel upward toward the block above"
            );

            double reconstructedTargetY = chargedSpawn.startY()
                    + chargedSpawn.velocityY() * ChargedEndcloudBlock.PARTICLE_PROFILE.travelFactor();
            helper.assertTrue(
                    Math.abs(reconstructedTargetY - expectedTargetY) < 1.0e-6,
                    "Particle should target the center of the block above (" + expectedTargetY
                            + "); got " + reconstructedTargetY
            );

            if (chargedSpawn.particleType() == EnderEndeavorsParticles.CHARGED_ENDCLOUD_DUST) {
                sawPrimary = true;
            } else if (chargedSpawn.particleType() == EnderEndeavorsParticles.CHARGED_ENDCLOUD_DUST_PALE) {
                sawPale = true;
            } else {
                helper.fail("Unexpected particle type: " + chargedSpawn.particleType());
            }

            chargedVelocityYSum += chargedSpawn.velocityY();
            endcloudVelocityYSum += endcloudSpawn.velocityY();
        }

        double chargedMeanVelocityY = chargedVelocityYSum / SAMPLE_SIZE;
        double endcloudMeanVelocityY = endcloudVelocityYSum / SAMPLE_SIZE;
        helper.assertTrue(
                Math.abs(chargedMeanVelocityY - endcloudMeanVelocityY) < 0.001,
                "Charged Endcloud mean vertical speed (" + chargedMeanVelocityY
                        + ") should match Endcloud (" + endcloudMeanVelocityY + ")"
        );
        helper.assertTrue(sawPrimary, "Expected CHARGED_ENDCLOUD_DUST particles in sample");
        helper.assertTrue(sawPale, "Expected CHARGED_ENDCLOUD_DUST_PALE particles in sample");
        helper.succeed();
    }
}
