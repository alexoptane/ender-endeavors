package com.optane.client.datagen.recipes;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.crafting.Recipe;

import java.util.concurrent.CompletableFuture;

/** Provides mod recipes. */
public class EnderEndeavorsRecipeProvider extends FabricRecipeProvider {

    /**
     * Creates the recipe provider.
     *
     * @param output pack output destination for generated files
     * @param registriesFuture async registry lookup used by the data generation pipeline
     */
    public EnderEndeavorsRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    /**
     * Creates the recipe generator.
     *
     * @param registries registry lookup
     * @param recipes recipe registry context
     * @param advancements advancement registry context
     * @return recipe generator
     */
    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries,
                                                  BootstrapContext<Recipe<?>> recipes,
                                                  BootstrapContext<Advancement> advancements) {
        return new RecipeProvider(recipes, advancements) {
            /** Registers recipe groups. */
            @Override
            public void buildRecipes() {
                registerEndcloudRecipe();
            }

            /** Endcloud recipe hook. */
            private void registerEndcloudRecipe() {

//                 --- Example: Endcloud block (4x Endcloud Dust, like Glowstone) ---
//                private void registerEndcloudRecipes() {
//                    shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.ENDCLOUD)
//                        .requires(ModItems.ENDCLOUD_DUST)
//                        .requires(ModItems.ENDCLOUD_DUST)
//                        .requires(ModItems.ENDCLOUD_DUST)
//                        .requires(ModItems.ENDCLOUD_DUST)
//                        .unlockedBy(getHasName(ModItems.ENDCLOUD_DUST), has(ModItems.ENDCLOUD_DUST))
//                        .save(exporter);
//                }
            }
        };
    }

}
