package dev.mvxmenu.ui.screen;

import dev.mvxmenu.config.MvxmenuConfig;
import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.ui.widget.*;
import dev.mvxmenu.theme.FontRenderer;
import dev.mvxmenu.theme.MvxmenuTheme;
import dev.mvxmenu.theme.RoundedRectRenderer;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Settings panel widget - replaces SettingsView + renderSettingsView().
 * Section tabs: Appearance | Performance | System
 * Real-time preview of theme changes.
 */
public class SettingsPanelWidget implements MvxmenuWidget {

    public enum Section {
        APPEARANCE,
        PERFORMANCE,
        SYSTEM
    }

    private final String id = "settings_panel";
    private MvxmenuLayout.Bounds bounds;
    private final MvxmenuLayout layout;
    private MvxmenuConfig config;
    private Section activeSection = Section.APPEARANCE;
    private final List<MvxmenuWidget> appearanceWidgets = new ArrayList<>();
    private final List<MvxmenuWidget> performanceWidgets = new ArrayList<>();
    private final List<MvxmenuWidget> systemWidgets = new ArrayList<>();
    private final List<ButtonWidget> tabButtons = new ArrayList<>();
    private boolean visible = false;
    private float tabHoverProgress = 0f;

    public SettingsPanelWidget(MvxmenuLayout layout) {
        this.layout = layout;
    }

    public void init(MvxmenuConfig config) {
        this.config = config;
        buildWidgets();
    }

    private void buildWidgets() {
        appearanceWidgets.clear();
        performanceWidgets.clear();
        systemWidgets.clear();
        tabButtons.clear();

        if (config == null) return;

        // Tab buttons
        String[] tabs = {"APPEARANCE", "PERFORMANCE", "SYSTEM"};
        for (int i = 0; i < tabs.length; i++) {
            final int tabIndex = i;
            ButtonWidget tab = new ButtonWidget("settings_tab_" + tabs[i], ButtonWidget.Variant.GHOST, tabs[i]);
            tab.setActive(i == 0);
            tabButtons.add(tab);
        }

        // Appearance Section
        DropdownWidget guiScale = new DropdownWidget("settings_gui_scale", "GUI SCALE",
                new String[]{"AUTO (0)", "1x", "2x", "3x", "4x"},
                config.getGuiScale() == 0 ? "AUTO (0)" : config.getGuiScale() + "x");
        appearanceWidgets.add(guiScale);

        DropdownWidget themeAccent = new DropdownWidget("settings_theme_accent", "THEME ACCENT",
                new String[]{"DEFAULT (PURPLE)", "BLUE", "RED", "GREEN", "ORANGE", "YELLOW", "CYAN", "CUSTOM"},
                config.getThemeAccent());
        appearanceWidgets.add(themeAccent);

        ToggleWidget useCustomAccent = new ToggleWidget("settings_use_custom_accent", config.isUseCustomAccent());
        appearanceWidgets.add(useCustomAccent);

        DropdownWidget customAccent = new DropdownWidget("settings_custom_accent", "CUSTOM ACCENT",
                new String[]{"PURPLE", "BLUE", "RED", "GREEN", "ORANGE", "YELLOW", "CYAN", "PINK"},
                formatColor(config.getCustomAccent()));
        appearanceWidgets.add(customAccent);

        ToggleWidget blurEffects = new ToggleWidget("settings_blur_effects", config.isBlurEffects());
        appearanceWidgets.add(blurEffects);

        ToggleWidget scanlineOverlay = new ToggleWidget("settings_scanline_overlay", config.isScanlineOverlay());
        appearanceWidgets.add(scanlineOverlay);

        ToggleWidget highContrast = new ToggleWidget("settings_high_contrast", config.isHighContrast());
        appearanceWidgets.add(highContrast);

        SliderWidget bgOpacity = new SliderWidget("settings_bg_opacity", "BACKGROUND OPACITY", 0, 100, config.getBgOpacity());
        appearanceWidgets.add(bgOpacity);

        SliderWidget animationSpeed = new SliderWidget("settings_animation_speed", "ANIMATION SPEED", 50, 200, config.getAnimationSpeed());
        appearanceWidgets.add(animationSpeed);

        SliderWidget panelRounding = new SliderWidget("settings_panel_rounding", "PANEL ROUNDING", 0, 8, config.getPanelRounding());
        appearanceWidgets.add(panelRounding);

        // Performance Section
        SliderWidget tickRate = new SliderWidget("settings_tick_rate", "TICK RATE LIMIT", 20, 200, config.getTickRateLimit());
        performanceWidgets.add(tickRate);

        DropdownWidget renderBackend = new DropdownWidget("settings_render_backend", "RENDER BACKEND",
                new String[]{"AUTO-DETECT", "VULKAN", "OPENGL", "SOFTWARE"},
                config.getRenderBackend());
        performanceWidgets.add(renderBackend);

        // System Section
        ToggleWidget telemetry = new ToggleWidget("settings_telemetry", config.isTelemetry());
        systemWidgets.add(telemetry);

        ButtonWidget exportBtn = new ButtonWidget("settings_export", ButtonWidget.Variant.DEFAULT, "EXPORT CONFIGURATION");
        systemWidgets.add(exportBtn);

        ButtonWidget importBtn = new ButtonWidget("settings_import", ButtonWidget.Variant.DEFAULT, "IMPORT");
        systemWidgets.add(importBtn);

        ButtonWidget resetBtn = new ButtonWidget("settings_reset", ButtonWidget.Variant.DANGER, "FACTORY RESET");
        systemWidgets.add(resetBtn);
    }

