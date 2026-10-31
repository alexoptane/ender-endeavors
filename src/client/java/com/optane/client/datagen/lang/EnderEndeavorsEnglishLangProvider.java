package com.optane.client.datagen.lang;

import com.optane.item.EnderEndeavorsCreativeTab;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

/**
 * Generates English language translations for Ender Endeavors.
 */
public class EnderEndeavorsEnglishLangProvider extends FabricLanguageProvider {

    /**
     * Creates a new English language provider instance.
     *
     * @param dataOutput     pack output destination for generated files
     * @param registryLookup async registry lookup used by the data generation pipeline
     */
    public EnderEndeavorsEnglishLangProvider(FabricPackOutput dataOutput,
                                             CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    /**
     * Registers all English translation entries for the mod.
     *
     * @param registryLookup     registry lookup provided by the data generation pipeline
     * @param translationBuilder builder used to register translation entries
     */
    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup,
                                     TranslationBuilder translationBuilder) {
        //region Creative Tabs
        translationBuilder.addCreativeModeTab(EnderEndeavorsCreativeTab.ENDER_ENDEAVORS_TAB_KEY, "Ender Endeavors");
        //endregion

        //region Blocks
        //endregion

        //region Items
        //endregion
    }
}
