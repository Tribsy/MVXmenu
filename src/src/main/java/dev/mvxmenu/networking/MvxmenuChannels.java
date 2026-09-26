package dev.mvxmenu.networking;

public class MvxmenuChannels {

    public static final String CONFIG_SYNC = "mvxmenu:config_sync";
    public static final String MODULE_STATE = "mvxmenu:module_state";
    public static final String TOGGLE_MODULE = "mvxmenu:toggle_module";
    public static final String KEYBIND_REQUEST = "mvxmenu:keybind_request";
    public static final String KEYBIND_RESPONSE = "mvxmenu:keybind_response";

    public static void registerAll() {
        MvxmenuPayloadRegistry registry = MvxmenuPayloadRegistry.get();
        registry.register(CONFIG_SYNC, PacketDirection.SERVER_TO_CLIENT);
        registry.register(MODULE_STATE, PacketDirection.SERVER_TO_CLIENT);
        registry.register(TOGGLE_MODULE, PacketDirection.CLIENT_TO_SERVER);
        registry.register(KEYBIND_REQUEST, PacketDirection.CLIENT_TO_SERVER);
        registry.register(KEYBIND_RESPONSE, PacketDirection.SERVER_TO_CLIENT);
    }
}
