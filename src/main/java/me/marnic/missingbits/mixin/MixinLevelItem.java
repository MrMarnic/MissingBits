package me.marnic.missingbits.mixin;

import me.marnic.missingbits.loading.MissingBitsGameLoader;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.world.level.storage.LevelSummary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

/**
 * Copyright (c) 26.07.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
@Mixin(WorldListWidget.Entry.class)
public abstract class MixinLevelItem extends AlwaysSelectedEntryListWidget.Entry<WorldListWidget.Entry> implements AutoCloseable {

    @Shadow
    public LevelSummary level;

    @Inject(method = "play", at = @At("HEAD"), cancellable = true)
    public void play(CallbackInfo info) {
        try {
            MissingBitsGameLoader.handleWorldLoad(level, info, getThis());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public WorldListWidget.Entry getThis() {
        return (WorldListWidget.Entry) ((Object) this);
    }
}
