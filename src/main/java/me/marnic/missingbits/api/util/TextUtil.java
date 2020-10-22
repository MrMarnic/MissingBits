package me.marnic.missingbits.api.util;

import me.marnic.missingbits.client.gui.components.BasicWidgetList;
import me.marnic.missingbits.client.gui.widgets.WidgetStringEntry;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 29.07.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class TextUtil {
    public static ArrayList<WidgetStringEntry> getForText(String text, TextRenderer renderer, int width, BasicWidgetList widgetList) {
        List<OrderedText> list = renderer.wrapLines(StringVisitable.plain(text), width);
        ArrayList<WidgetStringEntry> entries = new ArrayList<>();

        int id = 1;
        for (OrderedText s : list) {
            entries.add(new WidgetStringEntry(renderer, s.toString(), 0, 0, Formatting.GRAY.getColorValue(), widgetList));
            entries.get(id - 1).topLeft(8 * id);
            entries.get(id - 1).x += 2;
            id++;
        }

        return entries;
    }
}
