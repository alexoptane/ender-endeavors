package com.example.client.datagen;

import com.example.client.datagen.lang.EndermodEnglishLangProvider;
import com.example.client.datagen.lang.EndermodSpanishLangProvider;
/**
import com.example.client.datagen.lootTables.EndermodBlockLootTableProvider;
import com.example.client.datagen.lootTables.EndermodChestLootTableProvider;
import com.example.client.datagen.lootTables.EndermodEntityLootTableProvider;
*/
import com.example.client.datagen.recipes.EndermodRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class EndermodDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack endermodPack = fabricDataGenerator.createPack();
		endermodPack.addProvider(EndermodEnglishLangProvider::new);
		endermodPack.addProvider(EndermodSpanishLangProvider::new);
		endermodPack.addProvider(EndermodRecipeProvider::new);
		/**
		 * endermodPack.addProvider(EndermodBlockLootTableProvider::new);
		 * endermodPack.addProvider(EndermodChestLootTableProvider::new);
		 * endermodPack.addProvider(EndermodEntityLootTableProvider::new);
		 */
	}
}
