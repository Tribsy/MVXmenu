package dev.mvxmenu.module.impl;

import dev.mvxmenu.module.Module;
import dev.mvxmenu.module.BooleanSetting;
import dev.mvxmenu.module.EnumSetting;
import dev.mvxmenu.module.IntegerSetting;
import dev.mvxmenu.module.ModuleId;
import dev.mvxmenu.module.ModuleMetadata;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.Collections;

public class KillAuraModule extends Module {

    private final IntegerSetting range;
    private final EnumSetting<AttackMode> attackMode;
    private final BooleanSetting throughWalls;
    private final BooleanSetting players;
    private final BooleanSetting mobs;
    private final BooleanSetting animals;
    private final BooleanSetting invisible;
    private int attackCooldown = 0;
    private Entity target;

    public enum AttackMode {
        SINGLE, MULTI, SWITCH
    }

    public KillAuraModule() {
        super(ModuleMetadata.builder()
                .id(ModuleId.of("kill_aura"))
                .displayName("Kill Aura")
                .description("Automatically attack nearby entities")
                .category(Module.Category.COMBAT)
                .version("1.0.0")
                .dependencies(Set.of())
                .softDependencies(Set.of())
                .authors(List.of("MVXmenu"))
                .homepage("https://github.com/mvxmenu/mvxmenu")
                .build());
        this.range = registerSetting(new IntegerSetting("range", "Range", "Attack range in blocks", 4, 1, 6));
        this.attackMode = registerSetting(new EnumSetting<>("attack_mode", "Attack Mode", "Target selection mode", AttackMode.SINGLE, AttackMode.class));
        this.throughWalls = registerSetting(new BooleanSetting("through_walls", "Through Walls", "Attack through walls", false));
        this.players = registerSetting(new BooleanSetting("players", "Players", "Target players", true));
        this.mobs = registerSetting(new BooleanSetting("mobs", "Mobs", "Target hostile mobs", true));
        this.animals = registerSetting(new BooleanSetting("animals", "Animals", "Target passive animals", false));
        this.invisible = registerSetting(new BooleanSetting("invisible", "Invisible", "Target invisible entities", false));
    }

    private MinecraftClient getMc() {
        return MinecraftClient.getInstance();
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onTick() {
        MinecraftClient mc = getMc();
        if (mc == null || mc.player == null || mc.world == null || mc.interactionManager == null) return;

        if (attackCooldown > 0) {
            attackCooldown--;
            return;
        }

        Entity target = findTarget();
        if (target == null) return;

        attack(target);
        attackCooldown = 10;
    }

    private Entity findTarget() {
        MinecraftClient mc = getMc();
        if (mc == null || mc.world == null) return null;

        Iterable<Entity> entities = mc.world.getEntities();
        double maxDist = range.getValue();
        double bestDist = Double.MAX_VALUE;
        Entity bestTarget = null;

        for (Entity entity : entities) {
            if (!isValidTarget(entity)) continue;

            double dist = mc.player.distanceTo(entity);
            if (dist > maxDist) continue;

            if (!throughWalls.getValue() && !mc.player.canSee(entity)) continue;

            if (dist < bestDist) {
                bestDist = dist;
                bestTarget = entity;
            }
        }

        this.target = bestTarget;
        return bestTarget;
    }

    private boolean isValidTarget(Entity entity) {
        MinecraftClient mc = getMc();
        if (mc == null) return false;

        if (entity == mc.player) return false;
        if (!entity.isAlive()) return false;
        if (entity.isInvulnerable()) return false;

        if (entity instanceof PlayerEntity) {
            if (!players.getValue()) return false;
            PlayerEntity player = (PlayerEntity) entity;
            if (player.isSpectator() || player.isCreative()) return false;
            if (player.isInvisible() && !invisible.getValue()) return false;
            return true;
        }

        if (entity instanceof MobEntity) {
            if (!mobs.getValue()) return false;
            return true;
        }

        if (entity instanceof LivingEntity) {
            if (!animals.getValue()) return false;
            return true;
        }

        return false;
    }

    private void attack(Entity target) {
        MinecraftClient mc = getMc();
        if (mc == null || mc.player == null || mc.interactionManager == null) return;

        double diffX = target.getX() - mc.player.getX();
        double diffY = target.getY() + target.getEyeHeight(mc.player.getPose()) - (mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()));
        double diffZ = target.getZ() - mc.player.getZ();
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90;
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, dist)));

        mc.player.setYaw(yaw);
        mc.player.setPitch(pitch);

        mc.interactionManager.attackEntity(mc.player, target);
        mc.player.swingHand(net.minecraft.util.Hand.MAIN_HAND);
    }

    public int getRange() {
        return range.getValue();
    }

    public AttackMode getAttackMode() {
        return attackMode.getValue();
    }

    public boolean isThroughWalls() {
        return throughWalls.getValue();
    }

    public Entity getTarget() {
        return target;
    }
}