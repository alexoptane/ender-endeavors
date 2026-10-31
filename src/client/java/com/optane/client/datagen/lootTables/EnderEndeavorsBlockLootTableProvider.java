package com.optane.client.datagen.lootTables;

//import com.optane.registry.ModBlocks;
//import com.optane.registry.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.core.HolderLookup;
import java.util.concurrent.CompletableFuture;

/**
 * Generates all block loot tables for the Ender Endeavors.
 */
public class EnderEndeavorsBlockLootTableProvider extends FabricBlockLootSubProvider {

    /**
     * @param output        pack output destination for generated files
     * @param registries    async registry lookup used by the data generation pipeline
     */
    public EnderEndeavorsBlockLootTableProvider(FabricPackOutput output,
                                          CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    /**
     * Registers one loot table per block; every block in ModBlocks must have an entry here.
     */
    @Override
    public void generate() {
//        add(ModBlocks.ENDCLOUD,
//                createSingleItemTableWithSilkTouch(
//                        ModBlocks.ENDCLOUD,
//                        ModItems.ENDCLOUD_DUST,
//                        ContextIntProviders.between(2, 4)
//                )
//        );
//        add(ModBlocks.CHARGED_ENDCLOUD, noDrop());
//        add(ModBlocks.END_SINGULARITY, noDrop());
    }
}