package dev.mvxmenu.module.impl;

import dev.mvxmenu.module.Module;
import dev.mvxmenu.module.BooleanSetting;
import net.minecraft.client.MinecraftClient;

public class NoFallModule extends Module {

    private final BooleanSetting damage;
    private final MinecraftClient mc = MinecraftClient.getInstance();

    public NoFallModule() {
        super("no_fall", "No Fall", "Prevent fall damage", Module.Category.EXPLOIT);
        this.damage = registerSetting(new BooleanSetting("damage", "Damage", "Also prevent damage sound", true));
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        if (mc.player == null) return;

        if (mc.player.fallDistance > 3.0f) {
            mc.player.fallDistance = 0.0f;
            if (damage.getValue()) {
                mc.player.onLanding();
            }
        }
    }

    public boolean isDamage() {
        return damage.getValue();
    }
}