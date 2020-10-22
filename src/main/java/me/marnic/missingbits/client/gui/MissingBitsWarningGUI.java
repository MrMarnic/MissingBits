package me.marnic.missingbits.client.gui;

import me.marnic.missingbits.api.util.LogUtil;
import me.marnic.missingbits.client.gui.components.BasicWidgetList;
import me.marnic.missingbits.client.gui.warning.WarningTypesList;
import me.marnic.missingbits.client.lang.MissingBitsLang;
import me.marnic.missingbits.common.packet.PacketModsListConsumer;
import me.marnic.missingbits.common.packet.PacketModsProblemConsumer;
import me.marnic.missingbits.config.MissingBitsConfig;
import me.marnic.missingbits.loading.LoadingInfo;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.SaveLevelScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.world.EditWorldScreen;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.level.storage.LevelSummary;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Copyright (c) 27.07.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class MissingBitsWarningGUI extends Screen {

    private LoadingInfo.ComparingInfo comparingInfo;
    private Screen parentScreen;
    private WorldListWidget.Entry levelItem;
    private File worldFile;

    private WarningTypesList warningTypesList;
    private BasicWidgetList warningInfoList;

    private Type type;

    private String warningReason;

    private ServerInfo serverInfo;

    public MissingBitsWarningGUI(LoadingInfo.ComparingInfo info, Screen parentScreen, WorldListWidget.Entry levelItem, File worldFile) {
        super(MissingBitsLang.forKey(MissingBitsLang.GUI_NAME));
        this.comparingInfo = info;
        this.parentScreen = parentScreen;
        this.levelItem = levelItem;
        this.worldFile = worldFile;
        this.type = Type.SP;
    }

    public MissingBitsWarningGUI(LoadingInfo.ComparingInfo info, String serverIp, boolean local) {
        super(MissingBitsLang.forKey(MissingBitsLang.GUI_NAME));
        this.comparingInfo = info;
        this.parentScreen = new MultiplayerScreen(new TitleScreen());
        this.type = Type.MP;
        this.warningReason = MissingBitsLang.forKey(MissingBitsLang.SERVER_ERROR, "§4" + serverIp).asString();
        this.serverInfo = new ServerInfo("",serverIp,local);
    }

    @Override
    protected void init() {
        super.init();
        warningInfoList = new BasicWidgetList(client, MissingBitsWarningGUI.this.width / 2 - 10, MissingBitsWarningGUI.this.height, 22, MissingBitsWarningGUI.this.height - 65 + 4, 18, textRenderer) {

            @Override
            protected int getScrollbarPositionX() {
                return width * 2 - 7 + 20;
            }

            @Override
            public int getRowWidth() {
                return 150;
            }
        };
        warningInfoList.setLeftPos(width / 2 + 10);

        warningTypesList = new WarningTypesList(client, MissingBitsWarningGUI.this.width / 2 - 10, MissingBitsWarningGUI.this.height, 22, MissingBitsWarningGUI.this.height - 65 + 4, 10, textRenderer, comparingInfo, warningInfoList);


        children.add(warningTypesList);
        children.add(warningInfoList);


        addButton(new ButtonWidget(10, this.height - 30, 100, 20, new TranslatableText(MissingBitsLang.GO_BACK), (a) -> {
            client.openScreen(parentScreen);
        }));

        if (type == Type.MP && PacketModsProblemConsumer.allowConnectingWhenError) {
            addButton(new ButtonWidget(this.width - 110, this.height - 30, 100, 20, new TranslatableText(MissingBitsLang.CON_LOADING), (a) -> {
                PacketModsListConsumer.SERVER_CONTINUE = true;
                client.openScreen(new ConnectScreen(null,client,serverInfo));
            }));
        }

        if (type == Type.SP) {
            addButton(new ButtonWidget(this.width - 110, this.height - 30, 100, 20, new TranslatableText(MissingBitsLang.CON_LOADING), (a) -> {

                try {
                    start();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                //CURRENT FIX "NOT IDEAL"
                /*
                try {
                    METHOD.invoke(levelItem);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                 */
            }));
        }

        if (type == Type.SP) {
            addButton(new ButtonWidget(this.width / 2 - 140 / 2, this.height - 30, 140, 20, new TranslatableText(MissingBitsLang.CREATE_BACKUP), (a) -> {
                try {
                    LevelStorage.Session session = client.getLevelStorage().createSession(worldFile.getName());
                    EditWorldScreen.backupLevel(session);
                    session.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));

            addButton(new ButtonWidget(this.width / 2 - 100 / 2, this.height - 55, 100, 20, new TranslatableText(MissingBitsLang.SAVE_LOG), (a) -> {
                LogUtil.saveLog(comparingInfo, worldFile);
            }));
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        warningTypesList.render(matrices, mouseX, mouseY, delta);
        warningInfoList.render(matrices, mouseX, mouseY, delta);
        drawCenteredString(matrices,textRenderer, MissingBitsLang.textForKey(MissingBitsLang.GUI_NAME), this.width / 2, 10, Formatting.YELLOW.getColorValue());
        if (type == Type.MP) {
            drawCenteredString(matrices,textRenderer, warningReason, this.width / 2, this.height - 50, Formatting.YELLOW.getColorValue());
        }
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return super.isPauseScreen();
    }

    private void start() throws IllegalAccessException {

        LevelSummary summary = (LevelSummary) LEVEL_ITEM_FIELD.get(this.levelItem);

        this.client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        if (this.client.getLevelStorage().levelExists(summary.getName())) {
            this.method_29990();
            this.client.startIntegratedServer(summary.getName());
        }

    }

    private void method_29990() {
        this.client.method_29970(new SaveLevelScreen(new TranslatableText("selectWorld.data_read")));
    }

    public static Method METHOD;
    public static Field LEVEL_ITEM_FIELD;

    static {
        for (Method m : WorldListWidget.Entry.class.getDeclaredMethods()) {
            if (m.getReturnType().equals(void.class) && m.getParameterCount() == 0 && Modifier.isPrivate(m.getModifiers()) && !m.isSynthetic()) {
                METHOD = m;
                break;
            }
        }

        if (METHOD == null) {
            throw new IllegalStateException("LevelItem METHOD is null. Reflection not working. Please report this to the mod author.");
        }

        METHOD.setAccessible(true);

        for (Field f : WorldListWidget.Entry.class.getDeclaredFields()) {
            if (f.getType().equals(LevelSummary.class)) {
                LEVEL_ITEM_FIELD = f;
                break;
            }
        }

        if (LEVEL_ITEM_FIELD != null) {
            LEVEL_ITEM_FIELD.setAccessible(true);
        }
    }

    private enum Type {
        SP, MP
    }
}



