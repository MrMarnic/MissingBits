package me.marnic.missingbits.client.gui.components;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;

/**
 * Copyright (c) 28.07.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BasicWidgetEntry extends AlwaysSelectedEntryListWidget.Entry<BasicWidgetEntry> {

    private TextRenderer renderer;
    private BasicWidgetList list;
    private boolean canBeClicked;

    public BasicWidgetEntry(TextRenderer renderer, BasicWidgetList list) {
        this.renderer = renderer;
        this.list = list;
    }

    @Override
    public void render(int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8, float var9) {

    }

    @Override
    public boolean mouseClicked(double double_1, double double_2, int int_1) {
        if (canBeClicked) {
            setClicked();
        }
        return false;
    }

    public void setCanBeClicked(boolean canBeClicked) {
        this.canBeClicked = canBeClicked;
    }

    public void setClicked() {
        list.setSelected(this);
    }
}
