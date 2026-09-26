package dev.mvxmenu.networking;

import dev.mvxmenu.networking.MvxmenuS2CPacket;

public class MvxmenuS2PHandler {

    public static void handleConfigSync(MvxmenuS2CPacket packet) {
        String payload = new String(packet.getPayload());
    }

    public static void handleModuleState(MvxmenuS2CPacket packet) {
        String payload = new String(packet.getPayload());
    }

    public static void handle(MvxmenuS2CPacket packet) {
        String channel = packet.getChannel();
        if ("mvxmenu:config_sync".equals(channel)) {
            handleConfigSync(packet);
        } else if ("mvxmenu:module_state".equals(channel)) {
            handleModuleState(packet);
        }
    }
}
