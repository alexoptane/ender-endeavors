package com.optane.block;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;

public class LightBlocksGameTest {
	@GameTest(maxTicks = 600)
	public void EndcloudLightsAdjacentBlocks(GameTestHelper helper) {
		assertAdjacentBlockLight(helper, EnderEndeavorsBlocks.ENDCLOUD, 2);
	}

	@GameTest(maxTicks = 600)
	public void chargedEndcloudLightsAdjacentBlocks(GameTestHelper helper) {
		assertAdjacentBlockLight(helper, EnderEndeavorsBlocks.CHARGED_ENDCLOUD, 5);
	}

	private static void assertAdjacentBlockLight(GameTestHelper helper, Block block, int expectedLight) {
		BlockPos source = new BlockPos(3, 3, 3);
		BlockPos neighbor = helper.absolutePos(source.east());
		helper.setBlock(source, block);

		// Lighting runs on a separate thread. Wait for the update queued by setBlock.
		ChunkPos chunk = ChunkPos.containing(helper.absolutePos(source));
		var lightingReady = helper.getLevel().getChunkSource().getLightEngine().waitForPendingTasks(chunk.x(), chunk.z());
		helper.startSequence()
				.thenWaitUntil(() -> helper.assertTrue(lightingReady.isDone(), "waiting for block light propagation"))
				.thenExecute(() -> {
					lightingReady.join();
					helper.assertValueEqual(helper.getLevel().getBrightness(LightLayer.BLOCK, neighbor),
							expectedLight, "block light next to " + block.getName().getString());
				})
				.thenSucceed();
	}
}
