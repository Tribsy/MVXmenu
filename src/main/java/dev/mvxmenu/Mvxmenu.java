package dev.mvxmenu;

import dev.mvxmenu.command.MvxmenuCommands;
import dev.mvxmenu.config.MvxmenuServerConfig;
import dev.mvxmenu.config.MvxmenuServerConfigSerializer;
import dev.mvxmenu.module.ModuleRegistry;
import dev.mvxmenu.module.ModuleServerManager;
import dev.mvxmenu.networking.MvxmenuNetworking;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mvxmenu implements ModInitializer {

    public static final String MOD_ID = "mvxmenu";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static MvxmenuServerConfig serverConfig;
    private static MvxmenuServerConfigSerializer configSerializer;
    private static ModuleServerManager moduleServerManager;

    @Override
    public void onInitialize() {
        LOGGER.info("[{}] MVXmenu loading", MOD_ID);

        configSerializer = new MvxmenuServerConfigSerializer(
                FabricLoader.getInstance().getConfigDir()
        );
        serverConfig = configSerializer.load();
        LOGGER.info("[{}] Server config loaded from: {}", MOD_ID, configSerializer.getConfigPath());

        moduleServerManager = new ModuleServerManager(serverConfig);
        LOGGER.info("[{}] Module server manager initialized", MOD_ID);

        ModuleRegistry.get().getAll().forEach(m -> LOGGER.debug("Registered module: {}", m.getId()));

        // Initialize networking (registers payload types)
        MvxmenuNetworking.initialize();

        // Register server receivers when server starts
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            MvxmenuNetworking.registerServerReceivers(server);
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            MvxmenuCommands.initialize(moduleServerManager, configSerializer);
            MvxmenuCommands.register(dispatcher);
        });

        LOGGER.info("[{}] MVXmenu loaded successfully", MOD_ID);
    }

    public static MvxmenuServerConfig getServerConfig() {
        return serverConfig;
    }

    public static ModuleServerManager getModuleServerManager() {
        return moduleServerManager;
    }

    public static MvxmenuServerConfigSerializer getConfigSerializer() {
        return configSerializer;
    }
}