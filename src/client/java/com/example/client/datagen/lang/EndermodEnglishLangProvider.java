package com.example.client.datagen.lang;

import com.example.lang.ModTranslationKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class EndermodEnglishLangProvider extends FabricLanguageProvider {
    public EndermodEnglishLangProvider(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder translationBuilder) {
        //region Items
        translationBuilder.add(ModTranslationKeys.CREATIVE_TAB_TITLE, "Endermod");
        //endregion
    }
}
