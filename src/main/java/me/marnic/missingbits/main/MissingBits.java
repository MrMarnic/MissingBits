package me.marnic.missingbits.main;

import me.marnic.missingbits.loading.MissingBitsGameLoader;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;

import java.io.File;

/**
 * Copyright (c) 26.07.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class MissingBits implements ClientModInitializer {

    public static File CONFIG_FILE;

    @Override
    public void onInitializeClient() {
        CONFIG_FILE = new File(MinecraftClient.getInstance().runDirectory + "//config");

        if (!CONFIG_FILE.exists()) {
            CONFIG_FILE.mkdir();
        }

        MissingBitsGameLoader.init();
    }
}
