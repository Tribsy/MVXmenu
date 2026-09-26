package dev.mvxmenu.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class MvxmenuNetworking {

    private static boolean initialized = false;

    public static void initialize() {
        if (initialized) return;
        initialized = true;

        // Register payload types
        PayloadTypeRegistry.playS2C().register(ModuleStateSyncPayload.ID, ModuleStateSyncPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ConfigSyncResponsePayload.ID, ConfigSyncResponsePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(KeybindAckPayload.ID, KeybindAckPayload.CODEC);
        
        PayloadTypeRegistry.playC2S().register(ModuleTogglePayload.ID, ModuleTogglePayload.CODEC);
        PayloadTypeRegistry.playC2S().register(ConfigSyncRequestPayload.ID, ConfigSyncRequestPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(KeybindUpdatePayload.ID, KeybindUpdatePayload.CODEC);
    }

    public static void registerClientReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(ModuleStateSyncPayload.ID, (payload, context) -> {
            MvxmenuClientNetworkHandler.handleModuleStateSync(payload);
        });
        ClientPlayNetworking.registerGlobalReceiver(ConfigSyncResponsePayload.ID, (payload, context) -> {
            MvxmenuClientNetworkHandler.handleConfigSyncResponse(payload);
        });
        ClientPlayNetworking.registerGlobalReceiver(KeybindAckPayload.ID, (payload, context) -> {
            MvxmenuClientNetworkHandler.handleKeybindAck(payload);
        });
    }

    public static void registerServerReceivers(MinecraftServer server) {
        ServerPlayNetworking.registerGlobalReceiver(ModuleTogglePayload.ID, (payload, context) -> {
            MvxmenuServerNetworkHandler.handleModuleToggle(context.player(), payload);
        });
        ServerPlayNetworking.registerGlobalReceiver(ConfigSyncRequestPayload.ID, (payload, context) -> {
            MvxmenuServerNetworkHandler.handleConfigSyncRequest(context.player(), payload);
        });
        ServerPlayNetworking.registerGlobalReceiver(KeybindUpdatePayload.ID, (payload, context) -> {
            MvxmenuServerNetworkHandler.handleKeybindUpdate(context.player(), payload);
        });
    }

    public static void sendModuleToggle(ServerPlayerEntity player, String moduleId, boolean enabled) {
        ServerPlayNetworking.send(player, new ModuleStateSyncPayload(moduleId, enabled, false));
    }

    public static void broadcastModuleState(String moduleId, boolean enabled) {
        // Would broadcast to all players
    }

    public static void sendConfigSync(ServerPlayerEntity player, String configJson) {
        ServerPlayNetworking.send(player, new ConfigSyncResponsePayload(configJson));
    }

    public static void sendKeybindAck(ServerPlayerEntity player, String keybindId, boolean success) {
        ServerPlayNetworking.send(player, new KeybindAckPayload(keybindId, success));
    }

    // Client-to-server sending
    public static void sendModuleToggleToServer(String moduleId, boolean enabled) {
        ClientPlayNetworking.send(new ModuleTogglePayload(moduleId, enabled));
    }

    public static void sendConfigSyncRequestToServer(String configJson) {
        ClientPlayNetworking.send(new ConfigSyncRequestPayload(configJson));
    }

    public static void sendKeybindUpdateToServer(String keybindId, String key) {
        ClientPlayNetworking.send(new KeybindUpdatePayload(keybindId, key));
    }
}