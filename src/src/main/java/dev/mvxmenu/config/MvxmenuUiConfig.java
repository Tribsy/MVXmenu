package dev.mvxmenu.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MvxmenuUiConfig {

    public static class LayoutConfig {
        public int screenWidth = 1200;
        public int screenHeight = 800;
        public int sidebarWidth = 180;
        public int headerHeight = 48;
        public int footerHeight = 32;
        public int padding = 16;
        public int slotSize = 72;
        public int slotSpacing = 4;
    }

    public static class ThemeConfig {
        public String accent = "DEFAULT";
        public String customAccent = "#4ADE80";
        public int bgOpacity = 90;
        public int panelRounding = 4;
        public int animationSpeed = 100;
        public boolean highContrast = false;
        public boolean blurEffects = true;
        public boolean scanlineOverlay = false;
    }

    public static class CategoryConfig {
        public String id;
        public String label;
        public String icon;
    }

    public static class ComponentConfig {
        public int radius;
        public int height;
        public int width;
        public int trackHeight;
        public int thumbSize;
        public int minWidth;
        public int size;
    }

    public static class TypographyConfig {
        public String font = "JetBrains Mono";
        public Map<String, Integer> sizes = new HashMap<>();
    }

    private int version = 2;
    private LayoutConfig layout = new LayoutConfig();
    private ThemeConfig theme = new ThemeConfig();
    private CategoryConfig[] categories;
    private Map<String, ComponentConfig> components = new HashMap<>();
    private TypographyConfig typography = new TypographyConfig();

    private static MvxmenuUiConfig instance;
    private static final Gson GSON = new GsonBuilder().create();

    private MvxmenuUiConfig() {}

    public static MvxmenuUiConfig get() {
        if (instance == null) {
            instance = loadFromResources();
        }
        return instance;
    }

    public static MvxmenuUiConfig loadFromResources() {
        try (InputStream is = MvxmenuUiConfig.class.getClassLoader().getResourceAsStream("mvxmenu-ui-config.json")) {
            if (is == null) {
                throw new IllegalStateException("mvxmenu-ui-config.json not found in resources");
            }
            return GSON.fromJson(new InputStreamReader(is, StandardCharsets.UTF_8), MvxmenuUiConfig.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load UI config", e);
        }
    }

    public void reload() {
        instance = loadFromResources();
    }

    public LayoutConfig getLayout() {
        return layout;
    }

    public ThemeConfig getTheme() {
        return theme;
    }

    public CategoryConfig[] getCategories() {
        return categories;
    }

    public ComponentConfig getComponent(String name) {
        return components.get(name);
    }

    public TypographyConfig getTypography() {
        return typography;
    }

    public int getVersion() {
        return version;
    }

    public int getSidebarWidth() {
        return layout.sidebarWidth;
    }

    public int getHeaderHeight() {
        return layout.headerHeight;
    }

    public int getFooterHeight() {
        return layout.footerHeight;
    }

    public int getPadding() {
        return layout.padding;
    }

    public int getScreenWidth() {
        return layout.screenWidth;
    }

    public int getScreenHeight() {
        return layout.screenHeight;
    }

    public int getSlotSize() {
        return layout.slotSize;
    }

    public int getSlotSpacing() {
        return layout.slotSpacing;
    }

    public int getAnimationSpeed() {
        return theme.animationSpeed;
    }

    public int getPanelRounding() {
        return theme.panelRounding;
    }

    public int getBgOpacity() {
        return theme.bgOpacity;
    }

    public boolean isHighContrast() {
        return theme.highContrast;
    }

    public boolean isBlurEffects() {
        return theme.blurEffects;
    }

    public boolean isScanlineOverlay() {
        return theme.scanlineOverlay;
    }

    public String getAccent() {
        return theme.accent;
    }

    public String getCustomAccent() {
        return theme.customAccent;
    }
}