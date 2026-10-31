package com.optane.lang;

import com.optane.EnderEndeavors;
import net.minecraft.util.Util;

/**
 * Repository and builder for custom translation keys not backed by standard registry objects.
 */
public final class EnderEndeavorsTranslationKeys {

    private EnderEndeavorsTranslationKeys() {
        /* Utility class — prevents instantiation. */
    }

    //region Key Builders

    /**
     * Builds a general text or message translation key scoped to the mod.
     *
     * @param name the translation key suffix
     * @return the formatted translation key
     */
    public static String text(String name) {
        return buildKey("text", name);
    }

    /**
     * Builds an item or smithing template tooltip translation key scoped to the mod.
     *
     * @param name the translation key suffix
     * @return the formatted translation key
     */
    public static String tooltip(String name) {
        return buildKey("tooltip", name);
    }

    /**
     * Builds a custom death attack message translation key scoped to the mod.
     *
     * @param name the death message identifier suffix
     * @return the formatted translation key
     */
    public static String deathAttack(String name) {
        return "death.attack." + EnderEndeavors.MOD_ID + "." + name;
    }

    /**
     * Builds an advancement title or description translation key scoped to the mod.
     *
     * @param name   the advancement identifier path
     * @param suffix the property suffix such as title or description
     * @return the formatted translation key
     */
    public static String advancement(String name, String suffix) {
        return buildKey("advancements", name + "." + suffix);
    }

    /**
     * Builds a screen or container GUI translation key scoped to the mod.
     *
     * @param name the screen element suffix
     * @return the formatted translation key
     */
    public static String container(String name) {
        return buildKey("container", name);
    }

    /**
     * Builds a keybinding translation key scoped to the mod.
     *
     * @param name the keybinding identifier suffix
     * @return the formatted translation key
     */
    public static String keybind(String name) {
        return buildKey("key", name);
    }

    /**
     * Builds a standard Minecraft description ID for a given category and path within the mod namespace.
     *
     * @param category the translation category prefix
     * @param name     the resource path within the mod namespace
     * @return the full translation key string
     */
    private static String buildKey(String category, String name) {
        return Util.makeDescriptionId(category, EnderEndeavors.id(name));
    }

    //endregion

    //region Text & Messages
    //endregion

    //region Tooltips
    //endregion

    //region Death Messages
    //endregion

    //region Advancements
    //endregion

    //region Containers
    //endregion

    //region Keybinds
    //endregion
}