    private String formatColor(int color) {
        String hex = String.format("#%06X", color & 0xFFFFFF).toUpperCase();
        return switch (hex) {
            case "#8B5CF6" -> "PURPLE";
            case "#60A5FA" -> "BLUE";
            case "#F87171" -> "RED";
            case "#4ADE80" -> "GREEN";
            case "#FB923C" -> "ORANGE";
            case "#FCD34D" -> "YELLOW";
            case "#22D3EE" -> "CYAN";
            case "#F472B6" -> "PINK";
            default -> "PURPLE";
        };
    }

    private int parseColor(String formatted) {
        return switch (formatted) {
            case "PURPLE" -> 0xFF8B5CF6;
            case "BLUE" -> 0xFF60A5FA;
            case "RED" -> 0xFFF87171;
            case "GREEN" -> 0xFF4ADE80;
            case "ORANGE" -> 0xFFFB923C;
            case "YELLOW" -> 0xFFFCD34D;
            case "CYAN" -> 0xFF22D3EE;
            case "PINK" -> 0xFFF472B6;
            default -> 0xFF8B5CF6;
        };
    }

    public void setActiveSection(Section section) {
        this.activeSection = section;
        for (int i = 0; i < tabButtons.size(); i++) {
            tabButtons.get(i).setActive(i == section.ordinal());
        }
    }

    public Section getActiveSection() {
        return activeSection;
    }

