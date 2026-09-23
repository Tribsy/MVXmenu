package dev.mvxmenu.networking;

import dev.mvxmenu.Mvxmenu;
import dev.mvxmenu.module.ModuleServerManager;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MvxmenuServerNetworkHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger("MVXmenu/ServerNetwork");

    public static void registerReceivers() {
        ServerPlayNetworking.registerGlobalReceiver(ModuleTogglePayload.ID, (payload, context) -> {
            handleModuleToggle(context.player(), payload);
        });

        ServerPlayNetworking.registerGlobalReceiver(ConfigSyncRequestPayload.ID, (payload, context) -> {
            handleConfigSyncRequest(context.player(), payload);
        });

        ServerPlayNetworking.registerGlobalReceiver(KeybindUpdatePayload.ID, (payload, context) -> {
            handleKeybindUpdate(context.player(), payload);
        });
    }

    public static void handleModuleToggle(ServerPlayerEntity player, ModuleTogglePayload payload) {
        ModuleServerManager manager = Mvxmenu.getModuleServerManager();
        if (manager == null) return;

        if (!manager.isModuleAllowed(player, payload.moduleId())) {
            LOGGER.warn("Player {} attempted to toggle restricted module: {}", player.getName().getString(), payload.moduleId());
            MvxmenuNetworking.sendModuleToggle(player, payload.moduleId(), false);
            return;
        }

        if (manager.isModuleRestricted(player, payload.moduleId())) {
            LOGGER.info("Player {} toggled restricted module: {} = {}", player.getName().getString(), payload.moduleId(), payload.enabled());
        }

        manager.setModuleState(player, payload.moduleId(), payload.enabled());
    }

    public static void handleConfigSyncRequest(ServerPlayerEntity player, ConfigSyncRequestPayload payload) {
        ModuleServerManager manager = Mvxmenu.getModuleServerManager();
        if (manager == null) return;

        manager.onPlayerJoin(player);
    }

    public static void handleKeybindUpdate(ServerPlayerEntity player, KeybindUpdatePayload payload) {
        LOGGER.info("Player {} updated keybind: {} = {}", player.getName().getString(), payload.keybindId(), payload.key());
        MvxmenuNetworking.sendKeybindAck(player, payload.keybindId(), true);
    }
}