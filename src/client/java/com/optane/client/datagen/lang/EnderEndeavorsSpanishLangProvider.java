package com.optane.client.datagen.lang;

import com.optane.lang.ModTranslationKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class EnderEndeavorsSpanishLangProvider extends FabricLanguageProvider {
    public EnderEndeavorsSpanishLangProvider(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, "es_es", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder translationBuilder) {
        //region Items
        translationBuilder.add(ModTranslationKeys.CREATIVE_TAB_TITLE, "Ender Endeavors");
        //endregion
    }
}
