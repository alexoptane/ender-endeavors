//package com.example.client.datagen.lootTables;
//
//import com.example.registry.ModBlocks;
//import com.example.registry.ModItems;
//import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
//import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
//import net.minecraft.core.HolderLookup;
//import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
//import java.util.concurrent.CompletableFuture;
//
///**
// * Generates all block loot tables for the Endermod.
// */
//public class EndermodBlockLootTableProvider extends FabricBlockLootSubProvider {
//
//    /**
//     * @param output        pack output destination for generated files
//     * @param registries    async registry lookup used by the data generation pipeline
//     */
//    public EndermodBlockLootTableProvider(FabricPackOutput output,
//                                          CompletableFuture<HolderLookup.Provider> registries) {
//        super(output, registries);
//    }
//
//    /**
//     * Registers one loot table per block; every block in ModBlocks must have an entry here.
//     */
//    @Override
//    public void generate() {
//        //region Cloud blocks
//        add(ModBlocks.ENDCLOUD,
//                createSingleItemTableWithSilkTouch(
//                        ModBlocks.ENDCLOUD,
//                        ModItems.ENDCLOUD_DUST,
//                        ContextIntProviders.between(2, 4)
//                )
//        );
//        add(ModBlocks.CHARGED_ENDCLOUD, noDrop());
//        //endregion
//
//        //region Resource blocks
//        add(ModBlocks.END_SINGULARITY, noDrop());
//        //endregion
//    }
//}