package com.optane;

import com.optane.block.EnderEndeavorsBlocks;
import com.optane.effect.EnderEndeavorsEffects;
import com.optane.item.EnderEndeavorsCreativeTab;
import com.optane.item.EnderEndeavorsItems;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Initializes Ender Endeavors. */
public class EnderEndeavors implements ModInitializer {

	public static final String MOD_ID = "ender_endeavors";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		EnderEndeavorsItems.register();
		EnderEndeavorsBlocks.register();
		EnderEndeavorsEffects.register();
		EnderEndeavorsCreativeTab.register();
		LOGGER.info("Ender Endeavors initialized!");
	}

	/**
	 * Creates an identifier in the mod namespace.
	 *
	 * @param path resource path
	 * @return namespaced identifier
	 */
	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
