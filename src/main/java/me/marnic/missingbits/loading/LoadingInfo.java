package me.marnic.missingbits.loading;

import io.netty.buffer.Unpooled;
import me.marnic.missingbits.api.util.IPacketSerializable;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.StringTag;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Copyright (c) 26.07.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class LoadingInfo implements IPacketSerializable<LoadingInfo> {
    private HashMap<String, ModInfo> mods;
    private HashMap<String, ArrayList<String>> registries;
    private String mcVersion;
    private boolean shouldBeUsed = true;

    public LoadingInfo() {
        this.mods = new HashMap<>();
        this.registries = new HashMap<>();
    }

    public LoadingInfo(CompoundTag mods, CompoundTag regs) {
        this.mods = modsFromTag(mods);
        this.registries = regsFromTag(regs);
    }

    public void setMods(HashMap<String, ModInfo> mods) {
        this.mods = mods;
    }

    public void setRegistries(HashMap<String, ArrayList<String>> registries) {
        this.registries = registries;
    }

    public void setMcVersion(String mcVersion) {
        this.mcVersion = mcVersion;
    }

    public HashMap<String, ModInfo> modsFromTag(CompoundTag tag) {

        HashMap<String, ModInfo> modsList = new HashMap<>();

        tag.getKeys().forEach((key) -> {
            CompoundTag modTag = tag.getCompound(key);
            modsList.put(key, new ModInfo(key, modTag.getString("version"), modTag.getString("name"),""));
        });

        return modsList;
    }

    public HashMap<String, ArrayList<String>> regsFromTag(CompoundTag tag) {

        HashMap<String, ArrayList<String>> registries = new HashMap<>();

        for (String key : tag.getKeys()) {

            ArrayList<String> data = new ArrayList<>();

            ListTag list = tag.getList(key, NbtType.STRING);

            list.forEach((li) -> {
                data.add(li.asString());
            });

            registries.put(key, data);
        }

        this.mcVersion = tag.getString("mcVersion");

        return registries;
    }

    public CompoundTag modsAsTag() {
        CompoundTag tag = new CompoundTag();

        mods.forEach((k, v) -> {
            CompoundTag mod = new CompoundTag();
            mod.putString("version", v.getVersion());
            mod.putString("name", v.getModName());
            tag.put(k, mod);
        });

        return tag;
    }

    public CompoundTag registriesAsTag() {
        CompoundTag tag = new CompoundTag();

        tag.putString("mcVersion", mcVersion);

        registries.forEach((name, list) -> {
            ListTag data = new ListTag();
            list.forEach((n) -> {

                data.add(new StringTag(n));
            });
            tag.put(name, data);
        });

        return tag;
    }

    public HashMap<String, ModInfo> getMods() {
        return mods;
    }

    public HashMap<String, ArrayList<String>> getRegistries() {
        return registries;
    }

    public String getMcVersion() {
        return mcVersion;
    }

    public static LoadingInfo fromFile(File modsFile, File regFile) {

        LoadingInfo info = new LoadingInfo();

        try {
            CompoundTag mods = new CompoundTag();
            CompoundTag regs = new CompoundTag();

            if (modsFile.exists()) {
                mods = NbtIo.readCompressed(new FileInputStream(modsFile));
            }
            if (regFile.exists()) {
                regs = NbtIo.readCompressed(new FileInputStream(regFile));
            }


            info = new LoadingInfo(mods, regs);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!modsFile.exists() && !regFile.exists()) {
            info.shouldBeUsed = false;
        }

        return info;
    }

    public ComparingInfo compare(LoadingInfo info) {
        ComparingInfo comparingInfo = new ComparingInfo();

        ArrayList<ModInfo> missing = new ArrayList<>();
        ArrayList<Pair<ModInfo, ModInfo>> updated = new ArrayList<>();

        getMods().forEach((mod, version) -> {
            if (info.getMods().containsKey(mod)) {
                ModInfo modInfo = info.getMods().get(mod);
                if (!version.getVersion().equalsIgnoreCase(modInfo.getVersion())) {
                    updated.add(new Pair<>(version, modInfo));
                }
            } else {
                System.out.println(version.enviroment);
                if(!version.enviroment.equals("server")) {
                    missing.add(version);
                }
            }
        });

        HashMap<String, ArrayList<String>> missingRegs = new HashMap<>();

        info.getRegistries().forEach((k, v) -> {
            if (!getRegistries().containsKey(k)) {
                missingRegs.put(k, new ArrayList<>());
            } else {
                ArrayList<String> msRegs = new ArrayList<>(getRegistries().get(k));

                msRegs.removeAll(v);

                if (!msRegs.isEmpty()) {
                    missingRegs.put(k, msRegs);
                }
            }
        });

        comparingInfo.setData(missing, missingRegs, (info.mcVersion.equalsIgnoreCase(mcVersion)), updated, info.mcVersion, mcVersion);

        return comparingInfo;
    }

    public boolean isShouldBeUsed() {
        return shouldBeUsed;
    }

    @Override
    public LoadingInfo readFrom(PacketByteBuf packetByteBuf) {
        this.mcVersion = readString(packetByteBuf);
        HashMap<String, ModInfo> map = new HashMap<>();

        this.readSerializables(packetByteBuf, ModInfo.class).forEach((mod) -> {
            map.put(mod.getModId(), mod);
        });


        this.mods = map;
        return this;
    }

    @Override
    public PacketByteBuf write() {
        PacketByteBuf byteBuf = new PacketByteBuf(Unpooled.buffer());
        writeString(byteBuf, mcVersion);
        writeSerializables(mods.values(), byteBuf);
        return byteBuf;
    }

    public static class ModInfo implements IPacketSerializable<ModInfo> {
        private String modId;
        private String version;
        private String modName;
        private String enviroment;

        public ModInfo() {
        }

        public ModInfo(String modId, String version, String modName,String enviroment) {
            this.modId = modId;
            this.version = version;
            this.modName = modName;
            this.enviroment = enviroment;
        }

        @Override
        public ModInfo readFrom(PacketByteBuf packetByteBuf) {
            this.modId = readString(packetByteBuf);
            this.version = readString(packetByteBuf);
            this.modName = readString(packetByteBuf);
            return this;
        }

        @Override
        public PacketByteBuf write() {
            PacketByteBuf byteBuf = new PacketByteBuf(Unpooled.buffer());
            writeString(byteBuf, modId);
            writeString(byteBuf, version);
            writeString(byteBuf, modName);

            return byteBuf;
        }

        public String getModId() {
            return modId;
        }

        public String getModName() {
            return modName;
        }

        public String getVersion() {
            return version;
        }

        public String getEnviroment() {
            return enviroment;
        }
    }

    public static class ComparingInfo implements IPacketSerializable<ComparingInfo> {
        private ArrayList<ModInfo> missingMods;
        private ArrayList<Pair<ModInfo, ModInfo>> updated;
        private HashMap<String, ArrayList<String>> missingRegs;
        private int missingContentSize;
        private int missingRegistriesSize;

        private String originalMc;
        private String newMc;

        private boolean mcVersionsEqual;
        private boolean equal;

        public ComparingInfo() {
            this.missingRegs = new HashMap<>();
            this.missingMods = new ArrayList<>();
            this.updated = new ArrayList<>();
        }

        public void setData(ArrayList<ModInfo> missingMods, HashMap<String, ArrayList<String>> missingRegs, boolean versionsEqual, ArrayList<Pair<ModInfo, ModInfo>> updated, String originalMc, String newMc) {
            this.missingMods = missingMods;
            this.missingRegs = missingRegs;
            this.mcVersionsEqual = versionsEqual;
            this.updated = updated;
            this.originalMc = originalMc;
            this.newMc = newMc;

            getMissingRegs().forEach((k, v) -> {
                if (!v.isEmpty()) {
                    missingContentSize += v.size();
                } else {
                    missingRegistriesSize++;
                }
            });

            if (newMc.equalsIgnoreCase("")) {
                this.newMc = "?";
            }

            if (missingMods.size() == 0 && missingRegs.size() == 0 && mcVersionsEqual && updated.size() == 0) {
                equal = true;
            }

            Collections.sort(missingMods, Comparator.comparing(ModInfo::getModName));
            Collections.sort(updated, Comparator.comparing(o -> o.getRight().getModName()));
        }

        @Override
        public ComparingInfo readFrom(PacketByteBuf packetByteBuf) {
            this.missingMods = new ArrayList<>(this.readSerializables(packetByteBuf, ModInfo.class));
            int size = packetByteBuf.readVarInt();

            for (int i = 0; i < size; i++) {
                ModInfo infoLeft = new ModInfo().readFrom(packetByteBuf);
                ModInfo infoRight = new ModInfo().readFrom(packetByteBuf);

                this.updated.add(new Pair<>(infoLeft, infoRight));
            }

            this.originalMc = readString(packetByteBuf);
            this.newMc = readString(packetByteBuf);

            setData(missingMods, missingRegs, (originalMc.equalsIgnoreCase(newMc)), updated, originalMc, newMc);

            return this;
        }

        @Override
        public PacketByteBuf write() {
            PacketByteBuf byteBuf = new PacketByteBuf(Unpooled.buffer());

            writeSerializables(missingMods, byteBuf);

            int size = updated.size();

            byteBuf.writeVarInt(size);

            updated.forEach(pair -> {
                byteBuf.writeBytes(pair.getRight().write());
                byteBuf.writeBytes(pair.getLeft().write());
            });

            writeString(byteBuf, originalMc);
            writeString(byteBuf, newMc);

            return byteBuf;
        }

        public ArrayList<ModInfo> getMissingMods() {
            return missingMods;
        }

        public HashMap<String, ArrayList<String>> getMissingRegs() {
            return missingRegs;
        }

        public ArrayList<Pair<ModInfo, ModInfo>> getUpdated() {
            return updated;
        }

        public String getNewMc() {
            return newMc;
        }

        public String getOriginalMc() {
            return originalMc;
        }

        public boolean isEqual() {
            return equal;
        }

        public boolean isMcVersionsEqual() {
            return mcVersionsEqual;
        }

        public int getMissingContentSize() {
            return missingContentSize;
        }

        public int getMissingRegistriesSize() {
            return missingRegistriesSize;
        }

        public void setEqual(boolean equal) {
            this.equal = equal;
        }
    }
}
