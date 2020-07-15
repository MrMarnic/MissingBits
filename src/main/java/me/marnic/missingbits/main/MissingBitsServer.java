package me.marnic.missingbits.main;

import com.mojang.authlib.minecraft.MinecraftSessionService;
import me.marnic.missingbits.config.MissingBitsConfig;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;

import java.io.File;
import java.io.IOException;

/**
 * Copyright (c) 15.07.2020
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class MissingBitsServer implements DedicatedServerModInitializer {

    public static File CONFIG_FILE;

    @Override
    public void onInitializeServer() {
        CONFIG_FILE = new File(".//config");

        if (!CONFIG_FILE.exists()) {
            CONFIG_FILE.mkdir();
        }

        CONFIG_FILE = new File(".//config//missing_bits_server.json");

        MissingBitsConfig.initServer();

        if (MissingBitsConfig.SERVER_DATA.allowConnectingWhenError) {
            System.out.println("[MissingBits/WARN] \"allowConnectingWhenError\" option is activated. This can be a dangerous option for the user.");
        }
    }
}
