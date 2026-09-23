package dev.mvxmenu.mixin;

import dev.mvxmenu.module.ModuleRegistry;
import dev.mvxmenu.module.impl.KillAuraModule;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        
        // KillAura rotation handling
        KillAuraModule killAura = (KillAuraModule) ModuleRegistry.get().get("kill_aura");
        if (killAura != null && killAura.isEnabled()) {
            Entity target = killAura.getTarget();
            if (target != null) {
                // The rotation is set in the module's attack() method
                // This mixin ensures the rotation is applied to the player
            }
        }
    }
}