package com.example.client.datagen.lang;

import com.example.lang.ModTranslationKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class EndermodSpanishLangProvider extends FabricLanguageProvider {
    public EndermodSpanishLangProvider(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, "es_es", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder translationBuilder) {
        //region Items
        translationBuilder.add(ModTranslationKeys.CREATIVE_TAB_TITLE, "Endermod");
        //endregion
    }
}
