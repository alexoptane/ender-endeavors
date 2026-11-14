package com.optane.client.datagen.lootTables;

import com.optane.block.EnderEndeavorsBlocks;
import com.optane.item.EnderEndeavorsItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.IntLimit;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.LimitCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;

import java.util.concurrent.CompletableFuture;

/**
 * Generates block loot tables.
 */
public class EnderEndeavorsBlockLootTableProvider extends FabricBlockLootSubProvider {

    /**
     * Creates the block loot provider.
     *
     * @param output pack output destination for generated files
     * @param registries async registry lookup used by the data generation pipeline
     */
    public EnderEndeavorsBlockLootTableProvider(FabricPackOutput output,
                                                CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    public void generate() {
        add(EnderEndeavorsBlocks.ENDCLOUD,
                LootTable.lootTable().withPool(
                        LootPool.lootPool()
                                // Explosion loot contexts have no tool, so this gate prevents explosion drops.
                                .when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(this.items, ItemTags.SHOVELS)))
                                .add(AlternativesEntry.alternatives(
                                        LootItem.lootTableItem(EnderEndeavorsBlocks.ENDCLOUD)
                                                .when(this.hasSilkTouch()),
                                        LootItem.lootTableItem(EnderEndeavorsItems.ENDCLOUD_DUST)
                                                .apply(SetItemCountFunction.setCount(ContextIntProviders.between(2, 4)))
                                                .apply(ApplyBonusCount.addUniformBonusCount(
                                                        this.enchantments.getOrThrow(Enchantments.FORTUNE)))
                                                .apply(LimitCount.limitCount(IntLimit.upperBound(4)))
                                ))));
    }
}
