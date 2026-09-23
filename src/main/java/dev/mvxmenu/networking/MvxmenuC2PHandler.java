package dev.mvxmenu.networking;

import dev.mvxmenu.networking.MvxmenuC2SPacket;
import dev.mvxmenu.networking.MvxmenuS2CPacket;

public class MvxmenuC2PHandler {

    public static void handleConfigSync(MvxmenuC2SPacket packet) {
        String payload = new String(packet.getPayload());
    }

    public static void handleToggleModule(MvxmenuC2SPacket packet) {
        String payload = new String(packet.getPayload());
    }

    public static void handleKeybindRequest(MvxmenuC2SPacket packet) {
        String payload = new String(packet.getPayload());
    }

    public static void handle(MvxmenuC2SPacket packet) {
        String channel = packet.getChannel();
        if ("mvxmenu:config_sync".equals(channel)) {
            handleConfigSync(packet);
        } else if ("mvxmenu:toggle_module".equals(channel)) {
            handleToggleModule(packet);
        } else if ("mvxmenu:keybind_request".equals(channel)) {
            handleKeybindRequest(packet);
        }
    }
}
