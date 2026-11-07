package com.optane.client.datagen.lang;

import com.optane.block.EnderEndeavorsBlocks;
import com.optane.item.EnderEndeavorsCreativeTab;
import com.optane.item.EnderEndeavorsItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

/** Generates Spanish translations. */
public class EnderEndeavorsSpanishLangProvider extends FabricLanguageProvider {

    /**
     * Creates the Spanish translation provider.
     *
     * @param dataOutput pack output destination for generated files
     * @param registryLookup async registry lookup used by the data generation pipeline
     */
    public EnderEndeavorsSpanishLangProvider(FabricPackOutput dataOutput,
                                             CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, "es_es", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup,
                                     TranslationBuilder translationBuilder) {
        //region Creative Tabs
        translationBuilder.addCreativeModeTab(EnderEndeavorsCreativeTab.ENDER_ENDEAVORS_TAB_KEY, "Ender Endeavors");
        //endregion

        //region Blocks
        translationBuilder.add(EnderEndeavorsBlocks.ENDCLOUD, "Nube del End");
        translationBuilder.add(EnderEndeavorsBlocks.CHARGED_ENDCLOUD, "Nube cargada del End");
        //endregion

        //region Items
        translationBuilder.add(EnderEndeavorsItems.ENDCLOUD_DUST, "Polvo de nube del End");
        //endregion
    }
}
