package me.marnic.missingbits.client.gui.widgets;

import me.marnic.missingbits.client.gui.components.BasicWidgetEntry;
import me.marnic.missingbits.client.gui.components.BasicWidgetList;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;

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
    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        buttonWidget.y = y;
        buttonWidget.render(matrices, mouseX, mouseY,tickDelta);
    }

    @Override
    public boolean mouseClicked(double double_1, double double_2, int int_1) {
        buttonWidget.mouseClicked(double_1, double_2, int_1);
        return false;
    }
}
