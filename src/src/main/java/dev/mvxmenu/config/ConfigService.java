package dev.mvxmenu.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import java.io.*;

public class ConfigService {
    private final MvxmenuConfigSerializer serializer;

    public ConfigService() {
        this.serializer = new MvxmenuConfigSerializer(FabricLoader.getInstance().getConfigDir());
    }

    public void exportConfig(MvxmenuConfig config) {
        try {
            serializer.save(config);
        } catch (Exception ignored) {}
    }

    public MvxmenuConfig importConfig() {
        File configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "mvxmenu.json");
        if (!configFile.exists()) return null;
        try (FileReader reader = new FileReader(configFile)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            return gson.fromJson(reader, MvxmenuConfig.class);
        } catch (Exception ignored) {
            return null;
        }
    }

    public void resetConfig(MvxmenuConfig config) {
        config.setGuiScale(0);
        config.setThemeAccent("DEFAULT (GREEN)");
        config.setBlurEffects(true);
        config.setScanlineOverlay(false);
        config.setTickRateLimit(100);
        config.setRenderBackend("AUTO-DETECT");
        config.setTelemetry(true);
        config.setHighContrast(false);
        config.setBgOpacity(90);
        config.setCustomAccent(0xFF4ADE80);
        config.setUseCustomAccent(false);
        config.setAnimationSpeed(100);
        config.setPanelRounding(4);
    }
}
