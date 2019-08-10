package me.marnic.missingbits.api.util;

/*
 * Copyright (c) 09.08.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

import me.marnic.missingbits.loading.LoadingInfo;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class ModsUtil {

    public static boolean isFabricServer;

    public static String getAllModsAsString() {
        Collection<ModContainer> mods = FabricLoader.getInstance().getAllMods();
        StringBuilder builder = new StringBuilder();

        for (ModContainer mod : mods) {
            builder.append(getStringForMod(mod) + ";");
        }

        return builder.toString();
    }

    public static LoadingInfo getAllModsAsList() {
        Collection<ModContainer> mods = FabricLoader.getInstance().getAllMods();

        ArrayList<LoadingInfo.ModInfo> modsList = new ArrayList<>();

        for (ModContainer mod : mods) {
            modsList.add(new LoadingInfo.ModInfo(mod.getMetadata().getId(), mod.getMetadata().getVersion().getFriendlyString(), mod.getMetadata().getName()));
        }

        HashMap<String, LoadingInfo.ModInfo> modsHashMap = new HashMap<>();

        modsList.forEach((modInfo -> {
            modsHashMap.put(modInfo.getModId(), modInfo);
        }));

        LoadingInfo loadingInfo = new LoadingInfo();
        loadingInfo.setMods(modsHashMap);

        return loadingInfo;
    }

    private static String getStringForMod(ModContainer mod) {
        return mod.getMetadata().getId() + "#" + mod.getMetadata().getVersion() + "#" + mod.getMetadata().getName();
    }

    public static LoadingInfo getModsFromString(String mods) {
        ArrayList<LoadingInfo.ModInfo> modsList = new ArrayList<>();


        String[] allMods = mods.split(";");

        for (String mod : allMods) {
            String[] data = mod.split("#");
            modsList.add(new LoadingInfo.ModInfo(data[0], data[1], data[2]));
        }

        HashMap<String, LoadingInfo.ModInfo> modsHashMap = new HashMap<>();

        modsList.forEach((modInfo -> {
            modsHashMap.put(modInfo.getModId(), modInfo);
        }));

        LoadingInfo loadingInfo = new LoadingInfo();
        loadingInfo.setMods(modsHashMap);

        return loadingInfo;
    }
}
