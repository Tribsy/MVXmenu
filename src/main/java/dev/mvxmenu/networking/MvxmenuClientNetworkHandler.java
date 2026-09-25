package dev.mvxmenu.networking;

import dev.mvxmenu.MvxmenuClient;
import dev.mvxmenu.config.MvxmenuConfig;
import dev.mvxmenu.config.MvxmenuConfigSerializer;
import dev.mvxmenu.module.Module;
import dev.mvxmenu.module.ModuleId;
import dev.mvxmenu.module.ModuleRegistry;
import dev.mvxmenu.ui.widget.ModuleCardWidget;
import dev.mvxmenu.ui.widget.MvxmenuWidget;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MvxmenuClientNetworkHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger("MVXmenu/ClientNetwork");

    public static void handleModuleStateSync(ModuleStateSyncPayload payload) {
        LOGGER.info("Received module state sync: {} = {}", payload.moduleId(), payload.enabled());
        MvxmenuClient client = MvxmenuClient.getInstance();
        ModuleRegistry registry = ModuleRegistry.get();
        Module module = registry.get(ModuleId.from(payload.moduleId()));
        if (module != null) {
            module.setEnabled(payload.enabled());
            if (payload.enabled()) {
                module.onEnable();
            } else {
                module.onDisable();
            }
        }
        if (client != null && client.getScreen() != null) {
            for (MvxmenuWidget widget : client.getScreen().getWidgets()) {
                if (widget instanceof ModuleCardWidget m && m.getId().equals(payload.moduleId())) {
                    m.setEnabled(payload.enabled());
                    break;
                }
            }
        }
    }

    public static void handleConfigSyncResponse(ConfigSyncResponsePayload payload) {
        LOGGER.info("Received config sync response");
        MvxmenuConfigSerializer serializer = new MvxmenuConfigSerializer(FabricLoader.getInstance().getConfigDir());
        MvxmenuConfig config = serializer.deserialize(payload.configJson());
        MvxmenuClient client = MvxmenuClient.getInstance();
        if (client != null && client.getScreen() != null) {
            client.getScreen().setConfig(config);
        }
    }

    public static void handleKeybindAck(KeybindAckPayload payload) {
        LOGGER.info("Received keybind ack: {} = {}", payload.keybindId(), payload.success());
    }
}