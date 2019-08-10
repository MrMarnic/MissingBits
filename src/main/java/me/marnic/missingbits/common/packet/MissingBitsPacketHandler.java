package me.marnic.missingbits.common.packet;

/*
 * Copyright (c) 09.08.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.util.Identifier;

public class MissingBitsPacketHandler {

    public static Identifier PACKET_MODS_PROBLEM = new Identifier("missingbits", "packet_mods_problem");
    public static Identifier PACKET_MODS_LIST = new Identifier("missingbits", "packet_mods_list");

    @Environment(EnvType.CLIENT)
    public static void initClient() {
        ClientSidePacketRegistry.INSTANCE.register(PACKET_MODS_PROBLEM, new PacketModsProblemConsumer());
        ClientSidePacketRegistry.INSTANCE.register(PACKET_MODS_LIST, new PacketModsListConsumer());
    }

    public static void init() {
        ServerSidePacketRegistry.INSTANCE.register(PACKET_MODS_LIST, new PacketModsListConsumer());
    }
}
