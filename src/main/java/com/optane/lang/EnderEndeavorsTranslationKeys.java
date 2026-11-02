package com.optane.lang;

import com.optane.EnderEndeavors;
import net.minecraft.util.Util;

/** Repository and builder for custom translation keys not backed by standard registry objects. */
public final class EnderEndeavorsTranslationKeys {
    /** Prevents instantiation. */
    private EnderEndeavorsTranslationKeys() {}

    //region Key Builders

    /**
     * Builds a mod text translation key.
     *
     * @param name key suffix
     * @return translation key
     */
    public static String text(String name) {
        return buildKey("text", name);
    }

    /**
     * Builds a mod tooltip translation key.
     *
     * @param name key suffix
     * @return translation key
     */
    public static String tooltip(String name) {
        return buildKey("tooltip", name);
    }

    /**
     * Builds a mod death message translation key.
     *
     * @param name death message suffix
     * @return translation key
     */
    public static String deathAttack(String name) {
        return "death.attack." + EnderEndeavors.MOD_ID + "." + name;
    }

    /**
     * Builds a mod advancement translation key.
     *
     * @param name advancement path
     * @param suffix title or description suffix
     * @return translation key
     */
    public static String advancement(String name, String suffix) {
        return buildKey("advancements", name + "." + suffix);
    }

    /**
     * Builds a mod container translation key.
     *
     * @param name key suffix
     * @return translation key
     */
    public static String container(String name) {
        return buildKey("container", name);
    }

    /**
     * Builds a mod keybinding translation key.
     *
     * @param name keybinding suffix
     * @return translation key
     */
    public static String keybind(String name) {
        return buildKey("key", name);
    }

    /**
     * Builds a mod description ID from a category and path.
     *
     * @param category key prefix
     * @param name resource path
     * @return description ID
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
