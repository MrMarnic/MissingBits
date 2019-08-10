package me.marnic.missingbits.api.util;

import me.marnic.missingbits.client.lang.MissingBitsLang;
import me.marnic.missingbits.loading.LoadingInfo;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Copyright (c) 29.07.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class LogUtil {
    public static void saveLog(LoadingInfo.ComparingInfo info, File worldFile) {

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
        Date date = new Date();

        File logFile = new File(worldFile.getAbsolutePath() + "//missing_bits_logs//" + format.format(date) + ".txt");


        try {
            logFile.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(logFile));

            write("Date: " + format.format(date), writer);
            write("Missing Bits Report:", writer);

            write("World File:" + worldFile.getAbsolutePath(), writer);

            writer.newLine();

            if (!info.isMcVersionsEqual()) {
                write("MC Version changes: 1", writer);
                write(info.getOriginalMc() + " -> " + info.getNewMc(), writer);
            } else {
                write("MC Version changes: 0", writer);
            }

            writer.newLine();

            if (!info.getMissingMods().isEmpty()) {
                write("Removed mods: " + info.getMissingMods().size(), writer);
                info.getMissingMods().forEach((v) -> {
                    try {
                        write("- " + v.getModName() + " " + v.getVersion() + "(" + v.getModId() + ")", writer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                write("Removed mods: 0", writer);
            }

            writer.newLine();

            if (!info.getUpdated().isEmpty()) {
                write("Updated mods: " + info.getUpdated().size(), writer);
                info.getUpdated().forEach((v) -> {
                    try {
                        write("- " + v.getRight().getModName() + " (" + v.getLeft().getVersion() + " -> " + v.getRight().getVersion() + ") (" + v.getRight().getModId() + ")", writer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                write("Updated mods: 0", writer);
            }

            writer.newLine();

            if (!info.getMissingRegs().isEmpty()) {
                write("Missing registries: " + info.getMissingRegistriesSize(), writer);
                info.getMissingRegs().forEach((k, v) -> {
                    if (v.isEmpty()) {
                        try {
                            write("- " + k, writer);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                write("Missing registries: 0", writer);
            }

            if (info.getMissingContentSize() > 0) {
                write("Missing content: " + info.getMissingContentSize(), writer);
                info.getMissingRegs().forEach((k, v) -> {
                    if (!v.isEmpty()) {
                        try {
                            writer.newLine();
                            write(k, writer);
                            write("Content:", writer);
                            v.forEach((a) -> {
                                try {
                                    write("- " + a, writer);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                write("Missing content: 0", writer);
            }

            writer.close();

            createToast(logFile.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createToast(String fileName) {
        ToastManager toastManager_1 = MinecraftClient.getInstance().getToastManager();

        Text text_3, text_4;

        text_3 = MissingBitsLang.forKey(MissingBitsLang.SAVED_LOG);
        text_4 = new LiteralText(fileName);


        toastManager_1.add(new SystemToast(SystemToast.Type.WORLD_BACKUP, text_3, text_4));
    }

    private static void write(String line, BufferedWriter writer) throws IOException {
        writer.write(line);
        writer.newLine();
    }
}
