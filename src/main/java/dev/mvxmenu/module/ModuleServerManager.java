package dev.mvxmenu.module;

import dev.mvxmenu.config.MvxmenuServerConfig;
import dev.mvxmenu.networking.MvxmenuNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ModuleServerManager {

    private static final Logger LOGGER = LoggerFactory.getLogger("MVXmenu/ServerModuleManager");

    private final MvxmenuServerConfig config;
    private final Map<UUID, Map<String, Boolean>> playerModuleStates = new HashMap<>();

    public ModuleServerManager(MvxmenuServerConfig config) {
        this.config = config;
    }

    public MvxmenuServerConfig getConfig() {
        return config;
    }

    public boolean isModuleAllowed(ServerPlayerEntity player, String moduleId) {
        if (!config.isEnforceServerConfig()) {
            return true;
        }
        return config.isModuleAllowed(moduleId);
    }

    public boolean isModuleRestricted(ServerPlayerEntity player, String moduleId) {
        return config.isModuleRestricted(moduleId);
    }

    public void setModuleState(ServerPlayerEntity player, String moduleId, boolean enabled) {
        UUID uuid = player.getUuid();
        playerModuleStates.computeIfAbsent(uuid, k -> new HashMap<>()).put(moduleId, enabled);

        if (config.isLogModuleToggles()) {
            LOGGER.info("Player {} toggled {} to {}", player.getName().getString(), moduleId, enabled);
        }

        if (enabled) {
            Module module = ModuleRegistry.get().get(ModuleId.from(moduleId));
            if (module != null) {
                validateModuleSettings(module);
            }
        }

        MvxmenuNetworking.sendModuleToggle(player, moduleId, enabled);
    }

    public boolean getModuleState(ServerPlayerEntity player, String moduleId) {
        return playerModuleStates.getOrDefault(player.getUuid(), Map.of()).getOrDefault(moduleId, false);
    }

    public void sendAllowedModules(ServerPlayerEntity player) {
        ModuleRegistry registry = ModuleRegistry.get();
        for (Module module : registry.getAll()) {
            boolean allowed = isModuleAllowed(player, module.getId().toString());
            boolean restricted = isModuleRestricted(player, module.getId().toString());
            MvxmenuNetworking.sendModuleToggle(player, module.getId().toString(), allowed);
        }
    }

    public void validateModuleSettings(Module module) {
        if (!config.isEnforceServerConfig()) return;

        for (Setting<?> setting : module.getSettings()) {
            int maxValue = config.getMaxValueForModule(module.getId().toString(), setting.getId());
            if (maxValue != Integer.MAX_VALUE) {
                if (setting instanceof IntegerSetting is) {
                    int current = is.getValue();
                    if (current > maxValue) {
                        is.setValue(maxValue);
                        LOGGER.warn("Clamped {} {} to max value {} for server config", module.getId(), setting.getId(), maxValue);
                    }
                }
            }
        }
    }

    public void onPlayerJoin(ServerPlayerEntity player) {
        sendAllowedModules(player);
        LOGGER.info("Sent module whitelist to player {}", player.getName().getString());
    }

    public void onPlayerLeave(ServerPlayerEntity player) {
        playerModuleStates.remove(player.getUuid());
    }

    public void addToWhitelist(ServerPlayerEntity admin, String moduleId) {
        if (config.isRequirePermissionForAdmin() && !admin.hasPermissionLevel(2)) {
            admin.sendMessage(Text.literal("§cInsufficient permission"), false);
            return;
        }
        config.addToWhitelist(moduleId);
        admin.sendMessage(Text.literal("§aAdded " + moduleId + " to whitelist"), false);
    }

    public void removeFromWhitelist(ServerPlayerEntity admin, String moduleId) {
        if (config.isRequirePermissionForAdmin() && !admin.hasPermissionLevel(2)) {
            admin.sendMessage(Text.literal("§cInsufficient permission"), false);
            return;
        }
        config.removeFromWhitelist(moduleId);
        admin.sendMessage(Text.literal("§aRemoved " + moduleId + " from whitelist"), false);
    }

    public void addToBlacklist(ServerPlayerEntity admin, String moduleId) {
        if (config.isRequirePermissionForAdmin() && !admin.hasPermissionLevel(2)) {
            admin.sendMessage(Text.literal("§cInsufficient permission"), false);
            return;
        }
        config.addToBlacklist(moduleId);
        admin.sendMessage(Text.literal("§aAdded " + moduleId + " to blacklist"), false);
    }

    public void removeFromBlacklist(ServerPlayerEntity admin, String moduleId) {
        if (config.isRequirePermissionForAdmin() && !admin.hasPermissionLevel(2)) {
            admin.sendMessage(Text.literal("§cInsufficient permission"), false);
            return;
        }
        config.removeFromBlacklist(moduleId);
        admin.sendMessage(Text.literal("§aRemoved " + moduleId + " from blacklist"), false);
    }

    public void setMaxValue(String moduleId, String settingName, int value) {
        switch (moduleId + ":" + settingName) {
            case "sprint:sprint_speed" -> config.setMaxSprintSpeed(value);
            case "flight:fly_speed" -> config.setMaxFlySpeed(value);
            case "kill_aura:range" -> config.setMaxKillAuraRange(value);
            case "timer:timer_speed" -> config.setMaxTimerSpeed(value);
        }
    }
}