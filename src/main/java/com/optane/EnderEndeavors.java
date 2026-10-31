package com.optane;

import com.optane.block.EnderEndeavorsBlocks;
import com.optane.item.EnderEndeavorsCreativeTab;
import com.optane.block.EnderEndeavorsBlocks;
import com.optane.item.EnderEndeavorsItems;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main entry point for the Ender Endeavors.
 */
public class EnderEndeavors implements ModInitializer {

	public static final String MOD_ID = "ender-endeavors";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		EnderEndeavorsItems.register();
		EnderEndeavorsBlocks.register();
		EnderEndeavorsCreativeTab.register();
		LOGGER.info("Ender Endeavors initialized!");
	}

	/**
	 * Creates a namespaced identifier scoped to this mod.
	 *
	 * @param path the resource path within the mod namespace
	 * @return a new {@link Identifier} for the given path
	 */
	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
