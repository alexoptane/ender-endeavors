package com.example.client;

import com.example.client.recipes.ModRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

/* Entry point for all data generators of the mod. */
public class TemplateModDataGenerator implements DataGeneratorEntrypoint {

    /**
     * Registers every data provider that generates mod assets and data.
     * Add a new provider per data category (recipes, loot tables, tags…).
     * @param fabricDataGenerator  entry point provided by Fabric to configure the data generation pack
     */
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModRecipeProvider::new);
    }
}
