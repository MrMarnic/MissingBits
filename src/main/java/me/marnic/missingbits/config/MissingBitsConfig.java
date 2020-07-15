package me.marnic.missingbits.config;/*
 * Copyright (c) 08.08.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.marnic.missingbits.main.MissingBitsClient;
import me.marnic.missingbits.main.MissingBitsServer;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

public class MissingBitsConfig {

    public static MissingBitsData DATA;
    public static MissingBitsServerData SERVER_DATA;
    public static Gson GSON;

    public static void init() {
        DATA = new MissingBitsData();
        GSON = new GsonBuilder().setPrettyPrinting().create();
        if (MissingBitsClient.CONFIG_FILE.exists()) {
            try {
                DATA = GSON.fromJson(new FileReader(MissingBitsClient.CONFIG_FILE), MissingBitsData.class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                String json = GSON.toJson(DATA);
                BufferedWriter writer = new BufferedWriter(new FileWriter(MissingBitsClient.CONFIG_FILE));
                writer.write(json);
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void initServer() {
        SERVER_DATA = new MissingBitsServerData();
        GSON = new GsonBuilder().setPrettyPrinting().create();
        if (MissingBitsServer.CONFIG_FILE.exists()) {
            try {
                SERVER_DATA = GSON.fromJson(new FileReader(MissingBitsServer.CONFIG_FILE), MissingBitsServerData.class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                String json = GSON.toJson(SERVER_DATA);
                BufferedWriter writer = new BufferedWriter(new FileWriter(MissingBitsServer.CONFIG_FILE));
                writer.write(json);
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class MissingBitsData {
        public boolean alwaysDoBackup = false;
    }

    public static class MissingBitsServerData {
        public boolean allowConnectingWhenError = false;
    }
}
