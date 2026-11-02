package com.optane.client.datagen.lootTables;

//import com.optane.registry.ModEntities;
//import com.optane.registry.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricEntityLootSubProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

import java.util.concurrent.CompletableFuture;

/** Provides entity loot tables. */
public class EnderEndeavorsEntityLootTableProvider extends FabricEntityLootSubProvider {

    /**
     * Creates the entity loot provider.
     *
     * @param output pack output destination for generated files
     * @param registries async registry lookup used by the data generation pipeline
     */
    public EnderEndeavorsEntityLootTableProvider(FabricPackOutput output,
                                           CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    public void generate() {
//        add(ModEntities.ENDER_PHANTOM,
//                LootTable.lootTable()
//                        .withPool(LootPool.lootPool()
//                                .setRolls(ContextIntProviders.exactly(1))
//                                .add(LootItem.lootTableItem(ModItems.ENDER_PHANTOM_MEMBRANE))
//                        )
//        );
//
//        add(ModEntities.ENDER_SENTINEL, LootTable.lootTable());

    }
}
