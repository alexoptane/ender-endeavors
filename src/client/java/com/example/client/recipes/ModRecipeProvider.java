package com.example.client.recipes;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.crafting.Recipe;

import java.util.concurrent.CompletableFuture;

/**
 *  Generates all data-driven recipes for the mod.
 */
public class ModRecipeProvider extends FabricRecipeProvider {

    /**
     * @param output            pack output destination for generated files
     * @param registriesFuture  async registry lookup used by the data generation pipeline
     */
    public ModRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    /**
     * Returns the generator that registers all mod recipes.
     * Add a call per recipe group inside {@code buildRecipes()}; keep each group in its own method.
     * @param registries  registry lookup provided by the data generation pipeline
     * @param recipes     bootstrap context used to register recipe entries
     * @param advancements bootstrap context used to register unlock advancements
     */
    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries,
                                                  BootstrapContext<Recipe<?>> recipes,
                                                  BootstrapContext<Advancement> advancements) {
        return new RecipeProvider(recipes, advancements) {
            /**
             * Entry point for recipe registration; delegates to one method per recipe group.
             */
            @Override
            public void buildRecipes() {
                registerEndcloudRecipes();
            }

            /**
             *  Recipes related to Endcloud and its variants.
             */
            private void registerEndcloudRecipes() {

                /** --- Example: Endcloud block (4x Endcloud Dust, like Glowstone) ---
                * private void registerEndcloudRecipes() {
                *     shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.ENDCLOUD)
                *         .requires(ModItems.ENDCLOUD_DUST)
                *         .requires(ModItems.ENDCLOUD_DUST)
                *         .requires(ModItems.ENDCLOUD_DUST)
                *         .requires(ModItems.ENDCLOUD_DUST)
                *         .unlockedBy(getHasName(ModItems.ENDCLOUD_DUST), has(ModItems.ENDCLOUD_DUST))
                *         .save(exporter);
                * }
                */

            }
        };
    }

    /**
     * @return human-readable name shown in data generation logs
     */
    @Override
    public String getName() {

        return "EndMod Recipe Provider";
    }
}
