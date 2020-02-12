package me.marnic.missingbits.common.packet;

/*
 * Copyright (c) 09.08.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

import me.marnic.missingbits.loading.LoadingInfo;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.PacketConsumer;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.PacketByteBuf;

public class PacketModsProblemConsumer implements PacketConsumer {

    public PacketModsProblemConsumer() {
    }

    public static LoadingInfo.ComparingInfo COMPARING_INFO;

    public static String SERVER_IP;

    @Environment(EnvType.CLIENT)
    @Override
    public void accept(PacketContext context, PacketByteBuf buffer) {
        COMPARING_INFO = new LoadingInfo.ComparingInfo().readFrom(buffer);
        SERVER_IP = MinecraftClient.getInstance().getCurrentServerEntry().address;
    }
}
