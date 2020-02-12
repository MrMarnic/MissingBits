package me.marnic.missingbits.mixin;

/*
 * Copyright (c) 10.08.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

import me.marnic.missingbits.main.MissingBits;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftDedicatedServer.class)
public abstract class MixinDedicatedServerStarting implements DedicatedServer {

    @Inject(method = "setupServer", at = @At("HEAD"))
    public void setup(CallbackInfoReturnable returnable) {
        MissingBits.ALL_MODS.setMcVersion(getVersion());
    }
}
