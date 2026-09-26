package dev.mvxmenu.ui.views;

import dev.mvxmenu.config.MvxmenuConfig;
import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.ui.widget.*;
import dev.mvxmenu.theme.MvxmenuTheme;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

public class SettingsView extends BaseView {

    public enum Section {
        APPEARANCE,
        PERFORMANCE,
        SYSTEM
    }

    public static final String GUI_SCALE_LABEL = "GUI SCALE";
    public static final String THEME_ACCENT_LABEL = "THEME ACCENT";
    public static final String BLUR_EFFECTS_LABEL = "BLUR EFFECTS";
    public static final String SCANLINE_OVERLAY_LABEL = "SCANLINE OVERLAY";
    public static final String TICK_RATE_LABEL = "TICK RATE LIMIT";
    public static final String RENDER_BACKEND_LABEL = "RENDER BACKEND";
    public static final String TELEMETRY_LABEL = "TELEMETRY";
    public static final String HIGH_CONTRAST_LABEL = "HIGH CONTRAST MODE";

    public static final String BG_OPACITY_LABEL = "BACKGROUND OPACITY";
    public static final String USE_CUSTOM_ACCENT_LABEL = "USE CUSTOM ACCENT";
    public static final String CUSTOM_ACCENT_LABEL = "CUSTOM ACCENT COLOR";
    public static final String ANIMATION_SPEED_LABEL = "ANIMATION SPEED";
    public static final String PANEL_ROUNDING_LABEL = "PANEL ROUNDING";

    public static final String EXPORT_BUTTON = "EXPORT CONFIGURATION";
    public static final String IMPORT_BUTTON = "IMPORT";
    public static final String RESET_BUTTON = "FACTORY RESET";

    private Section activeSection = Section.APPEARANCE;
    private MvxmenuConfig config;

    public SettingsView(MvxmenuLayout layout) {
        super(layout);
    }

    public void init(MvxmenuConfig config) {
        this.config = config;
        clearWidgets();

        // Appearance Section
        DropdownWidget guiScale = new DropdownWidget("settings_gui_scale", GUI_SCALE_LABEL,
                new String[]{"AUTO (0)", "1x", "2x", "3x", "4x"},
                config.getGuiScale() == 0 ? "AUTO (0)" : config.getGuiScale() + "x");
        addWidget(guiScale);

        DropdownWidget themeAccent = new DropdownWidget("settings_theme_accent", THEME_ACCENT_LABEL,
                new String[]{"DEFAULT (GREEN)", "BLUE", "RED", "PURPLE", "ORANGE", "YELLOW", "CUSTOM"},
                config.getThemeAccent());
        addWidget(themeAccent);

        ToggleWidget useCustomAccent = new ToggleWidget("settings_use_custom_accent", config.isUseCustomAccent());
        addWidget(useCustomAccent);

        DropdownWidget customAccent = new DropdownWidget("settings_custom_accent", CUSTOM_ACCENT_LABEL,
                new String[]{"DEFAULT GREEN", "BLUE", "RED", "PURPLE", "ORANGE", "YELLOW", "CYAN", "PINK"},
                formatColor(config.getCustomAccent()));
        addWidget(customAccent);

        ToggleWidget blurEffects = new ToggleWidget("settings_blur_effects", config.isBlurEffects());
        addWidget(blurEffects);

        ToggleWidget scanlineOverlay = new ToggleWidget("settings_scanline_overlay", config.isScanlineOverlay());
        addWidget(scanlineOverlay);

        ToggleWidget highContrast = new ToggleWidget("settings_high_contrast", config.isHighContrast());
        addWidget(highContrast);

        SliderWidget bgOpacity = new SliderWidget("settings_bg_opacity", BG_OPACITY_LABEL, 0, 100, config.getBgOpacity());
        addWidget(bgOpacity);

        SliderWidget animationSpeed = new SliderWidget("settings_animation_speed", ANIMATION_SPEED_LABEL, 50, 200, config.getAnimationSpeed());
        addWidget(animationSpeed);

        SliderWidget panelRounding = new SliderWidget("settings_panel_rounding", PANEL_ROUNDING_LABEL, 0, 8, config.getPanelRounding());
        addWidget(panelRounding);

        // Performance Section
        SliderWidget tickRate = new SliderWidget("settings_tick_rate", TICK_RATE_LABEL, 20, 200, config.getTickRateLimit());
        addWidget(tickRate);

        DropdownWidget renderBackend = new DropdownWidget("settings_render_backend", RENDER_BACKEND_LABEL,
                new String[]{"AUTO-DETECT", "VULKAN", "OPENGL", "SOFTWARE"},
                config.getRenderBackend());
        addWidget(renderBackend);

        // System Section
        ToggleWidget telemetry = new ToggleWidget("settings_telemetry", config.isTelemetry());
        addWidget(telemetry);

        ButtonWidget exportBtn = new ButtonWidget("settings_export", ButtonWidget.Variant.DEFAULT, EXPORT_BUTTON);
        addWidget(exportBtn);

        ButtonWidget importBtn = new ButtonWidget("settings_import", ButtonWidget.Variant.DEFAULT, IMPORT_BUTTON);
        addWidget(importBtn);

        ButtonWidget resetBtn = new ButtonWidget("settings_reset", ButtonWidget.Variant.DANGER, RESET_BUTTON);
        addWidget(resetBtn);

        // Layout positions
        int contentX = layout.contentX() + layout.padding();
        int contentY = layout.contentY() + layout.padding() + 32;
        int width = layout.contentWidth() - layout.padding() * 2;
        int y = contentY;

        for (MvxmenuWidget w : widgets) {
            if (w instanceof DropdownWidget) {
                w.setBounds(new MvxmenuLayout.Bounds(contentX, y, width, 28));
            } else if (w instanceof ToggleWidget) {
                w.setBounds(new MvxmenuLayout.Bounds(contentX, y, 40, 24));
            } else if (w instanceof SliderWidget) {
                w.setBounds(new MvxmenuLayout.Bounds(contentX, y, width, 28));
            } else if (w instanceof ButtonWidget) {
                w.setBounds(new MvxmenuLayout.Bounds(contentX, y, 160, 28));
            }
            y += MvxmenuTheme.SP_8 * 2;
        }
    }

