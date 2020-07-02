package me.marnic.missingbits.client.gui.widgets;

import me.marnic.missingbits.client.gui.components.BasicWidgetEntry;
import me.marnic.missingbits.client.gui.components.BasicWidgetList;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.StringRenderable;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

import java.util.ArrayList;

/**
 * Copyright (c) 28.07.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class WidgetStringEntry extends BasicWidgetEntry {

    private TextRenderer renderer;
    private String text;
    public int x;
    public int y;
    private int color;
    private BasicWidgetList list;
    private Runnable clicked;

    public ArrayList<BasicWidgetEntry> children;

    public WidgetStringEntry(TextRenderer renderer, String text, int x, int y, int color, BasicWidgetList list) {
        super(renderer, list);
        this.renderer = renderer;
        this.text = text;
        this.x = x;
        this.y = y;
        this.color = color;
        this.list = list;
        this.children = new ArrayList<>();
    }

    @Override
    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        renderer.draw(matrices, StringRenderable.plain(text),x + this.x, this.y + y + list.getHeight() / 2 - renderer.fontHeight / 2,color);
        if (!children.isEmpty()) {
            for (BasicWidgetEntry element : children) {
                element.render(matrices,index,y,x,entryWidth,entryHeight,mouseX,mouseY,hovered,tickDelta);
            }
        }
    }

    public WidgetStringEntry setOnClicked(Runnable runnable) {
        this.clicked = runnable;
        setCanBeClicked(true);
        return this;
    }

    @Override
    public boolean mouseClicked(double double_1, double double_2, int int_1) {
        if (clicked != null) {
            clicked.run();
        }
        return super.mouseClicked(double_1, double_2, int_1);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void center(int width) {
        x = width / 2 - renderer.getWidth(text) / 2;
    }

    public void topLeft() {
        y = -11;
        x = 0;
    }

    public void topLeft(int y_plus) {
        y = -11 + y_plus;
        x = 0;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
