package me.marnic.missingbits.client.gui;

import me.marnic.missingbits.api.util.LogUtil;
import me.marnic.missingbits.client.gui.components.BasicWidgetList;
import me.marnic.missingbits.client.gui.warning.WarningTypesList;
import me.marnic.missingbits.client.lang.MissingBitsLang;
import me.marnic.missingbits.loading.LoadingInfo;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.world.EditWorldScreen;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.Formatting;

import java.io.File;
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
    private WorldListWidget.LevelItem levelItem;
    private File worldFile;

    private WarningTypesList warningTypesList;
    private BasicWidgetList warningInfoList;

    private Type type;

    private String warningReason;

    public MissingBitsWarningGUI(LoadingInfo.ComparingInfo info, Screen parentScreen, WorldListWidget.LevelItem levelItem, File worldFile) {
        super(MissingBitsLang.forKey(MissingBitsLang.GUI_NAME));
        this.comparingInfo = info;
        this.parentScreen = parentScreen;
        this.levelItem = levelItem;
        this.worldFile = worldFile;
        this.type = Type.SP;
    }

    public MissingBitsWarningGUI(LoadingInfo.ComparingInfo info, String serverIp) {
        super(MissingBitsLang.forKey(MissingBitsLang.GUI_NAME));
        this.comparingInfo = info;
        this.parentScreen = new MultiplayerScreen(new TitleScreen());
        this.type = Type.MP;
        this.warningReason = MissingBitsLang.forKey(MissingBitsLang.SERVER_ERROR, "§4" + serverIp).asFormattedString();
    }

    @Override
    protected void init() {
        super.init();

        warningInfoList = new BasicWidgetList(minecraft, MissingBitsWarningGUI.this.width / 2 - 10, MissingBitsWarningGUI.this.height, 22, MissingBitsWarningGUI.this.height - 65 + 4, 18, font) {
            @Override
            protected int getScrollbarPosition() {
                return width * 2 - 7 + 20;
            }

            @Override
            public int getRowWidth() {
                return 150;
            }
        };
        warningInfoList.setLeftPos(width / 2 + 10);

        warningTypesList = new WarningTypesList(minecraft, MissingBitsWarningGUI.this.width / 2 - 10, MissingBitsWarningGUI.this.height, 22, MissingBitsWarningGUI.this.height - 65 + 4, 10, font, comparingInfo, warningInfoList);


        children.add(warningTypesList);
        children.add(warningInfoList);

        addButton(new ButtonWidget(10, this.height - 30, 100, 20, MissingBitsLang.textForKey(MissingBitsLang.GO_BACK), (a) -> {
            minecraft.openScreen(parentScreen);
        }));

        if (type == Type.SP) {
            addButton(new ButtonWidget(this.width - 110, this.height - 30, 100, 20, MissingBitsLang.textForKey(MissingBitsLang.CON_LOADING), (a) -> {
                try {
                    METHOD.invoke(levelItem);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }));
        }

        if (type == Type.SP) {
            addButton(new ButtonWidget(this.width / 2 - 140 / 2, this.height - 30, 140, 20, MissingBitsLang.textForKey(MissingBitsLang.CREATE_BACKUP), (a) -> {
                EditWorldScreen.backupLevel(minecraft.getLevelStorage(), worldFile.getName());
            }));

            addButton(new ButtonWidget(this.width / 2 - 100 / 2, this.height - 55, 100, 20, MissingBitsLang.textForKey(MissingBitsLang.SAVE_LOG), (a) -> {
                LogUtil.saveLog(comparingInfo, worldFile);
            }));
        }
    }

    @Override
    public void render(int int_1, int int_2, float float_1) {
        this.renderDirtBackground(0);
        warningTypesList.render(int_1, int_2, float_1);
        warningInfoList.render(int_1, int_2, float_1);
        drawCenteredString(font, MissingBitsLang.textForKey(MissingBitsLang.GUI_NAME), this.width / 2, 10, Formatting.YELLOW.getColorValue());
        if (type == Type.MP) {
            drawCenteredString(font, warningReason, this.width / 2, this.height - 50, Formatting.YELLOW.getColorValue());
        }
        super.render(int_1, int_2, float_1);
    }

    @Override
    public boolean isPauseScreen() {
        return super.isPauseScreen();
    }

    public static Method METHOD;

    static {
        for (Method m : WorldListWidget.LevelItem.class.getDeclaredMethods()) {
            if (m.getReturnType().equals(void.class) && m.getParameterCount() == 0 && Modifier.isPrivate(m.getModifiers()) && !m.isSynthetic()) {
                METHOD = m;
                break;
            }
        }

        if (METHOD == null) {
            throw new IllegalStateException("LevelItem METHOD is null. Reflection not working. Please report this to the mod author.");
        }

        METHOD.setAccessible(true);
    }

    private enum Type {
        SP, MP
    }
}



