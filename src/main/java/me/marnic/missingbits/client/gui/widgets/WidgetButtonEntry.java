package me.marnic.missingbits.client.gui.widgets;

import me.marnic.missingbits.client.gui.components.BasicWidgetEntry;
import me.marnic.missingbits.client.gui.components.BasicWidgetList;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ButtonWidget;

/**
 * Copyright (c) 28.07.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class WidgetButtonEntry extends BasicWidgetEntry {

    ButtonWidget buttonWidget;

    public WidgetButtonEntry(TextRenderer renderer, ButtonWidget widget, BasicWidgetList list) {
        super(renderer, list);
        this.buttonWidget = widget;
    }

    @Override
    public void render(int x, int y, int var3, int var4, int var5, int var6, int var7, boolean var8, float var9) {
        buttonWidget.y = y;
        buttonWidget.render(var6, var7, var9);
    }

    @Override
    public boolean mouseClicked(double double_1, double double_2, int int_1) {
        buttonWidget.mouseClicked(double_1, double_2, int_1);
        return false;
    }
}
