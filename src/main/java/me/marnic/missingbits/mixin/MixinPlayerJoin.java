package me.marnic.missingbits.mixin;

/*
 * Copyright (c) 10.08.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

import io.netty.buffer.Unpooled;
import me.marnic.missingbits.common.packet.MissingBitsPacketHandler;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class MixinPlayerJoin {
    @Inject(method = "onPlayerConnect", at = @At("RETURN"))
    public void onConnect(ClientConnection clientConnection_1, ServerPlayerEntity serverPlayerEntity_1, CallbackInfo info) {
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(serverPlayerEntity_1, MissingBitsPacketHandler.PACKET_MODS_LIST, new PacketByteBuf(Unpooled.buffer()));
    }
}
