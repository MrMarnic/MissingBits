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
import net.minecraft.network.PacketByteBuf;

public class PacketModsProblemConsumer implements PacketConsumer {

    public PacketModsProblemConsumer() {
    }

    public static LoadingInfo.ComparingInfo COMPARING_INFO;

    public static String SERVER_IP;

    public static boolean allowConnectingWhenError;

    public static boolean LOCAL;

    @Environment(EnvType.CLIENT)
    @Override
    public void accept(PacketContext context, PacketByteBuf buffer) {
        COMPARING_INFO = new LoadingInfo.ComparingInfo().readFrom(buffer);
        allowConnectingWhenError = buffer.readBoolean();
        SERVER_IP = MinecraftClient.getInstance().getCurrentServerEntry().address;
        LOCAL = MinecraftClient.getInstance().getCurrentServerEntry().isLocal();
    }
}
