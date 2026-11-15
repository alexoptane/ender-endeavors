package com.optane.block;

import com.optane.item.EnderEndeavorsItems;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/** Game tests for Endcloud mining and loot behavior. */
public class EndcloudLootGameTest {
    private static final BlockPos TEST_POS = new BlockPos(3, 3, 3);
    private static final Item[] SHOVELS = {
            Items.WOODEN_SHOVEL,
            Items.STONE_SHOVEL,
            Items.COPPER_SHOVEL,
            Items.IRON_SHOVEL,
            Items.GOLDEN_SHOVEL,
            Items.DIAMOND_SHOVEL,
            Items.NETHERITE_SHOVEL
    };

    @GameTest(maxTicks = 600)
    public void endcloudIsMineableWithEveryShovel(GameTestHelper helper) {
        helper.setBlock(TEST_POS, EnderEndeavorsBlocks.ENDCLOUD);
        helper.assertBlockTag(BlockTags.MINEABLE_WITH_SHOVEL, TEST_POS);
        helper.assertTrue(EnderEndeavorsBlocks.ENDCLOUD.defaultBlockState().requiresCorrectToolForDrops(),
                "Endcloud should require the correct tool for player drops");

        float handSpeed = ItemStack.EMPTY.getDestroySpeed(EnderEndeavorsBlocks.ENDCLOUD.defaultBlockState());
        for (Item shovel : SHOVELS) {
            float shovelSpeed = new ItemStack(shovel).getDestroySpeed(EnderEndeavorsBlocks.ENDCLOUD.defaultBlockState());
            helper.assertTrue(shovelSpeed > handSpeed,
                    shovel + " should mine Endcloud faster than an empty hand");
        }

        helper.succeed();
    }

    @GameTest(maxTicks = 600)
    public void everyShovelTierDropsTwoToFourDust(GameTestHelper helper) {
        for (int i = 0; i < SHOVELS.length; i++) {
            List<ItemStack> drops = rollDrops(helper, EnderEndeavorsBlocks.ENDCLOUD,
                    new ItemStack(SHOVELS[i]), i);
            assertDustCount(helper, drops, 2, 4, SHOVELS[i].toString());
        }

        helper.succeed();
    }

    @GameTest(maxTicks = 600)
    public void handAndNonShovelToolsDropNothing(GameTestHelper helper) {
        List<ItemStack> emptyHandDrops = rollDrops(helper, EnderEndeavorsBlocks.ENDCLOUD, ItemStack.EMPTY, 0L);
        assertNoDrops(helper, emptyHandDrops, "empty hand");

        Item[] nonShovels = {
                Items.WOODEN_PICKAXE,
                Items.DIAMOND_PICKAXE,
                Items.IRON_AXE,
                Items.DIAMOND_HOE,
                Items.NETHERITE_SWORD
        };
        for (Item tool : nonShovels) {
            assertNoDrops(helper, rollDrops(helper, EnderEndeavorsBlocks.ENDCLOUD,
                    new ItemStack(tool), 1L), tool.toString());
        }

        helper.succeed();
    }

    @GameTest(maxTicks = 600)
    public void silkTouchOnlyDropsEndcloudFromShovels(GameTestHelper helper) {
        for (int i = 0; i < SHOVELS.length; i++) {
            ItemStack shovel = enchantedTool(helper, SHOVELS[i], Enchantments.SILK_TOUCH, 1);
            List<ItemStack> drops = rollDrops(helper, EnderEndeavorsBlocks.ENDCLOUD, shovel, i);
            helper.assertTrue(drops.size() == 1
                            && drops.getFirst().is(EnderEndeavorsBlocks.ENDCLOUD.asItem()),
                    "Silk Touch on " + SHOVELS[i] + " should drop only Endcloud");
        }

        ItemStack silkTouchPickaxe = enchantedTool(helper, Items.DIAMOND_PICKAXE, Enchantments.SILK_TOUCH, 1);
        assertNoDrops(helper, rollDrops(helper, EnderEndeavorsBlocks.ENDCLOUD, silkTouchPickaxe, 8L),
                "Silk Touch diamond pickaxe");

        helper.succeed();
    }

