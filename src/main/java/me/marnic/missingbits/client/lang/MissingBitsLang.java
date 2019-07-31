package me.marnic.missingbits.client.lang;

import net.minecraft.text.TranslatableText;

/**
 * Copyright (c) 29.07.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class MissingBitsLang {
    public static String GUI_NAME = "text.missingbits.gui_name";
    public static String GO_BACK = "text.missingbits.go_back";
    public static String CON_LOADING = "text.missingbits.con_loading";
    public static String CREATE_BACKUP = "text.missingbits.create_backup";
    public static String SAVE_LOG = "text.missingbits.save_log";

    public static String MC_VERSION_CHANGE = "text.missingbits.mc_version_changed";
    public static String MODS_REMOVED = "text.missingbits.mods_removed";
    public static String MODS_UPDATED = "text.missingbits.mods_updated";
    public static String REGS_MISSING = "text.missingbits.regs_missing";
    public static String CONTENT_MISSING = "text.missingbits.content_missing";

    public static String SAVED_LOG = "text.missingbits.saved_log";

    public static String WELCOME_TO_MISSING_BITS = "text.missingbits.welcome_to_missingbits";
    public static String MISSINGBITS_SAVING = "text.missingbits.saving";

    public static String textForKey(String key) {
        return new TranslatableText(key).asFormattedString();
    }

    public static TranslatableText forKey(String key) {
        return new TranslatableText(key);
    }
}
