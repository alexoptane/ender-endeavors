package com.example.lang;

/**
 * Translation keys repository
 */
public final class ModTranslationKeys {
    /** Prevents instantiating */
    private ModTranslationKeys() {}
    private static final String TEXT_PREFIX = "text.endermod.";

    /**
     * Translation keys autobuilder with "text.endermod." prefix
     * @param name Translation key suffix
     * @return Full translation key
     */
    private static String text(String name) {
        return TEXT_PREFIX + name;
    }

    //region Items
    public static final String CREATIVE_TAB_TITLE = text("creative_tab_title");
    //endregion

}
