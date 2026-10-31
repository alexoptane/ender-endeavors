package com.optane.registry;

import com.optane.EnderEndeavors;

import com.optane.lang.ModTranslationKeys;
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
 * Registers all custom creative mode tabs for the Ender Endeavors.
 */
public final class ModCreativeTab {

    /**
     * Resource key identifying the Ender Endeavors creative tab within the registry.
     */
    public static final ResourceKey<CreativeModeTab> ENDER_ENDEAVORS_TAB_KEY =
            ResourceKey.create(Registries.CREATIVE_MODE_TAB, EnderEndeavors.id("ender-endeavors"));

    /**
     * The registered Ender Endeavors creative mode tab instance.
     */
    public static final CreativeModeTab ENDER_ENDEAVORS_TAB = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            ENDER_ENDEAVORS_TAB_KEY,
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
     * Populates the Ender Endeavors creative tab with all mod items
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
        EnderEndeavors.LOGGER.info("Registering creative tabs for " + EnderEndeavors.MOD_ID);
        EnderEndeavors.LOGGER.info("Registered creative tab key: " + ENDER_ENDEAVORS_TAB_KEY.identifier());
    }
}
