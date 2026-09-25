package dev.mvxmenu;

import dev.mvxmenu.config.MvxmenuConfig;
import dev.mvxmenu.config.MvxmenuConfigSerializer;
import dev.mvxmenu.module.ModuleLoader;
import dev.mvxmenu.module.ModuleRegistry;
import dev.mvxmenu.module.Module;
import dev.mvxmenu.networking.MvxmenuNetworking;
import dev.mvxmenu.performance.PerformanceManager;
import dev.mvxmenu.theme.FontRenderer;
import dev.mvxmenu.ui.screen.MvxmenuScreen;
import dev.mvxmenu.ui.widget.ModuleCardWidget;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MvxmenuClient implements ClientModInitializer {

    public static final String MOD_ID = "mvxmenu";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static MvxmenuClient instance;
    private MvxmenuScreen screen;
    private KeyBinding openKeybind;
    private MvxmenuConfig config;
    private MvxmenuConfigSerializer configSerializer;

    @Override
    public void onInitializeClient() {
        instance = this;
        LOGGER.info("[{}] Initializing MVXmenu client", MOD_ID);

        configSerializer = new MvxmenuConfigSerializer(
                FabricLoader.getInstance().getConfigDir()
        );
        config = configSerializer.load();
        LOGGER.info("[{}] Config loaded from: {}", MOD_ID, configSerializer.getConfigPath());

        if (FabricLoader.getInstance().isModLoaded("examplemod")) {
            LOGGER.info("[{}] Integration mod detected: examplemod", MOD_ID);
        }

        MvxmenuNetworking.initialize();
        MvxmenuNetworking.registerClientReceivers();

        PerformanceManager.init();

        // Load modules via Fabric entrance points
        ModuleLoader.loadModules();

        // Initialize custom fonts from resource pack
        FontRenderer.initialize();

        initializeScreen(config);
        registerKeybinds();
        registerTickHandler();
        registerConnectionEvents();

        LOGGER.info("[{}] Client initialized successfully", MOD_ID);
    }

    private void initializeScreen(MvxmenuConfig config) {
        screen = new MvxmenuScreen();
        screen.setConfig(config);

        // Add registered modules to the screen
        ModuleRegistry registry = ModuleRegistry.get();
        for (Module module : registry.getAll()) {
            String category = module.getCategory().getDisplayName().toUpperCase();
            ModuleCardWidget card = new ModuleCardWidget(module);
            screen.addModule(category, card);
        }

        screen.init();
        LOGGER.info("[{}] Screen initialized with {} modules", MOD_ID, registry.getAll().size());
    }

    private void registerKeybinds() {
        openKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.mvxmenu.open",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.mvxmenu.general"
        ));
    }

    private void registerTickHandler() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (openKeybind.wasPressed()) {
                toggleScreen();
            }
            // Tick all modules
            ModuleRegistry.get().onTick();
        });
    }

    private void registerConnectionEvents() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            LOGGER.info("[{}] Joined server, requesting config sync", MOD_ID);
            MvxmenuNetworking.sendConfigSyncRequestToServer(configSerializer.serialize(config));
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            LOGGER.info("[{}] Disconnected from server", MOD_ID);
        });
    }

    private void toggleScreen() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen == null) {
            client.setScreen(new MvxmenuScreenAdapter(screen));
        } else if (client.currentScreen instanceof MvxmenuScreenAdapter) {
            client.setScreen(null);
        }
    }

    public static MvxmenuClient getInstance() {
        return instance;
    }

    public MvxmenuScreen getScreen() {
        return screen;
    }

    public MvxmenuConfig getConfig() {
        return config;
    }

    /**
     * Adapter to make MvxmenuScreen compatible with Minecraft's Screen interface.
     */
    public static class MvxmenuScreenAdapter extends Screen {
        private final MvxmenuScreen mvxmenuScreen;

        public MvxmenuScreenAdapter(MvxmenuScreen mvxmenuScreen) {
            super(Text.literal("MVXmenu"));
            this.mvxmenuScreen = mvxmenuScreen;
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            mvxmenuScreen.render(context, mouseX, mouseY, delta);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return mvxmenuScreen.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            return mvxmenuScreen.keyPressed(keyCode, scanCode, modifiers);
        }

        @Override
        public boolean charTyped(char chr, int modifiers) {
            return mvxmenuScreen.charTyped(chr, modifiers);
        }

        @Override
        public void close() {
            super.close();
        }

        @Override
        public boolean shouldPause() {
            return false;
        }
    }
}