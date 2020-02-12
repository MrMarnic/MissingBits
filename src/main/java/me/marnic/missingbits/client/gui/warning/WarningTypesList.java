package me.marnic.missingbits.client.gui.warning;

import me.marnic.missingbits.api.util.TextUtil;
import me.marnic.missingbits.client.gui.components.BasicWidgetList;
import me.marnic.missingbits.client.gui.widgets.WidgetStringEntry;
import me.marnic.missingbits.client.lang.MissingBitsLang;
import me.marnic.missingbits.loading.LoadingInfo;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.util.Formatting;

/**
 * Copyright (c) 28.07.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class WarningTypesList extends BasicWidgetList {

    private WidgetStringEntry mcVersionChange, modsUpdated, modsRemoved, regsMissing, contentMissing;
    private LoadingInfo.ComparingInfo comparingInfo;
    private BasicWidgetList infoList;

    public WarningTypesList(MinecraftClient minecraftClient_1, int int_1, int int_2, int int_3, int int_4, int itemHeight, TextRenderer renderer, LoadingInfo.ComparingInfo comparingInfo, BasicWidgetList infoList) {
        super(minecraftClient_1, int_1, int_2, int_3, int_4, 30, renderer);
        this.comparingInfo = comparingInfo;
        this.infoList = infoList;

        mcVersionChange = addString("MC Version Changes (0)", 150 / 2 - renderer.getStringWidth("MC Version Changes (0)") / 2).setOnClicked(() -> {
            clearAll();
            infoList.addString("Minecraft Version Changes: " + (comparingInfo.isMcVersionsEqual() ? 0 : 1), 0).setColor(Formatting.YELLOW.getColorValue());
            if (!comparingInfo.isMcVersionsEqual()) {
                infoList.addString(comparingInfo.getOriginalMc() + " -> " + comparingInfo.getNewMc(), 0);
            }
        });

        for (WidgetStringEntry entry : TextUtil.getForText(MissingBitsLang.textForKey(MissingBitsLang.MC_VERSION_CHANGE), renderer, 150, this)) {
            mcVersionChange.children.add(entry);
        }

        modsUpdated = addString("Mods Updated (0)", 150 / 2 - renderer.getStringWidth("Mods Updated (0)") / 2).setOnClicked(() -> {
            clearAll();
            infoList.addString("Updated Mods: " + comparingInfo.getUpdated().size(), 0).setColor(Formatting.YELLOW.getColorValue());
            comparingInfo.getUpdated().forEach((k) -> {
                infoList.addString("- " + k.getRight().getModName(), 0);
                infoList.addString("(" + k.getRight().getModId() + ")", 0);
                infoList.addString("Old: (" + k.getLeft().getVersion() + ")", 0);
                infoList.addString("New: (" + k.getRight().getVersion() + ")", 0);
            });
        });

        for (WidgetStringEntry entry : TextUtil.getForText(MissingBitsLang.textForKey(MissingBitsLang.MODS_UPDATED), renderer, 150, this)) {
            modsUpdated.children.add(entry);
        }

        modsRemoved = addString("Mods Removed (0)", 150 / 2 - renderer.getStringWidth("Mods Removed (0)") / 2).setOnClicked(() -> {
            clearAll();
            infoList.addString("Removed Mods: " + comparingInfo.getMissingMods().size(), 0).setColor(Formatting.YELLOW.getColorValue());
            comparingInfo.getMissingMods().forEach((k) -> {
                infoList.addString("- " + k.getModName() + " " + k.getVersion(), 0);
                infoList.addString("(" + k.getModId() + ")", 0);
            });
        });

        for (WidgetStringEntry entry : TextUtil.getForText(MissingBitsLang.textForKey(MissingBitsLang.MODS_REMOVED), renderer, 150, this)) {
            modsRemoved.children.add(entry);
        }

        regsMissing = addString("Missing Registries (0)", 150 / 2 - renderer.getStringWidth("Missing Registries (0)") / 2).setOnClicked(() -> {
            clearAll();
            infoList.addString("Missing Registries: " + comparingInfo.getMissingRegs().size(), 0).setColor(Formatting.YELLOW.getColorValue());
            comparingInfo.getMissingRegs().forEach((k, v) -> {
                if (v.isEmpty()) {
                    infoList.addString("Registry Missing:", 0);
                    infoList.addString("- " + k, 0).setColor(Formatting.GOLD.getColorValue());
                }
            });
        });

        for (WidgetStringEntry entry : TextUtil.getForText(MissingBitsLang.textForKey(MissingBitsLang.REGS_MISSING), renderer, 150, this)) {
            regsMissing.children.add(entry);
        }

        contentMissing = addString("Missing Content (0)", 0).setOnClicked(() -> {

            clearAll();
            infoList.addString("Missing Content " + comparingInfo.getMissingContentSize(), 0).setColor(Formatting.YELLOW.getColorValue());

            comparingInfo.getMissingRegs().forEach((k, v) -> {
                if (!v.isEmpty()) {
                    infoList.addString(k + ":", 0).setColor(Formatting.GOLD.getColorValue());
                    for (String s : v) {
                        infoList.addString("-" + s, 0);
                    }
                }
            });
        });

        for (WidgetStringEntry entry : TextUtil.getForText(MissingBitsLang.textForKey(MissingBitsLang.CONTENT_MISSING), renderer, 150, this)) {
            contentMissing.children.add(entry);
        }

        if (!comparingInfo.isMcVersionsEqual()) {
            mcVersionChange.setText("MC Version Changes (1)");
        }

        if (!comparingInfo.getUpdated().isEmpty()) {
            modsUpdated.setText("Mods Updated (" + comparingInfo.getUpdated().size() + ")");
        }

        if (!comparingInfo.getMissingMods().isEmpty()) {
            modsRemoved.setText("Mods Removed (" + comparingInfo.getMissingMods().size() + ")");
        }

        if (!comparingInfo.getMissingRegs().isEmpty()) {
            regsMissing.setText("Missing Registries (" + comparingInfo.getMissingRegistriesSize() + ")");
        }

        if (comparingInfo.getMissingContentSize() > 0) {
            contentMissing.setText("Missing Content (" + comparingInfo.getMissingContentSize() + ")");
        }

        mcVersionChange.topLeft();
        modsUpdated.topLeft();
        modsRemoved.topLeft();
        regsMissing.topLeft();
        contentMissing.topLeft();

        setSelected(mcVersionChange);
        mcVersionChange.mouseClicked(0, 0, 0);
    }

    @Override
    protected int getScrollbarPosition() {
        return width - 7;
    }

    @Override
    public int getRowWidth() {
        return 150;
    }

    private void clearAll() {
        infoList.children().clear();
        infoList.setScrollAmount(0);
    }
}
