package com.optane.block.blocks;

import com.optane.block.EnderEndeavorsBlocks;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/** Game tests for the Endcloud ambient particle obstruction rule. */
public class EndcloudParticleGameTest {
    private static final BlockPos ENDCLOUD_POS = new BlockPos(3, 3, 3);

    @GameTest(maxTicks = 600)
    public void fullBlocksBelowPreventAmbientParticles(GameTestHelper helper) {
        helper.setBlock(ENDCLOUD_POS, EnderEndeavorsBlocks.ENDCLOUD);
        BlockPos absoluteEndcloudPos = helper.absolutePos(ENDCLOUD_POS);

        for (Block fullBlock : new Block[]{Blocks.STONE, Blocks.GLASS, Blocks.IRON_BLOCK}) {
            helper.setBlock(ENDCLOUD_POS.below(), fullBlock);
            helper.assertTrue(
                    !EndcloudBlock.canEmitAmbientParticles(helper.getLevel(), absoluteEndcloudPos),
                    "A full collision block should prevent Endcloud particles: " + fullBlock
            );
        }

        helper.succeed();
    }

    @GameTest(maxTicks = 600)
    public void airBelowAllowsAmbientParticles(GameTestHelper helper) {
        helper.setBlock(ENDCLOUD_POS, EnderEndeavorsBlocks.ENDCLOUD);
        helper.setBlock(ENDCLOUD_POS.below(), Blocks.AIR);

        helper.assertTrue(
                EndcloudBlock.canEmitAmbientParticles(helper.getLevel(), helper.absolutePos(ENDCLOUD_POS)),
                "Air below Endcloud should allow ambient particles"
        );
        helper.succeed();
    }

    @GameTest(maxTicks = 600)
    public void partialBlocksBelowAllowAmbientParticles(GameTestHelper helper) {
        helper.setBlock(ENDCLOUD_POS, EnderEndeavorsBlocks.ENDCLOUD);
        BlockPos absoluteEndcloudPos = helper.absolutePos(ENDCLOUD_POS);

        for (Block partialBlock : new Block[]{Blocks.OAK_SLAB, Blocks.OAK_FENCE}) {
            helper.setBlock(ENDCLOUD_POS.below(), partialBlock);
            helper.assertTrue(
                    EndcloudBlock.canEmitAmbientParticles(helper.getLevel(), absoluteEndcloudPos),
                    "A partial collision shape should allow Endcloud particles: " + partialBlock
            );
        }

        helper.succeed();
    }
}
