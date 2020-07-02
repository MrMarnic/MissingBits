package me.marnic.missingbits.client.gui.components;

import me.marnic.missingbits.client.gui.widgets.WidgetButtonEntry;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

/**
 * Copyright (c) 28.07.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class MissingPiecesButton extends ButtonWidget {

    public boolean clicked;
    public WidgetButtonEntry buttonEntry;
    private boolean hasContent;

    public MissingPiecesButton(int int_1, int int_2, int int_3, String string_1, PressAction buttonWidget$PressAction_1) {
        super(int_1, int_2, int_3, 20, Text.method_30163(string_1), buttonWidget$PressAction_1);
    }

    public void setEntry(WidgetButtonEntry entry) {
        this.buttonEntry = entry;
    }

    public void open() {

    }

    public void close() {

    }

    @Override
    public void onPress() {
        super.onPress();
        if (!clicked) {
            clicked = true;
            open();
            if (hasContent) {
                String msg = getMessage().getString();
                setMessage(Text.method_30163(msg.substring(0,msg.length()-1) + "-"));
            }
        } else {
            clicked = false;
            close();
            if (hasContent) {
                String msg = getMessage().getString();
                setMessage(Text.method_30163(msg.substring(0,msg.length()-1) + "-"));
            }
        }
    }
}