    @GameTest(maxTicks = 600)
    public void fortuneAddsDustWithoutExceedingFour(GameTestHelper helper) {
        for (int level = 1; level <= 3; level++) {
            int baseTotal = 0;
            int fortuneTotal = 0;
            for (long seed = 0; seed < 128; seed++) {
                int baseCount = dustCount(rollDrops(helper, EnderEndeavorsBlocks.ENDCLOUD,
                        new ItemStack(Items.IRON_SHOVEL), seed));
                ItemStack fortuneShovel = enchantedTool(helper, Items.IRON_SHOVEL, Enchantments.FORTUNE, level);
                int fortuneCount = dustCount(rollDrops(helper, EnderEndeavorsBlocks.ENDCLOUD, fortuneShovel, seed));

                helper.assertTrue(baseCount >= 2 && baseCount <= 4,
                        "Unenchanted shovel should drop between 2 and 4 dust");
                helper.assertTrue(fortuneCount >= 2 && fortuneCount <= 4,
                        "Fortune " + level + " should keep the dust count between 2 and 4");
                baseTotal += baseCount;
                fortuneTotal += fortuneCount;
            }

            helper.assertTrue(fortuneTotal > baseTotal,
                    "Fortune " + level + " should increase the total over the fixed seed sample");
        }

        helper.succeed();
    }

    @GameTest(maxTicks = 600)
    public void explosionDestroysEndcloudWithoutDrops(GameTestHelper helper) {
        helper.setBlock(TEST_POS, EnderEndeavorsBlocks.ENDCLOUD);
        Vec3 center = Vec3.atCenterOf(helper.absolutePos(TEST_POS));
        helper.getLevel().explode(null, center.x, center.y, center.z, 3.0F, false,
                Level.ExplosionInteraction.BLOCK);

        helper.assertBlockNotPresent(EnderEndeavorsBlocks.ENDCLOUD, TEST_POS);
        helper.assertItemEntityNotPresent(EnderEndeavorsBlocks.ENDCLOUD.asItem(), TEST_POS, 4.0);
        helper.assertItemEntityNotPresent(EnderEndeavorsItems.ENDCLOUD_DUST, TEST_POS, 4.0);
        helper.succeed();
    }

    private static List<ItemStack> rollDrops(GameTestHelper helper, Block block, ItemStack tool, long seed) {
        ServerLevel level = helper.getLevel();
        BlockPos absolutePos = helper.absolutePos(TEST_POS);
        LootParams params = new LootParams.Builder(level)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(absolutePos))
                .withParameter(LootContextParams.BLOCK_STATE, block.defaultBlockState())
                .withParameter(LootContextParams.TOOL, tool)
                .create(LootContextParamSets.BLOCK);
        ResourceKey<LootTable> lootTableKey = block.getLootTable().orElseThrow();
        LootTable lootTable = level.getServer().reloadableRegistries().getLootTable(lootTableKey);
        return lootTable.getRandomItems(params, seed);
    }

    private static ItemStack enchantedTool(GameTestHelper helper, Item item,
                                           ResourceKey<Enchantment> enchantment, int level) {
        ItemStack tool = new ItemStack(item);
        Holder<Enchantment> enchantmentHolder = helper.getLevel().registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .getOrThrow(enchantment);
        tool.enchant(enchantmentHolder, level);
        return tool;
    }

    private static void assertDustCount(GameTestHelper helper, List<ItemStack> drops,
                                        int min, int max, String toolName) {
        int dustCount = dustCount(drops);
        helper.assertTrue(drops.stream().allMatch(drop -> drop.is(EnderEndeavorsItems.ENDCLOUD_DUST)),
                toolName + " should drop only Endcloud Dust");
        helper.assertTrue(dustCount >= min && dustCount <= max,
                toolName + " should drop between " + min + " and " + max + " dust; got " + dustCount);
    }

    private static int dustCount(List<ItemStack> drops) {
        return drops.stream()
                .filter(drop -> drop.is(EnderEndeavorsItems.ENDCLOUD_DUST))
                .mapToInt(ItemStack::getCount)
                .sum();
    }

    private static void assertNoDrops(GameTestHelper helper, List<ItemStack> drops, String toolName) {
        helper.assertTrue(drops.isEmpty(), toolName + " should not drop Endcloud or dust");
    }
}