    public void applyToConfig() {
        if (config == null) return;

        List<MvxmenuWidget> allWidgets = new ArrayList<>();
        allWidgets.addAll(appearanceWidgets);
        allWidgets.addAll(performanceWidgets);
        allWidgets.addAll(systemWidgets);

        for (MvxmenuWidget w : allWidgets) {
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

    @Override
    public String getId() {
        return id;
    }

    @Override
    public MvxmenuLayout.Bounds getBounds() {
        return bounds;
    }

    @Override
    public void setBounds(MvxmenuLayout.Bounds bounds) {
        this.bounds = bounds;
        layoutTabs();
    }

    private void layoutTabs() {
        if (bounds == null) return;

        int tabWidth = bounds.width / 3;
        int tabHeight = 32;
        int tabY = bounds.y + 4;

        for (int i = 0; i < tabButtons.size(); i++) {
            ButtonWidget tab = tabButtons.get(i);
            tab.setBounds(new MvxmenuLayout.Bounds(
                    bounds.x + i * tabWidth, tabY, tabWidth, tabHeight));
        }
    }

    @Override
    public boolean isEnabled() {
        return visible;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.visible = enabled;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public boolean isHovered(int mouseX, int mouseY) {
        return bounds != null && bounds.contains(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if (!visible || bounds == null || !bounds.contains((int)x, (int)y)) return false;

        // Check tab clicks
        for (int i = 0; i < tabButtons.size(); i++) {
            ButtonWidget tab = tabButtons.get(i);
            if (tab.mouseClicked(x, y, button)) {
                setActiveSection(Section.values()[i]);
                return true;
            }
        }

        // Check section widgets
        List<MvxmenuWidget> currentWidgets = getCurrentWidgets();
        for (MvxmenuWidget w : currentWidgets) {
            if (w.mouseClicked(x, y, button)) {
                if (w instanceof ButtonWidget bw) {
                    handleButtonClick(bw);
                }
                return true;
            }
        }
        return false;
    }

    private void handleButtonClick(ButtonWidget btn) {
        switch (btn.getId()) {
            case "settings_export" -> {
                // Handled by screen
            }
            case "settings_import" -> {
                // Handled by screen
            }
            case "settings_reset" -> {
                // Handled by screen
            }
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!visible) return false;
        if (keyCode == 1 || keyCode == 256) { // ESC
            return false; // Let screen handle close
        }
        for (MvxmenuWidget w : getCurrentWidgets()) {
            if (w.keyPressed(keyCode, scanCode, modifiers)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        for (MvxmenuWidget w : getCurrentWidgets()) {
            if (w.charTyped(codePoint, modifiers)) {
                return true;
            }
        }
        return false;
    }

    private List<MvxmenuWidget> getCurrentWidgets() {
        return switch (activeSection) {
            case APPEARANCE -> appearanceWidgets;
            case PERFORMANCE -> performanceWidgets;
            case SYSTEM -> systemWidgets;
        };
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (!visible || bounds == null) return;

        // Panel background
        RoundedRectRenderer.render(context, bounds.x, bounds.y, bounds.width, bounds.height,
                MvxmenuTheme.R_PANEL, MvxmenuTheme.BG_0);

        // Top accent bar
        context.fill(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + 2, MvxmenuTheme.AC);

        // Tab bar background
        int tabBarHeight = 36;
        RoundedRectRenderer.render(context, bounds.x, bounds.y + 2, bounds.width, tabBarHeight,
                MvxmenuTheme.R_PANEL, MvxmenuTheme.BG_1);

        // Render tabs
        for (ButtonWidget tab : tabButtons) {
            tab.render(context, mouseX, mouseY, delta);
        }

        // Section content
        int contentX = bounds.x + MvxmenuTheme.SP_4;
        int contentY = bounds.y + tabBarHeight + MvxmenuTheme.SP_4;
        int contentWidth = bounds.width - MvxmenuTheme.SP_4 * 2;

        // Section title
        String sectionTitle = activeSection.name();
        FontRenderer.drawTextSimple(context, sectionTitle, contentX, contentY, MvxmenuTheme.TX_0, true, "ui");
        contentY += 24;

        // Render section widgets
        int y = contentY;
        for (MvxmenuWidget w : getCurrentWidgets()) {
            if (w instanceof DropdownWidget) {
                w.setBounds(new MvxmenuLayout.Bounds(contentX, y, contentWidth, 28));
            } else if (w instanceof ToggleWidget) {
                w.setBounds(new MvxmenuLayout.Bounds(contentX, y, 40, 24));
            } else if (w instanceof SliderWidget) {
                w.setBounds(new MvxmenuLayout.Bounds(contentX, y, contentWidth, 28));
            } else if (w instanceof ButtonWidget) {
                w.setBounds(new MvxmenuLayout.Bounds(contentX, y, 160, 28));
            }
            w.render(context, mouseX, mouseY, delta);
            y += 40;
        }
    }

    @Override
    public String getTooltipText(int mouseX, int mouseY) {
        return "Settings Panel - " + activeSection.name();
    }

    @Override
    public Type getType() {
        return Type.SCREEN;
    }

    @Override
    public boolean isFocused() {
        return false;
    }

    @Override
    public void setFocused(boolean focused) {}
}