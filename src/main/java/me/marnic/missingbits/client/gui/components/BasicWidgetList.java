package me.marnic.missingbits.client.gui.components;

import me.marnic.missingbits.client.gui.widgets.WidgetButtonEntry;
import me.marnic.missingbits.client.gui.widgets.WidgetStringEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.util.Formatting;

/**
 * Copyright (c) 28.07.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BasicWidgetList extends AlwaysSelectedEntryListWidget<BasicWidgetEntry> {

    private TextRenderer renderer;

    public BasicWidgetList(MinecraftClient minecraftClient_1, int int_1, int int_2, int int_3, int int_4, int itemHeight, TextRenderer renderer) {
        super(minecraftClient_1, int_1, int_2, int_3, int_4, itemHeight);
        this.renderer = renderer;
    }

    public void addButton(MissingPiecesButton widget) {
        WidgetButtonEntry entry = new WidgetButtonEntry(renderer, widget, this);
        widget.setEntry(entry);
        addEntry(entry);
    }

    public WidgetStringEntry createString(String text, int x) {
        return new WidgetStringEntry(renderer, text, x, 0, Formatting.WHITE.getColorValue(), this);
    }

    public WidgetStringEntry addString(String text, int x) {
        WidgetStringEntry entry = createString(text, x);
        addEntry(entry);
        return entry;
    }

    public int getHeight() {
        return itemHeight;
    }

    public void insertEntry(BasicWidgetEntry entry, int at) {
        children().add(at, entry);
    }

    public void removeEntryP(BasicWidgetEntry entry) {
        removeEntry(entry);
    }

    public void addEntryP(BasicWidgetEntry entry) {
        addEntry(entry);
    }

    public int indexOf(BasicWidgetEntry entry) {
        return children().indexOf(entry);
    }
}
