package com.optane.client.datagen;

import com.optane.client.datagen.lang.EnderEndeavorsEnglishLangProvider;
import com.optane.client.datagen.lang.EnderEndeavorsSpanishLangProvider;
import com.optane.client.datagen.lootTables.EnderEndeavorsBlockLootTableProvider;
import com.optane.client.datagen.lootTables.EnderEndeavorsChestLootTableProvider;
import com.optane.client.datagen.lootTables.EnderEndeavorsEntityLootTableProvider;
import com.optane.client.datagen.recipes.EnderEndeavorsRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class EnderEndeavorsDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack enderEndeavorsPack = fabricDataGenerator.createPack();
		enderEndeavorsPack.addProvider(EnderEndeavorsEnglishLangProvider::new);
		enderEndeavorsPack.addProvider(EnderEndeavorsSpanishLangProvider::new);
		enderEndeavorsPack.addProvider(EnderEndeavorsRecipeProvider::new);
		enderEndeavorsPack.addProvider(EnderEndeavorsBlockLootTableProvider::new);
		enderEndeavorsPack.addProvider(EnderEndeavorsChestLootTableProvider::new);
		enderEndeavorsPack.addProvider(EnderEndeavorsEntityLootTableProvider::new);
	}
}
