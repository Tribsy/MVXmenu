package dev.mvxmenu;

import dev.mvxmenu.config.MvxmenuConfig;
import dev.mvxmenu.config.MvxmenuConfigSerializer;
import dev.mvxmenu.module.BooleanSetting;
import dev.mvxmenu.module.ColorSetting;
import dev.mvxmenu.module.DoubleSetting;
import dev.mvxmenu.module.EnumSetting;
import dev.mvxmenu.module.IntegerSetting;
import dev.mvxmenu.module.KeybindSetting;
import dev.mvxmenu.module.Setting;
import dev.mvxmenu.module.Module;
import dev.mvxmenu.module.ModuleLoader;
import dev.mvxmenu.module.ModuleRegistry;
import dev.mvxmenu.module.StringSetting;
import dev.mvxmenu.networking.MvxmenuNetworking;
import dev.mvxmenu.performance.PerformanceManager;
import dev.mvxmenu.ui.screen.MvxmenuScreen;
import dev.mvxmenu.ui.widget.MvxmenuWidget;
import dev.mvxmenu.ui.widget.ModuleCardWidget;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class MvxmenuClient implements ClientModInitializer {

    public static final String MOD_ID = "mvxmenu";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static MvxmenuClient instance;
    private MvxmenuScreen screen;
    private KeyBinding openKeybind;
    private MvxmenuConfig config;
    private MvxmenuConfigSerializer configSerializer;
    private final Map<String, Boolean> moduleStates = new HashMap<>();

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

        ModuleLoader.loadAll();
        LOGGER.info("[{}] Loaded {} modules", MOD_ID, ModuleRegistry.get().getAll().size());

        MvxmenuNetworking.initialize();
        MvxmenuNetworking.registerClientReceivers();

        PerformanceManager.init();

        initializeScreen(config);
        registerKeybinds();
        registerTickHandler();
        registerConnectionEvents();

        loadModuleStates(config);
        LOGGER.info("[{}] Client initialized successfully", MOD_ID);
    }

    private void initializeScreen(MvxmenuConfig config) {
        screen = new MvxmenuScreen();
        screen.setConfig(config);

        ModuleRegistry registry = ModuleRegistry.get();
        for (Module.Category category : Module.Category.values()) {
            String categoryName = category.getDisplayName().toUpperCase();
            for (Module module : registry.getByCategory(category)) {
                Boolean savedState = moduleStates.get(module.getId());
                if (savedState != null) {
                    module.setEnabled(savedState);
                }
                ModuleCardWidget card = module.createCardWidget();
                if (savedState != null) {
                    card.setEnabled(savedState);
                }
                screen.addModule(categoryName, card);
            }
        }

        screen.init();
        LOGGER.info("[{}] Screen initialized with {} widgets across {} categories",
                MOD_ID, screen.getWidgets().size(), Module.Category.values().length);
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
            saveModuleStates();
        });
    }

    private void toggleScreen() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen == null) {
            client.setScreen(new MvxmenuScreenWrapper(screen));
        } else if (client.currentScreen instanceof MvxmenuScreenWrapper) {
            client.setScreen(null);
        }
    }

    private void loadModuleStates(MvxmenuConfig config) {
        NbtCompound nbt = config.getModuleStates();
        if (nbt != null) {
            for (String key : nbt.getKeys()) {
                moduleStates.put(key, nbt.getBoolean(key));
            }
        }
        loadModuleKeybinds(config);
        loadModuleSettings(config);
    }

    private void loadModuleKeybinds(MvxmenuConfig config) {
        NbtCompound nbt = config.getModuleKeybinds();
        if (nbt == null) return;
        for (Module module : ModuleRegistry.get().getAll()) {
            for (Setting<?> setting : module.getSettings()) {
                if (setting instanceof KeybindSetting ks) {
                    String key = module.getId() + ":" + setting.getId();
                    if (nbt.contains(key)) {
                        ks.setKey(nbt.getInt(key));
                    }
                }
            }
        }
    }

    private void saveModuleStates() {
        if (screen == null) return;
        NbtCompound nbt = new NbtCompound();
        for (MvxmenuWidget widget : screen.getWidgets()) {
            if (widget instanceof ModuleCardWidget m) {
                nbt.putBoolean(m.getId(), m.isEnabled());
            }
        }
        NbtCompound keybinds = saveModuleKeybinds();
        NbtCompound settings = saveModuleSettings();
        if (config != null) {
            config.setModuleStates(nbt);
            config.setModuleKeybinds(keybinds);
            config.setModuleSettings(settings);
            configSerializer.save(config);
        }
    }

    private NbtCompound saveModuleKeybinds() {
        NbtCompound nbt = new NbtCompound();
        for (Module module : ModuleRegistry.get().getAll()) {
            for (Setting<?> setting : module.getSettings()) {
                if (setting instanceof KeybindSetting ks) {
                    String key = module.getId() + ":" + setting.getId();
                    nbt.putInt(key, ks.getValue());
                }
            }
        }
        return nbt;
    }

    @SuppressWarnings("unchecked")
    private void loadModuleSettings(MvxmenuConfig config) {
        NbtCompound nbt = config.getModuleSettings();
        if (nbt == null) return;
        for (Module module : ModuleRegistry.get().getAll()) {
            for (Setting<?> setting : module.getSettings()) {
                String key = module.getId() + ":" + setting.getId();
                if (!nbt.contains(key)) continue;
                if (setting instanceof BooleanSetting bs && nbt.contains(key, net.minecraft.nbt.NbtElement.NUMBER_TYPE)) {
                    bs.setValue(nbt.getBoolean(key));
                } else if (setting instanceof IntegerSetting is && nbt.contains(key, net.minecraft.nbt.NbtElement.NUMBER_TYPE)) {
                    is.setValue(nbt.getInt(key));
                } else if (setting instanceof DoubleSetting ds && nbt.contains(key, net.minecraft.nbt.NbtElement.NUMBER_TYPE)) {
                    ds.setValue(nbt.getDouble(key));
                } else if (setting instanceof StringSetting ss && nbt.contains(key, net.minecraft.nbt.NbtElement.STRING_TYPE)) {
                    ss.setValue(nbt.getString(key));
                } else if (setting instanceof ColorSetting cs && nbt.contains(key, net.minecraft.nbt.NbtElement.NUMBER_TYPE)) {
                    cs.setValue(nbt.getInt(key));
                } else if (setting instanceof EnumSetting es && nbt.contains(key, NbtElement.STRING_TYPE)) {
                    try {
                        Class<Enum> enumClass = (Class<Enum>) es.getEnumClass();
                        Enum value = Enum.valueOf(enumClass, nbt.getString(key));
                        es.setValue(value);
                    } catch (IllegalArgumentException ignored) {}
                }
            }
        }
    }

    private NbtCompound saveModuleSettings() {
        NbtCompound nbt = new NbtCompound();
        for (Module module : ModuleRegistry.get().getAll()) {
            for (Setting<?> setting : module.getSettings()) {
                String key = module.getId() + ":" + setting.getId();
                if (setting instanceof BooleanSetting bs) {
                    nbt.putBoolean(key, bs.getValue());
                } else if (setting instanceof IntegerSetting is) {
                    nbt.putInt(key, is.getValue());
                } else if (setting instanceof DoubleSetting ds) {
                    nbt.putDouble(key, ds.getValue());
                } else if (setting instanceof StringSetting ss) {
                    nbt.putString(key, ss.getValue());
                } else if (setting instanceof ColorSetting cs) {
                    nbt.putInt(key, cs.getValue());
                } else if (setting instanceof EnumSetting<?> es) {
                    nbt.putString(key, es.getValue().name());
                }
            }
        }
        return nbt;
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

    public void updateModuleState(String moduleId, boolean enabled) {
        moduleStates.put(moduleId, enabled);
    }
}