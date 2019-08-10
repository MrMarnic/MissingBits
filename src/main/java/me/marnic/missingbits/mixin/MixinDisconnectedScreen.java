package me.marnic.missingbits.mixin;

/*
 * Copyright (c) 10.08.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

import me.marnic.missingbits.client.gui.MissingBitsWarningGUI;
import me.marnic.missingbits.common.packet.PacketModsProblemConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(DisconnectedScreen.class)
public class MixinDisconnectedScreen {

    @Shadow
    @Final
    private Text reason;

    @Inject(method = "init", at = @At("HEAD"))
    public void init(CallbackInfo info) {
        if (reason.asFormattedString().equalsIgnoreCase("MissingBitsModsWarning")) {
            MinecraftClient.getInstance().openScreen(new MissingBitsWarningGUI(PacketModsProblemConsumer.COMPARING_INFO, PacketModsProblemConsumer.SERVER_IP));
        }
    }
}
