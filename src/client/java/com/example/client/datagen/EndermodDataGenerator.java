package com.example.client.datagen;

import com.example.client.datagen.lang.EndermodEnglishLangProvider;
import com.example.client.datagen.lang.EndermodSpanishLangProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class EndermodDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack endermodPack = fabricDataGenerator.createPack();
		endermodPack.addProvider(EndermodEnglishLangProvider::new);
		endermodPack.addProvider(EndermodSpanishLangProvider::new);
	}
}
