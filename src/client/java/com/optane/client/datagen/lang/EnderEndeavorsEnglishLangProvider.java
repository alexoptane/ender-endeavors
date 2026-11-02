package com.optane.client.datagen.lang;

import com.optane.block.EnderEndeavorsBlocks;
import com.optane.item.EnderEndeavorsCreativeTab;
import com.optane.item.EnderEndeavorsItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

/** Generates English translations. */
public class EnderEndeavorsEnglishLangProvider extends FabricLanguageProvider {

    /**
     * Creates the English translation provider.
     *
     * @param dataOutput pack output destination for generated files
     * @param registryLookup async registry lookup used by the data generation pipeline
     */
    public EnderEndeavorsEnglishLangProvider(FabricPackOutput dataOutput,
                                             CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup,
                                     TranslationBuilder translationBuilder) {
        //region Creative Tabs
        translationBuilder.addCreativeModeTab(EnderEndeavorsCreativeTab.ENDER_ENDEAVORS_TAB_KEY, "Ender Endeavors");
        //endregion

        //region Blocks
        //endregion

        //region Items
        translationBuilder.add(EnderEndeavorsItems.ENDCLOUD_DUST, "Endcloud Dust");
        //endregion
        translationBuilder.add(EnderEndeavorsBlocks.ENDCLOUD, "Endcloud");
    }
}
