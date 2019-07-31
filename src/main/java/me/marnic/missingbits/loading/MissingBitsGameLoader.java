package me.marnic.missingbits.loading;

import me.marnic.missingbits.client.gui.MissingBitsWarningGUI;
import me.marnic.missingbits.client.lang.MissingBitsLang;
import net.fabricmc.loader.ModContainer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.level.storage.LevelSummary;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Copyright (c) 26.07.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class MissingBitsGameLoader {

    public static ArrayList<ModContainer> MODS = new ArrayList<>();

    public static void init() {
        MODS = new ArrayList(FabricLoader.getInstance().getAllMods());
    }

    public static void handleWorldLoad(LevelSummary levelSummary, CallbackInfo callbackInfo, WorldListWidget.LevelItem levelItem) {
        File gameFile = new File(MinecraftClient.getInstance().runDirectory.getAbsolutePath() + "//saves//" + levelSummary.getName());

        File logFolder = new File(gameFile.getAbsolutePath() + "//missing_bits_logs");

        if (!logFolder.exists()) {
            logFolder.mkdir();
        }

        File loadingData = new File(gameFile.getAbsolutePath() + "//missing_bits_data");
        {
            File regFile = new File(loadingData.getAbsolutePath() + "//registries.dat");
            File modsFile = new File(loadingData.getAbsolutePath() + "//mods.dat");

            LoadingInfo info = LoadingInfo.fromFile(modsFile, regFile);

            LoadingInfo.ComparingInfo comparingInfo = info.compare(createLoadingInfo());

            if (!comparingInfo.isEqual() && info.isShouldBeUsed()) {
                callbackInfo.cancel();
                Screen parent = MinecraftClient.getInstance().currentScreen;
                MinecraftClient.getInstance().openScreen(new MissingBitsWarningGUI(comparingInfo, parent, levelItem, gameFile));
            }else {
                if(!info.isShouldBeUsed()) {
                    showFirstLoadToast();
                    LoadingInfo info1 = new LoadingInfo();
                    info1.setMcVersion(levelSummary.getVersion().asFormattedString());
                    LoadingInfo info2 = new LoadingInfo();
                    info2.setMcVersion(MinecraftClient.getInstance().getGame().getVersion().getName());
                    LoadingInfo.ComparingInfo info3 = info1.compare(info2);

                    if(!info3.isMcVersionsEqual()) {
                        callbackInfo.cancel();
                        Screen parent = MinecraftClient.getInstance().currentScreen;
                        MinecraftClient.getInstance().openScreen(new MissingBitsWarningGUI(comparingInfo, parent, levelItem, gameFile));
                    }
                }
            }
        }
    }

    private static void showFirstLoadToast() {
        ToastManager toastManager_1 = MinecraftClient.getInstance().getToastManager();

        Text text_3, text_4;

        text_3 = MissingBitsLang.forKey(MissingBitsLang.WELCOME_TO_MISSING_BITS);
        text_4 = MissingBitsLang.forKey(MissingBitsLang.MISSINGBITS_SAVING);


        toastManager_1.add(new SystemToast(SystemToast.Type.WORLD_BACKUP, text_3, text_4));
    }

    public static void handleWorldSetup(ServerWorld world) {
        File gameFile = world.getSaveHandler().getWorldDir();

        File loadingData = new File(gameFile.getAbsolutePath() + "//missing_bits_data");

        loadingData.mkdir();
        LoadingInfo info = createLoadingInfo();

        File regFile = new File(loadingData.getAbsolutePath() + "//registries.dat");
        File modsFile = new File(loadingData.getAbsolutePath() + "//mods.dat");

        try {
            NbtIo.writeCompressed(info.modsAsTag(), new FileOutputStream(modsFile));
            NbtIo.writeCompressed(info.registriesAsTag(), new FileOutputStream(regFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static LoadingInfo createLoadingInfo() {
        HashMap<String, LoadingInfo.ModInfo> mods = new HashMap<>();

        FabricLoader.getInstance().getAllMods().forEach((mod) -> {
            mods.put(mod.getMetadata().getId(), new LoadingInfo.ModInfo(mod.getMetadata().getId(),mod.getMetadata().getVersion().getFriendlyString(),mod.getMetadata().getName()));
        });

        HashMap<String, ArrayList<String>> regs = new HashMap<>();

        SimpleRegistry registries = (SimpleRegistry) Registry.REGISTRIES;

        for (Object id : registries.getIds()) {
            SimpleRegistry registry = (SimpleRegistry) registries.get((Identifier) id);

            ArrayList<String> data = new ArrayList<>();

            if (registry != null) {
                for (Object identifier : registry.getIds()) {
                    data.add(identifier.toString());
                }
            }

            regs.put(id.toString(), data);
        }

        LoadingInfo info = new LoadingInfo();
        info.setMods(mods);
        info.setRegistries(regs);
        info.setMcVersion(MinecraftClient.getInstance().getGame().getVersion().getName());
        return info;
    }
}
