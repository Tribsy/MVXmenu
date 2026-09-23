package dev.mvxmenu.mixin;

import dev.mvxmenu.MvxmenuClient;
import dev.mvxmenu.module.ModuleRegistry;
import dev.mvxmenu.module.impl.TimerModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        MinecraftClient client = (MinecraftClient) (Object) this;
        
        // Run module ticks
        ModuleRegistry.get().onTick();
        
        // Timer module - modify game speed
        TimerModule timer = (TimerModule) ModuleRegistry.get().get("timer");
        if (timer != null && timer.isEnabled()) {
            float speed = timer.getTimerSpeed() / 100.0f;
            if (client.world != null) {
                // Timer affects the game loop - this is a simplified approach
                // Actual timer would need to modify the tick rate more deeply
            }
        }
    }
}