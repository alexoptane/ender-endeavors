package com.optane.client.datagen.tags;

import com.optane.block.EnderEndeavorsBlockItemIds;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;

import java.util.concurrent.CompletableFuture;

/** Generates the block tags used by Ender Endeavors. */
public class EnderEndeavorsBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {
    public EnderEndeavorsBlockTagProvider(FabricPackOutput output,
                                          CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        builder(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(EnderEndeavorsBlockItemIds.ENDCLOUD.block())
                .add(EnderEndeavorsBlockItemIds.CHARGED_ENDCLOUD.block());
    }
}
