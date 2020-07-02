package me.marnic.missingbits.api.util;

/*
 * Copyright (c) 10.08.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

import net.minecraft.network.PacketByteBuf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface IPacketSerializable<T extends IPacketSerializable> {
    T readFrom(PacketByteBuf packetByteBuf);

    PacketByteBuf write();

    default void writeString(PacketByteBuf byteBuf, String string) {
        int size = string.length();
        byte[] bytes = string.getBytes();
        byteBuf.writeVarInt(size);
        byteBuf.writeBytes(bytes);
    }

    default String readString(PacketByteBuf buf) {
        int size = buf.readVarInt();
        byte[] bytes = new byte[size];
        buf.readBytes(bytes);
        return new String(bytes);
    }

    default void writeSerializables(Collection<? extends IPacketSerializable> collections, PacketByteBuf packetByteBuf) {
        int size = collections.size();
        packetByteBuf.writeVarInt(size);

        for (IPacketSerializable serializable : collections) {
            packetByteBuf.writeBytes(serializable.write());
        }
    }

    default <B extends IPacketSerializable> List<B> readSerializables(PacketByteBuf packetByteBuf, Class<B> cl) {
        int size = packetByteBuf.readVarInt();

        ArrayList<B> list = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            list.add((B) IPacketSerializable.create(cl).readFrom(packetByteBuf));
        }

        return list;
    }

    static IPacketSerializable create(Class<? extends IPacketSerializable> cl) {
        try {
            return cl.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