    private String formatColor(int color) {
        String hex = String.format("#%06X", color & 0xFFFFFF).toUpperCase();
        switch (hex) {
            case "#4ADE80": return "DEFAULT GREEN";
            case "#60A5FA": return "BLUE";
            case "#F87171": return "RED";
            case "#A78BFA": return "PURPLE";
            case "#FB923C": return "ORANGE";
            case "#FCD34D": return "YELLOW";
            case "#22D3EE": return "CYAN";
            case "#F472B6": return "PINK";
            default: return hex;
        }
    }

    private int parseColor(String formatted) {
        switch (formatted) {
            case "DEFAULT GREEN": return 0xFF4ADE80;
            case "BLUE": return 0xFF60A5FA;
            case "RED": return 0xFFF87171;
            case "PURPLE": return 0xFFA78BFA;
            case "ORANGE": return 0xFFFB923C;
            case "YELLOW": return 0xFFFCD34D;
            case "CYAN": return 0xFF22D3EE;
            case "PINK": return 0xFFF472B6;
            default: return 0xFF4ADE80;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        if (textRenderer == null) return;
        int contentX = layout.contentX() + layout.padding();
        int contentY = layout.contentY() + layout.padding();
        context.fill(contentX, contentY, contentX + layout.contentWidth(), contentY + layout.contentHeight(), MvxmenuTheme.BG_2);
        context.fill(contentX, contentY, contentX + layout.contentWidth(), contentY + 24, MvxmenuTheme.BD_0);
        context.drawText(textRenderer, "SYSTEM // CONFIG", contentX + 8, contentY + 8, MvxmenuTheme.TX_0, true);

        for (MvxmenuWidget w : widgets) {
            w.render(context, mouseX, mouseY, delta);
        }
    }

    public void applyToConfig() {
        if (config == null) return;
        for (MvxmenuWidget w : widgets) {
            switch (w.getId()) {
                case "settings_gui_scale" -> {
                    DropdownWidget dw = (DropdownWidget) w;
                    String val = dw.getValue();
                    config.setGuiScale("AUTO (0)".equals(val) ? 0 : Integer.parseInt(val.replace("x", "")));
                }
                case "settings_theme_accent" -> {
                    DropdownWidget dw = (DropdownWidget) w;
                    config.setThemeAccent(dw.getValue());
                }
                case "settings_use_custom_accent" -> {
                    ToggleWidget tw = (ToggleWidget) w;
                    config.setUseCustomAccent(tw.getEnabled());
                }
                case "settings_custom_accent" -> {
                    DropdownWidget dw = (DropdownWidget) w;
                    config.setCustomAccent(parseColor(dw.getValue()));
                }
                case "settings_blur_effects" -> {
                    ToggleWidget tw = (ToggleWidget) w;
                    config.setBlurEffects(tw.getEnabled());
                }
                case "settings_scanline_overlay" -> {
                    ToggleWidget tw = (ToggleWidget) w;
                    config.setScanlineOverlay(tw.getEnabled());
                }
                case "settings_high_contrast" -> {
                    ToggleWidget tw = (ToggleWidget) w;
                    config.setHighContrast(tw.getEnabled());
                }
                case "settings_bg_opacity" -> {
                    SliderWidget sw = (SliderWidget) w;
                    config.setBgOpacity(sw.getValue());
                }
                case "settings_animation_speed" -> {
                    SliderWidget sw = (SliderWidget) w;
                    config.setAnimationSpeed(sw.getValue());
                }
                case "settings_panel_rounding" -> {
                    SliderWidget sw = (SliderWidget) w;
                    config.setPanelRounding(sw.getValue());
                }
                case "settings_tick_rate" -> {
                    SliderWidget sw = (SliderWidget) w;
                    config.setTickRateLimit(sw.getValue());
                }
                case "settings_render_backend" -> {
                    DropdownWidget dw = (DropdownWidget) w;
                    config.setRenderBackend(dw.getValue());
                }
                case "settings_telemetry" -> {
                    ToggleWidget tw = (ToggleWidget) w;
                    config.setTelemetry(tw.getEnabled());
                }
            }
        }
    }
}
