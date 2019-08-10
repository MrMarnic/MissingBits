package me.marnic.missingbits.main;

/*
 * Copyright (c) 09.08.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

import me.marnic.missingbits.api.util.ModsUtil;
import me.marnic.missingbits.common.packet.MissingBitsPacketHandler;
import me.marnic.missingbits.loading.LoadingInfo;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;

public class MissingBits implements ModInitializer {
    public static LoadingInfo ALL_MODS;

    @Override
    public void onInitialize() {
        MissingBits.ALL_MODS = ModsUtil.getAllModsAsList();

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            MissingBits.ALL_MODS.setMcVersion(MinecraftClient.getInstance().getVersionType());
        }

        MissingBitsPacketHandler.init();
    }
}
