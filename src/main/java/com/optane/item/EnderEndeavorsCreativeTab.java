package com.optane.item;

import com.optane.EnderEndeavors;
import com.optane.block.EnderEndeavorsBlocks;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/** Registers the mod's creative tab. */
public final class EnderEndeavorsCreativeTab {
    /** Prevents instantiation. */
    private EnderEndeavorsCreativeTab() {}
    /** Triggers static tab registration. Must be called during mod initialization. */
    public static void register() {
        EnderEndeavors.LOGGER.info("Registering creative tab for " + EnderEndeavors.MOD_ID);
    }

    public static final ResourceKey<CreativeModeTab> ENDER_ENDEAVORS_TAB_KEY =
            ResourceKey.create(Registries.CREATIVE_MODE_TAB, EnderEndeavors.id("creative_tab"));

    public static final CreativeModeTab ENDER_ENDEAVORS_TAB = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            ENDER_ENDEAVORS_TAB_KEY,
            FabricCreativeModeTab.builder()
                    .title(Component.translatable(Util.makeDescriptionId("itemGroup", ENDER_ENDEAVORS_TAB_KEY.identifier())))
                    .icon(() -> new ItemStack(Items.ENDER_PEARL))
                    .displayItems(EnderEndeavorsCreativeTab::populateTab)
                    .build()
    );


    /**
     * Adds items to the creative tab.
     *
     * @param context display context
     * @param output tab entries
     */
    private static void populateTab(CreativeModeTab.ItemDisplayParameters context,
                                    CreativeModeTab.Output output) {
        output.accept(EnderEndeavorsItems.ENDCLOUD_DUST);
        output.accept(EnderEndeavorsBlocks.ENDCLOUD);
        output.accept(EnderEndeavorsBlocks.CHARGED_ENDCLOUD);
    }


}
