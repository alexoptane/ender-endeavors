package com.optane.block;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.LightLayer;

public class LightBlocksGameTest {
	@GameTest(maxTicks = 100)
	public void EndcloudLightsAdjacentBlocks(GameTestHelper helper) {
		BlockPos source = new BlockPos(3, 3, 3);
		BlockPos neighbor = helper.absolutePos(source.east());
		helper.setBlock(source, EnderEndeavorsBlocks.ENDCLOUD);

		helper.succeedWhen(() -> helper.assertValueEqual(
				helper.getLevel().getBrightness(LightLayer.BLOCK, neighbor),
				2,
				"block light next to Charged Endcloud"
		));
	}
	@GameTest(maxTicks = 100)
	public void chargedEndcloudLightsAdjacentBlocks(GameTestHelper helper) {
		BlockPos source = new BlockPos(3, 3, 3);
		BlockPos neighbor = helper.absolutePos(source.east());
		helper.setBlock(source, EnderEndeavorsBlocks.CHARGED_ENDCLOUD);

		helper.succeedWhen(() -> helper.assertValueEqual(
				helper.getLevel().getBrightness(LightLayer.BLOCK, neighbor),
				5,
				"block light next to Charged Endcloud"
		));
	}

}
