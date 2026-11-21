package com.optane.block;

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

/** Game tests for Charged Endcloud mining and loot behavior. */
public class ChargedEndcloudLootGameTest {
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
    public void chargedEndcloudIsMineableWithEveryShovel(GameTestHelper helper) {
        helper.setBlock(TEST_POS, EnderEndeavorsBlocks.CHARGED_ENDCLOUD);
        helper.assertBlockTag(BlockTags.MINEABLE_WITH_SHOVEL, TEST_POS);
        helper.assertTrue(EnderEndeavorsBlocks.CHARGED_ENDCLOUD.defaultBlockState().requiresCorrectToolForDrops(),
                "Charged Endcloud should require the correct tool for player drops");
        helper.assertValueEqual(
                EnderEndeavorsBlocks.CHARGED_ENDCLOUD.defaultBlockState()
                        .getDestroySpeed(helper.getLevel(), helper.absolutePos(TEST_POS)),
                0.6f,
                "Charged Endcloud hardness");

        float handSpeed = ItemStack.EMPTY.getDestroySpeed(EnderEndeavorsBlocks.CHARGED_ENDCLOUD.defaultBlockState());
        for (Item shovel : SHOVELS) {
            float shovelSpeed = new ItemStack(shovel)
                    .getDestroySpeed(EnderEndeavorsBlocks.CHARGED_ENDCLOUD.defaultBlockState());
            helper.assertTrue(shovelSpeed > handSpeed,
                    shovel + " should mine Charged Endcloud faster than an empty hand");
        }

        helper.succeed();
    }

    @GameTest(maxTicks = 600)
    public void everyShovelTierWithoutSilkTouchDropsNothing(GameTestHelper helper) {
        for (int i = 0; i < SHOVELS.length; i++) {
            List<ItemStack> drops = rollDrops(helper, EnderEndeavorsBlocks.CHARGED_ENDCLOUD,
                    new ItemStack(SHOVELS[i]), i);
            assertNoDrops(helper, drops, SHOVELS[i].toString());
        }

        helper.succeed();
    }

    @GameTest(maxTicks = 600)
    public void handAndNonShovelToolsDropNothing(GameTestHelper helper) {
        List<ItemStack> emptyHandDrops = rollDrops(helper, EnderEndeavorsBlocks.CHARGED_ENDCLOUD, ItemStack.EMPTY, 0L);
        assertNoDrops(helper, emptyHandDrops, "empty hand");

        Item[] nonShovels = {
                Items.WOODEN_PICKAXE,
                Items.DIAMOND_PICKAXE,
                Items.IRON_AXE,
                Items.DIAMOND_HOE,
                Items.NETHERITE_SWORD
        };
        for (Item tool : nonShovels) {
            assertNoDrops(helper, rollDrops(helper, EnderEndeavorsBlocks.CHARGED_ENDCLOUD,
                    new ItemStack(tool), 1L), tool.toString());
        }

        helper.succeed();
    }

    @GameTest(maxTicks = 600)
    public void silkTouchOnlyDropsChargedEndcloudFromShovels(GameTestHelper helper) {
        for (int i = 0; i < SHOVELS.length; i++) {
            ItemStack shovel = enchantedTool(helper, SHOVELS[i], Enchantments.SILK_TOUCH, 1);
            List<ItemStack> drops = rollDrops(helper, EnderEndeavorsBlocks.CHARGED_ENDCLOUD, shovel, i);
            helper.assertTrue(drops.size() == 1
                            && drops.getFirst().is(EnderEndeavorsBlocks.CHARGED_ENDCLOUD.asItem())
                            && drops.getFirst().getCount() == 1,
                    "Silk Touch on " + SHOVELS[i] + " should drop exactly one Charged Endcloud");
        }

        ItemStack silkTouchPickaxe = enchantedTool(helper, Items.DIAMOND_PICKAXE, Enchantments.SILK_TOUCH, 1);
        assertNoDrops(helper, rollDrops(helper, EnderEndeavorsBlocks.CHARGED_ENDCLOUD, silkTouchPickaxe, 8L),
                "Silk Touch diamond pickaxe");

        helper.succeed();
    }

    @GameTest(maxTicks = 600)
    public void fortuneShovelsDropNothing(GameTestHelper helper) {
        for (int level = 1; level <= 3; level++) {
            for (int i = 0; i < SHOVELS.length; i++) {
                ItemStack fortuneShovel = enchantedTool(helper, SHOVELS[i], Enchantments.FORTUNE, level);
                List<ItemStack> drops = rollDrops(helper, EnderEndeavorsBlocks.CHARGED_ENDCLOUD,
                        fortuneShovel, level * 100L + i);
                assertNoDrops(helper, drops, "Fortune " + level + " " + SHOVELS[i]);
            }
        }

        helper.succeed();
    }

    @GameTest(maxTicks = 600)
    public void explosionDestroysChargedEndcloudWithoutDrops(GameTestHelper helper) {
        helper.setBlock(TEST_POS, EnderEndeavorsBlocks.CHARGED_ENDCLOUD);
        Vec3 center = Vec3.atCenterOf(helper.absolutePos(TEST_POS));
        helper.getLevel().explode(null, center.x, center.y, center.z, 3.0F, false,
                Level.ExplosionInteraction.BLOCK);

        helper.assertBlockNotPresent(EnderEndeavorsBlocks.CHARGED_ENDCLOUD, TEST_POS);
        helper.assertItemEntityNotPresent(EnderEndeavorsBlocks.CHARGED_ENDCLOUD.asItem(), TEST_POS, 4.0);
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

    private static void assertNoDrops(GameTestHelper helper, List<ItemStack> drops, String toolName) {
        helper.assertTrue(drops.isEmpty(), toolName + " should not drop Charged Endcloud");
    }
}