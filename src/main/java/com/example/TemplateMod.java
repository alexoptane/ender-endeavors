package com.example;

import com.example.registry.ModCreativeTab;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main entry point for the Endermod.
 */
public class TemplateMod implements ModInitializer {

	public static final String MOD_ID = "template-mod";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModCreativeTab.register();
		LOGGER.info("Endermod initialized!");
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
