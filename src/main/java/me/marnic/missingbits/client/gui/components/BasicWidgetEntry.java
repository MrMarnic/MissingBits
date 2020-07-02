package me.marnic.missingbits.client.gui.components;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.util.math.MatrixStack;

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
    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {

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
