package dev.mvxmenu.ui.screen;

import dev.mvxmenu.config.MvxmenuConfig;
import dev.mvxmenu.config.MvxmenuConfigSerializer;
import dev.mvxmenu.module.Module;
import dev.mvxmenu.networking.MvxmenuNetworking;
import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.ui.widget.*;
import dev.mvxmenu.theme.MvxmenuTheme;
import dev.mvxmenu.theme.RoundedRectRenderer;
import dev.mvxmenu.theme.FontRenderer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.font.TextRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MvxmenuScreen {

    private static final Logger LOGGER = LoggerFactory.getLogger(MvxmenuScreen.class);

    private final MvxmenuLayout layout;
    private final List<MvxmenuWidget> rootWidgets;
    private final Map<String, List<ModuleCardWidget>> categoryModules;
    private final Module.Category[] categories;

    // Shell widgets
    private HeaderWidget headerWidget;
    private SidebarWidget sidebarWidget;
    private ContentPanelWidget contentPanelWidget;
    private FooterWidget footerWidget;

    // Content widgets
    private ModuleGridWidget moduleGridWidget;
    private ModuleDetailPanelWidget moduleDetailWidget;
    private SettingsPanelWidget settingsPanelWidget;

    // State
    private MvxmenuConfig config;
    private Module.Category activeCategory;
    private String activeView = "generic"; // "generic", "detail", "settings"
    private ModuleCardWidget selectedModule;
    private boolean settingsActive = false;
    private int selectedCategoryIndex = 0;
    private String searchQuery = "";
    private float delta;

    public MvxmenuScreen() {
        this.layout = new MvxmenuLayout();
        this.rootWidgets = new ArrayList<>();
        this.categoryModules = new LinkedHashMap<>();
        this.categories = Module.Category.values();
        this.activeCategory = Module.Category.COMBAT;
        initCategories();
    }

    public void setConfig(MvxmenuConfig config) {
        this.config = config;
        if (settingsPanelWidget != null) {
            settingsPanelWidget.init(config);
        }
    }

    private void initCategories() {
        for (Module.Category cat : categories) {
            categoryModules.put(cat.getDisplayName().toUpperCase(), new ArrayList<>());
        }
    }

    public void addModule(String category, ModuleCardWidget module) {
        List<ModuleCardWidget> modules = categoryModules.get(category);
        if (modules != null) {
            modules.add(module);
        }
    }

    public void init() {
        rootWidgets.clear();

        // Create shell widgets
        headerWidget = new HeaderWidget(layout);
        sidebarWidget = new SidebarWidget(layout, categories, activeCategory);
        contentPanelWidget = new ContentPanelWidget(layout);
        footerWidget = new FooterWidget(layout);

        // Create content widgets
        moduleGridWidget = new ModuleGridWidget(layout, categoryModules);
        moduleDetailWidget = new ModuleDetailPanelWidget(layout);
        settingsPanelWidget = new SettingsPanelWidget(layout);
        if (config != null) {
            settingsPanelWidget.init(config);
        }

        // Add to root
        rootWidgets.add(headerWidget);
        rootWidgets.add(sidebarWidget);
        rootWidgets.add(contentPanelWidget);
        rootWidgets.add(footerWidget);

        // Initial layout
        recalculateLayout();
        showGenericView();
    }

    private void recalculateLayout() {
        int originX = layout.shellX();
        int originY = layout.shellY();
        int screenW = layout.screenWidth();
        int screenH = layout.screenHeight();
        int sidebarW = layout.sidebarWidth();
        int headerH = layout.headerHeight();
        int footerH = layout.footerHeight();
        int padding = layout.padding();

        headerWidget.setBounds(new MvxmenuLayout.Bounds(originX, originY, screenW, headerH));

        int sidebarH = screenH - headerH - footerH;
        sidebarWidget.setBounds(new MvxmenuLayout.Bounds(originX, originY + headerH, sidebarW, sidebarH));

        int contentX = originX + sidebarW + padding;
        int contentY = originY + headerH + padding;
        int contentW = screenW - sidebarW - padding * 2;
        int contentH = screenH - headerH - footerH - padding * 2;
        contentPanelWidget.setBounds(new MvxmenuLayout.Bounds(contentX, contentY, contentW, contentH));

        footerWidget.setBounds(new MvxmenuLayout.Bounds(originX, originY + screenH - footerH, screenW, footerH));
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.delta = delta;
        recalculateLayout();

        int viewportW = layout.viewportWidth();
        int viewportH = layout.viewportHeight();
        context.fill(0, 0, viewportW, viewportH, 0xCC060606);

        int originX = layout.shellX();
        int originY = layout.shellY();
        int screenW = layout.screenWidth();
        int screenH = layout.screenHeight();
        RoundedRectRenderer.render(context, originX, originY, screenW, screenH, MvxmenuTheme.R_WINDOW, MvxmenuTheme.BG_0);
        RoundedRectRenderer.renderBorder(context, originX, originY, screenW, screenH,
                MvxmenuTheme.R_WINDOW, 1, MvxmenuTheme.AC_BORDER, MvxmenuTheme.BG_0);
        context.fill(originX, originY, originX + screenW, originY + 2, MvxmenuTheme.AC);

        // Render root widgets (shell)
        for (MvxmenuWidget w : rootWidgets) {
            if (w.isVisible()) {
                w.render(context, mouseX, mouseY, delta);
            }
        }

        // Tooltip (rendered last)
        renderTooltip(context, mouseX, mouseY);
    }

    private void renderTooltip(DrawContext context, int mouseX, int mouseY) {
        if (mouseX < 0 || mouseY < 0) return;

        for (MvxmenuWidget w : rootWidgets) {
            if (w.isVisible() && w.isHovered(mouseX, mouseY)) {
                String tooltip = w.getTooltipText(mouseX, mouseY);
                if (tooltip != null && !tooltip.isEmpty()) {
                    renderTooltipAt(context, tooltip, mouseX + 12, mouseY - 12);
                    break;
                }
            }
        }

        // Also check content panel children
        if (contentPanelWidget != null) {
            for (MvxmenuWidget w : contentPanelWidget.getChildren()) {
                if (w.isVisible() && w.isHovered(mouseX, mouseY)) {
                    String tooltip = w.getTooltipText(mouseX, mouseY);
                    if (tooltip != null && !tooltip.isEmpty()) {
                        renderTooltipAt(context, tooltip, mouseX + 12, mouseY - 12);
                        break;
                    }
                }
            }
        }
    }

    private void renderTooltipAt(DrawContext context, String text, int x, int y) {
        TextRenderer tr = MinecraftClient.getInstance().textRenderer;
        if (tr == null) return;

        int padding = 6;
        int textWidth = tr.getWidth(text);
        int tw = textWidth + padding * 2;
        int th = 16;

        if (x + tw > layout.screenWidth()) x = x - tw - 24;
        if (y < 4) y = 4;

        context.fill(x - padding, y - padding, x + tw + padding, y + th + padding, 0xDD000000);
        FontRenderer.drawTextSimple(context, text, x, y + 2, MvxmenuTheme.TX_0, true, "ui");
    }

    public void setDelta(float delta) {
        this.delta = delta;
    }

    // View management
    private void showGenericView() {
        activeView = "generic";
        selectedModule = null;
        settingsActive = false;
        sidebarWidget.setSettingsActive(false);

        contentPanelWidget.clearChildren();
        moduleGridWidget.setActiveCategory(activeCategory);
        moduleGridWidget.setSearchQuery(searchQuery);
        contentPanelWidget.addChild(moduleGridWidget);
    }

    private void showModuleDetail(ModuleCardWidget card) {
        activeView = "detail";
        selectedModule = card;
        settingsActive = false;

        contentPanelWidget.clearChildren();
        moduleDetailWidget.openModule(card.getModule());
        contentPanelWidget.addChild(moduleDetailWidget);
    }

    private void showSettings() {
        activeView = "settings";
        selectedModule = null;
        settingsActive = true;
        sidebarWidget.setSettingsActive(true);

        if (config != null) {
            settingsPanelWidget.init(config);
        }
        contentPanelWidget.clearChildren();
        contentPanelWidget.addChild(settingsPanelWidget);
    }

    // Input handling
    public boolean mouseClicked(double x, double y, int button) {
        // Sidebar search field
        if (sidebarWidget != null && sidebarWidget.getSearchField() != null) {
            TextFieldWidget searchField = sidebarWidget.getSearchField();
            if (searchField.mouseClicked(x, y, button)) {
                searchField.setFocused(true);
                return true;
            }
        }

        // Root widgets (sidebar buttons, etc)
        for (int i = rootWidgets.size() - 1; i >= 0; i--) {
            MvxmenuWidget w = rootWidgets.get(i);
            if (w == sidebarWidget) continue; // Handled separately
            if (w.mouseClicked(x, y, button)) {
                handleRootWidgetClick(w);
                return true;
            }
        }

        // Sidebar category buttons
        if (sidebarWidget != null) {
            for (Module.Category cat : categories) {
                CategoryButtonWidget btn = sidebarWidget.getCategoryButton(cat.getDisplayName().toUpperCase());
                if (btn != null && btn.mouseClicked(x, y, button)) {
                    handleCategoryClick(cat);
                    return true;
                }
            }
            // Settings button
            if (sidebarWidget.getSettingsButton() != null && sidebarWidget.getSettingsButton().mouseClicked(x, y, button)) {
                showSettings();
                return true;
            }
        }

        // Search field blur
        if (sidebarWidget != null && sidebarWidget.getSearchField() != null) {
            sidebarWidget.getSearchField().setFocused(false);
        }

        // Content panel children
        if (contentPanelWidget != null) {
            for (MvxmenuWidget w : contentPanelWidget.getChildren()) {
                if (w.mouseClicked(x, y, button)) {
                    handleContentWidgetClick(w);
                    return true;
                }
            }
        }

        return false;
    }

    private void handleRootWidgetClick(MvxmenuWidget widget) {
        if (widget instanceof CategoryButtonWidget) {
            // Handled in sidebar
        } else if (widget instanceof ModuleCardWidget card) {
            handleModuleClick(card);
        } else if (widget instanceof ButtonWidget btn) {
            handleSettingsButtonClick(btn);
        }
    }

    private void handleCategoryClick(Module.Category cat) {
        activeCategory = cat;
        selectedCategoryIndex = 0;
        for (int i = 0; i < categories.length; i++) {
            if (categories[i] == cat) {
                selectedCategoryIndex = i;
                break;
            }
        }
        sidebarWidget.setActiveCategory(cat);
        showGenericView();
    }

    private void handleModuleClick(ModuleCardWidget card) {
        if (card.consumeToggleClick()) {
            Module module = card.getModule();
            if (module != null) {
                boolean nextState = module.isEnabled();
                if (nextState) {
                    module.onEnable();
                } else {
                    module.onDisable();
                }
                MvxmenuNetworking.sendModuleToggleToServer(module.getId().toString(), nextState);
            }
            return;
        }
        showModuleDetail(card);
    }

    private void handleSettingsButtonClick(ButtonWidget btn) {
        if (config == null) return;
        switch (btn.getId()) {
            case "settings_export" -> {
                settingsPanelWidget.applyToConfig();
                exportConfig();
            }
            case "settings_import" -> {
                importConfig();
            }
            case "settings_reset" -> {
                resetConfig();
            }
        }
    }

    private void handleContentWidgetClick(MvxmenuWidget widget) {
        if (widget instanceof ModuleCardWidget card) {
            handleModuleClick(card);
        } else if (widget instanceof ButtonWidget btn) {
            handleSettingsButtonClick(btn);
        } else if (activeView.equals("detail") && moduleDetailWidget != null) {
            moduleDetailWidget.applySettings();
        }
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // Search field focus
        if (sidebarWidget != null && sidebarWidget.getSearchField() != null) {
            TextFieldWidget searchField = sidebarWidget.getSearchField();
            if (searchField.isFocused()) {
                if (searchField.keyPressed(keyCode, scanCode, modifiers)) {
                    searchQuery = searchField.getText();
                    moduleGridWidget.setSearchQuery(searchQuery);
                    return true;
                }
                if (keyCode == 256) { // ESC
                    searchField.setFocused(false);
                    return true;
                }
                return true;
            }
        }

        if (keyCode == 1 || keyCode == 256) {
            if (settingsActive || selectedModule != null) {
                goBack();
                return true;
            }
            return false;
        }

        // Enter on selected module
        if (keyCode == 257) {
            if (selectedModule != null) {
                selectedModule = null;
                activeView = "generic";
                showGenericView();
                return true;
            }
        }

        // Navigation when in generic view
        if (!settingsActive && selectedModule == null) {
            if (keyCode == 262) { // Right arrow - next category
                selectedCategoryIndex = Math.min(categories.length - 1, selectedCategoryIndex + 1);
                activeCategory = categories[selectedCategoryIndex];
                sidebarWidget.setActiveCategory(activeCategory);
                showGenericView();
                return true;
            }
            if (keyCode == 263) { // Left arrow - prev category
                selectedCategoryIndex = Math.max(0, selectedCategoryIndex - 1);
                activeCategory = categories[selectedCategoryIndex];
                sidebarWidget.setActiveCategory(activeCategory);
                showGenericView();
                return true;
            }
        }

        // Delegate to content widgets
        if (contentPanelWidget != null) {
            for (MvxmenuWidget w : contentPanelWidget.getChildren()) {
                if (w.keyPressed(keyCode, scanCode, modifiers)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean charTyped(char codePoint, int modifiers) {
        if (sidebarWidget != null && sidebarWidget.getSearchField() != null) {
            TextFieldWidget searchField = sidebarWidget.getSearchField();
            if (searchField.isFocused()) {
                if (searchField.charTyped(codePoint, modifiers)) {
                    searchQuery = searchField.getText();
                    moduleGridWidget.setSearchQuery(searchQuery);
                    return true;
                }
            }
        }

        if (contentPanelWidget != null) {
            for (MvxmenuWidget w : contentPanelWidget.getChildren()) {
                if (w.charTyped(codePoint, modifiers)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void goBack() {
        if (settingsActive) {
            settingsActive = false;
            activeView = "generic";
            sidebarWidget.setSettingsActive(false);
            showGenericView();
        } else if (selectedModule != null) {
            selectedModule = null;
            activeView = "generic";
            showGenericView();
        }
    }

    public void openSettings() {
        if (config != null) {
            settingsPanelWidget.applyToConfig();
            showSettings();
        }
    }

    public void closeSettings() {
        settingsActive = false;
        activeView = "generic";
        sidebarWidget.setSettingsActive(false);
        showGenericView();
    }

    public void refreshTheme() {
        if (config != null) {
            settingsPanelWidget.init(config);
        }
    }

    private void exportConfig() {
        if (config == null) return;
        try {
            MvxmenuConfigSerializer serializer = new MvxmenuConfigSerializer(
                    FabricLoader.getInstance().getConfigDir()
            );
            serializer.save(config);
        } catch (Exception e) {
            LOGGER.error("Failed to export config", e);
        }
    }

    private void importConfig() {
        if (config == null) return;
        File configFile = new File(
                FabricLoader.getInstance().getConfigDir().toFile(),
                "mvxmenu.json"
        );
        if (!configFile.exists()) return;
        try (FileReader reader = new FileReader(configFile)) {
            GsonBuilder gsonBuilder = new GsonBuilder()
                    .setPrettyPrinting()
                    .excludeFieldsWithoutExposeAnnotation();
            Gson gson = gsonBuilder.create();
            MvxmenuConfig imported = gson.fromJson(reader, MvxmenuConfig.class);
            if (imported != null) {
                this.config = imported;
                settingsPanelWidget.init(config);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to import config", e);
        }
    }

private void resetConfig() {
        if (config == null) return;
        config.setGuiScale(0);
        config.setThemeAccent("DEFAULT (PURPLE)");
        config.setBlurEffects(true);
        config.setScanlineOverlay(false);
        config.setTickRateLimit(100);
        config.setRenderBackend("AUTO-DETECT");
        config.setTelemetry(true);
        config.setHighContrast(false);
        config.setBgOpacity(90);
        config.setCustomAccent(0xFF8B5CF6);
        config.setUseCustomAccent(false);
        config.setAnimationSpeed(100);
        config.setPanelRounding(12);
        settingsPanelWidget.init(config);
    }

    // Getters
    public Module.Category getActiveCategory() {
        return activeCategory;
    }

    public void setActiveCategory(Module.Category category) {
        this.activeCategory = category;
    }

    public String getActiveView() {
        return activeView;
    }

    public ModuleCardWidget getSelectedModule() {
        return selectedModule;
    }

    public void setSelectedModule(ModuleCardWidget module) {
        this.selectedModule = module;
    }

    public boolean isSettingsActive() {
        return settingsActive;
    }

    public MvxmenuLayout getLayout() {
        return layout;
    }

    public List<MvxmenuWidget> getWidgets() {
        return rootWidgets;
    }

    public Map<String, List<ModuleCardWidget>> getCategoryModules() {
        return categoryModules;
    }

    public SettingsPanelWidget getSettingsPanelWidget() {
        return settingsPanelWidget;
    }

    public ModuleDetailPanelWidget getModuleDetailWidget() {
        return moduleDetailWidget;
    }

    public List<String> getCategoryNames() {
        List<String> names = new ArrayList<>();
        for (Module.Category cat : categories) {
            names.add(cat.getDisplayName().toUpperCase());
        }
        return names;
    }

    public SidebarWidget getSidebarWidget() {
        return sidebarWidget;
    }
}