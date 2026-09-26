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

public class MvxmenuConfigSerializer {

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

    public MvxmenuConfigSerializer(Path configDir) {
        this.configDir = configDir;
    }

    public MvxmenuConfig load() {
        Path configFile = configDir.resolve("mvxmenu.json");
        if (!Files.exists(configFile)) {
            return createDefaults();
        }
        try (FileReader reader = new FileReader(configFile.toFile())) {
            MvxmenuConfig config = GSON.fromJson(reader, MvxmenuConfig.class);
            if (config.getVersion() == null) {
                config.setVersion("1.0.0");
            }
            return config;
        } catch (JsonSyntaxException e) {
            throw new RuntimeException("Failed to parse config file: " + configFile, e);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read config file: " + configFile, e);
        }
    }

    public void save(MvxmenuConfig config) {
        Path configFile = configDir.resolve("mvxmenu.json");
        try {
            Files.createDirectories(configDir);
            try (FileWriter writer = new FileWriter(configFile.toFile())) {
                GSON.toJson(config, writer);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write config file: " + configFile, e);
        }
    }

    public Path getConfigPath() {
        return configDir.resolve("mvxmenu.json");
    }

    public String serialize(MvxmenuConfig config) {
        return GSON.toJson(config);
    }

    public MvxmenuConfig deserialize(String json) {
        return GSON.fromJson(json, MvxmenuConfig.class);
    }

    private MvxmenuConfig createDefaults() {
        return new MvxmenuConfig();
    }
}