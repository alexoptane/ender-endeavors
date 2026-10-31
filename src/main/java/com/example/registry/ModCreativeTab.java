package com.example.registry;

import com.example.TemplateMod;

import com.example.lang.ModTranslationKeys;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * Registers all custom creative mode tabs for the Endermod.
 */
public final class ModCreativeTab {

    /**
     * Resource key identifying the Endermod creative tab within the registry.
     */
    public static final ResourceKey<CreativeModeTab> ENDERMOD_TAB_KEY =
            ResourceKey.create(Registries.CREATIVE_MODE_TAB, TemplateMod.id("endermod"));

    /**
     * The registered Endermod creative mode tab instance.
     */
    public static final CreativeModeTab ENDERMOD_TAB = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            ENDERMOD_TAB_KEY,
            FabricCreativeModeTab.builder()
                    .title(Component.translatable(ModTranslationKeys.CREATIVE_TAB_TITLE))
                    .icon(() -> new ItemStack(Items.ENDER_PEARL))
                    .displayItems(ModCreativeTab::populateTab)
                    .build()
    );

    private ModCreativeTab() {
        /* Utility class — prevents instantiation. */
    }

    /**
     * Populates the Endermod creative tab with all mod items
     * @param context  display context provided by the creative inventory
     * @param output   collector that accepts item stacks to display in the tab
     */
    private static void populateTab(CreativeModeTab.ItemDisplayParameters context,
                                    CreativeModeTab.Output output) {
        output.accept(Items.END_ROD);
    }

    /**
     * Triggers static initialization of the creative tab registration.
     * Must be called during mod initialization.
     */
    public static void register() {
        TemplateMod.LOGGER.info("Registering creative tabs for " + TemplateMod.MOD_ID);
        TemplateMod.LOGGER.info("Registered creative tab key: " + ENDERMOD_TAB_KEY.identifier());
    }
}
