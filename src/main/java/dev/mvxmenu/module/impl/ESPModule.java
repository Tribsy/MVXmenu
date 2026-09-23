package dev.mvxmenu.module.impl;

import dev.mvxmenu.module.Module;
import dev.mvxmenu.module.BooleanSetting;
import dev.mvxmenu.module.ColorSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class ESPModule extends Module {

    private final BooleanSetting players;
    private final BooleanSetting chests;
    private final BooleanSetting mobs;
    private final ColorSetting playerColor;
    private final ColorSetting chestColor;
    private final MinecraftClient mc = MinecraftClient.getInstance();

    public ESPModule() {
        super("esp", "ESP", "Highlight entities and containers through walls", Module.Category.RENDER);
        this.players = registerSetting(new BooleanSetting("players", "Players", "Show player ESP", true));
        this.chests = registerSetting(new BooleanSetting("chests", "Chests", "Show chest ESP", true));
        this.mobs = registerSetting(new BooleanSetting("mobs", "Mobs", "Show mob ESP", false));
        this.playerColor = registerSetting(new ColorSetting("player_color", "Player Color", "ESP color for players", 0xFF00FFFF, true));
        this.chestColor = registerSetting(new ColorSetting("chest_color", "Chest Color", "ESP color for chests", 0xFFFFFF00, true));
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {}

    public void renderESP(ClientWorld world, VertexConsumerProvider providers, double cameraX, double cameraY, double cameraZ) {
        if (mc.player == null) return;

        if (players.getValue()) {
            for (PlayerEntity player : world.getPlayers()) {
                if (player == mc.player || !player.isAlive()) continue;
                renderEntityBox(player, playerColor.getValue(), cameraX, cameraY, cameraZ);
            }
        }

        if (mobs.getValue()) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof LivingEntity && !(entity instanceof PlayerEntity) && entity.isAlive()) {
                    renderEntityBox(entity, 0xFFFF0000, cameraX, cameraY, cameraZ);
                }
            }
        }

        if (chests.getValue()) {
            // Chest ESP would require tile entity tracking
        }
    }

    private void renderEntityBox(Entity entity, int color, double cameraX, double cameraY, double cameraZ) {
        Box box = entity.getBoundingBox().expand(0.1);
        Vec3d min = new Vec3d(box.minX - cameraX, box.minY - cameraY, box.minZ - cameraZ);
        Vec3d max = new Vec3d(box.maxX - cameraX, box.maxY - cameraY, box.maxZ - cameraZ);
    }

    public boolean isPlayers() {
        return players.getValue();
    }

    public boolean isChests() {
        return chests.getValue();
    }

    public boolean isMobs() {
        return mobs.getValue();
    }

    public int getPlayerColor() {
        return playerColor.getValue();
    }

    public int getChestColor() {
        return chestColor.getValue();
    }
}