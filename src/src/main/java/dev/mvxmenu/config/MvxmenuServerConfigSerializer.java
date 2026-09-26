package dev.mvxmenu.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.annotations.Expose;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MvxmenuServerConfigSerializer {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .setExclusionStrategies(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    return f.getAnnotation(Expose.class) == null;
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            })
            .create();

    private final Path configDir;

    public MvxmenuServerConfigSerializer(Path configDir) {
        this.configDir = configDir;
    }

    public MvxmenuServerConfig load() {
        Path configFile = configDir.resolve("mvxmenu-server.json");
        if (!Files.exists(configFile)) {
            MvxmenuServerConfig defaultConfig = createDefaults();
            save(defaultConfig);
            return defaultConfig;
        }
        try (FileReader reader = new FileReader(configFile.toFile())) {
            MvxmenuServerConfig config = GSON.fromJson(reader, MvxmenuServerConfig.class);
            if (config.getVersion() == null) {
                config.setVersion("1.0.0");
            }
            return config;
        } catch (JsonSyntaxException e) {
            throw new RuntimeException("Failed to parse server config file: " + configFile, e);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read server config file: " + configFile, e);
        }
    }

    public void save(MvxmenuServerConfig config) {
        Path configFile = configDir.resolve("mvxmenu-server.json");
        try {
            Files.createDirectories(configDir);
            try (FileWriter writer = new FileWriter(configFile.toFile())) {
                GSON.toJson(config, writer);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write server config file: " + configFile, e);
        }
    }

    public Path getConfigPath() {
        return configDir.resolve("mvxmenu-server.json");
    }

    public String serialize(MvxmenuServerConfig config) {
        return GSON.toJson(config);
    }

    public MvxmenuServerConfig deserialize(String json) {
        return GSON.fromJson(json, MvxmenuServerConfig.class);
    }

    private MvxmenuServerConfig createDefaults() {
        MvxmenuServerConfig config = new MvxmenuServerConfig();
        config.getWhitelist().add("sprint");
        config.getWhitelist().add("auto_eat");
        config.getWhitelist().add("auto_tool");
        config.getWhitelist().add("no_fall");
        config.getBlacklist().add("kill_aura");
        config.getBlacklist().add("flight");
        config.getBlacklist().add("timer");
        config.getBlacklist().add("freecam");
        config.getBlacklist().add("xray");
        config.getRestrictedModules().add("esp");
        config.getRestrictedModules().add("scaffold");
        return config;
    }
}