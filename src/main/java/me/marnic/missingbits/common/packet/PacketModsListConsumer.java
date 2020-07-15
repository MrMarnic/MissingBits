package me.marnic.missingbits.common.packet;

/*
 * Copyright (c) 10.08.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

import io.netty.buffer.Unpooled;
import me.marnic.missingbits.config.MissingBitsConfig;
import me.marnic.missingbits.loading.LoadingInfo;
import me.marnic.missingbits.main.MissingBits;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketConsumer;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

import java.util.HashMap;
import java.util.UUID;

public class PacketModsListConsumer implements PacketConsumer {

    public static boolean SERVER_CONTINUE = false;

    @Override
    public void accept(PacketContext context, PacketByteBuf buffer) {
        if (context.getPacketEnvironment() == EnvType.CLIENT) {
            if (!SERVER_CONTINUE) {
                PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
                packetByteBuf.writeBytes(MissingBits.ALL_MODS.write());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ClientSidePacketRegistry.INSTANCE.sendToServer(MissingBitsPacketHandler.PACKET_MODS_LIST, packetByteBuf);
            }else {
                SERVER_CONTINUE = false;
            }
        } else {
            LoadingInfo info = new LoadingInfo().readFrom(buffer);
            info.setMcVersion(MissingBits.ALL_MODS.getMcVersion());

            LoadingInfo.ComparingInfo comparingInfo = MissingBits.ALL_MODS.compare(info);

            if (!comparingInfo.isEqual()) {
                PacketByteBuf byteBuf = new PacketByteBuf(Unpooled.buffer());
                byteBuf.writeBytes(comparingInfo.write());
                byteBuf.writeBoolean(MissingBitsConfig.SERVER_DATA.allowConnectingWhenError);
                ServerSidePacketRegistry.INSTANCE.sendToPlayer(context.getPlayer(), MissingBitsPacketHandler.PACKET_MODS_PROBLEM, byteBuf, future -> {
                    ((ServerPlayerEntity) context.getPlayer()).networkHandler.disconnect(new LiteralText("MissingBitsModsWarning"));
                });
            }

        }
    }
}
