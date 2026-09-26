package dev.mvxmenu.module.impl;

import dev.mvxmenu.module.Module;
import dev.mvxmenu.module.BooleanSetting;
import dev.mvxmenu.module.ModuleId;
import dev.mvxmenu.module.ModuleMetadata;
import net.minecraft.client.MinecraftClient;

import java.util.List;
import java.util.Set;
import java.util.Collections;

public class NoFallModule extends Module {

    private final BooleanSetting damage;
    private final MinecraftClient mc = MinecraftClient.getInstance();

    public NoFallModule() {
        super(ModuleMetadata.builder()
                .id(ModuleId.of("no_fall"))
                .displayName("No Fall")
                .description("Prevent fall damage")
                .category(Module.Category.EXPLOIT)
                .version("1.0.0")
                .dependencies(Set.of())
                .softDependencies(Set.of())
                .authors(List.of("MVXmenu"))
                .homepage("https://github.com/mvxmenu/mvxmenu")
                .build());
        this.damage = registerSetting(new BooleanSetting("damage", "Damage", "Also prevent damage sound", true));
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

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