package com.optane.client.datagen.lootTables;

//import com.optane.registry.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableSubProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * Generates all chest loot tables for the Ender Endeavors.
 */
public class EnderEndeavorsChestLootTableProvider extends SimpleFabricLootTableSubProvider {

    /**
     * Loot table for the chest containing the End Armor upgrade smithing template.
     */
    public static final ResourceKey<LootTable> END_CITY_ARMOR_TEMPLATE =
            ResourceKey.create(Registries.LOOT_TABLE,
                    Identifier.fromNamespaceAndPath("ender-endeavors", "chests/end_city_armor_template"));

    /**
     * Loot table for the chest containing the chestplate-elytra fusion smithing template.
     */
    public static final ResourceKey<LootTable> END_CITY_ELYTRA_TEMPLATE =
            ResourceKey.create(Registries.LOOT_TABLE,
                    Identifier.fromNamespaceAndPath("ender-endeavors", "chests/end_city_elytra_template"));

    /**
     * @param output        pack output destination for generated files
     * @param registries    async registry lookup used by the data generation pipeline
     */
    public EnderEndeavorsChestLootTableProvider(FabricPackOutput output,
                                          CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, LootContextParamSets.CHEST);
    }

    /**
     * Registers one loot table per End City chest variant.
     * @param exporter  consumer that writes each table to disk
     */
    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> exporter) {
//        exporter.accept(END_CITY_ARMOR_TEMPLATE,
//                LootTable.lootTable()
//                        .withPool(LootPool.lootPool()
//                                .setRolls(ContextIntProviders.exactly(1))
//                                .add(LootItem.lootTableItem(ModItems.ARMOR_UPGRADE_TEMPLATE))
//                        )
//        );
//        exporter.accept(END_CITY_ELYTRA_TEMPLATE,
//                LootTable.lootTable()
//                        .withPool(LootPool.lootPool()
//                                .setRolls(ContextIntProviders.exactly(1))
//                                .add(LootItem.lootTableItem(ModItems.ELYTRA_FUSION_TEMPLATE))
//                        )
//        );
    }
}