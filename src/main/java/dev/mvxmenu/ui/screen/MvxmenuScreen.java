package dev.mvxmenu.ui.screen;

import dev.mvxmenu.config.MvxmenuConfig;
import dev.mvxmenu.config.MvxmenuConfigSerializer;
import dev.mvxmenu.module.Module;
import dev.mvxmenu.networking.MvxmenuNetworking;
import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.ui.view.GenericView;
import dev.mvxmenu.ui.view.ModuleDetailView;
import dev.mvxmenu.ui.view.SettingsView;
import dev.mvxmenu.ui.widget.ButtonWidget;
import dev.mvxmenu.ui.widget.CategoryButtonWidget;
import dev.mvxmenu.ui.widget.DropdownWidget;
import dev.mvxmenu.ui.widget.KeybindWidget;
import dev.mvxmenu.ui.widget.MvxmenuWidget;
import dev.mvxmenu.ui.widget.ModuleCardWidget;
import dev.mvxmenu.ui.widget.SliderWidget;
import dev.mvxmenu.ui.widget.TextFieldWidget;
import dev.mvxmenu.ui.widget.ToggleWidget;
import dev.mvxmenu.theme.MvxmenuIcons;
import dev.mvxmenu.theme.MvxmenuTheme;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

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

    private final MvxmenuLayout layout;
    private final List<MvxmenuWidget> widgets;
    private final Map<String, List<ModuleCardWidget>> categoryModules;
    private final Map<String, CategoryButtonWidget> categoryButtons;
    private final SettingsView settingsView;
    private CategoryButtonWidget settingsButton;
    private MvxmenuConfig config;

    private Module.Category activeCategory;
    private String activeView;
    private ModuleCardWidget selectedModule;
    private boolean settingsActive;
    private int selectedCategoryIndex;
    private String searchQuery = "";
    private TextFieldWidget searchField;
    private Module.Category[] categories;

    public MvxmenuScreen() {
        this.layout = new MvxmenuLayout();
        this.widgets = new ArrayList<>();
        this.categoryModules = new LinkedHashMap<>();
        this.categoryButtons = new LinkedHashMap<>();
        this.settingsView = new SettingsView();
        this.activeCategory = Module.Category.COMBAT;
        this.activeView = "generic";
        this.settingsActive = false;
        this.selectedCategoryIndex = 0;
        this.categories = Module.Category.values();
        initCategories();
    }

    public void setConfig(MvxmenuConfig config) {
        this.config = config;
        settingsView.init(config);
        layoutSettings();
    }

    private void initCategories() {
        for (int i = 0; i < categories.length; i++) {
            Module.Category cat = categories[i];
            CategoryButtonWidget btn = new CategoryButtonWidget("cat_" + i, cat.getDisplayName().toUpperCase(), cat.getIcon());
            categoryButtons.put(cat.getDisplayName().toUpperCase(), btn);
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
        widgets.clear();
        layout.calculate(this);
        layoutSidebarButtons();
        layoutCategoryContent();
        layoutSettings();
    }

    private void layoutSidebarButtons() {
        int x = layout.sidebarX() + 6;
        int y = layout.headerHeight() + 8;
        int buttonWidth = layout.sidebarWidth() - 12;
        int buttonHeight = 32;
        int gap = 4;

        searchField = new TextFieldWidget("search", "SCAN MODULES... (enabled: | disabled:)", 32);
        searchField.setBounds(new MvxmenuLayout.Bounds(x, y, buttonWidth, 28));
        widgets.add(searchField);
        y += 36;

        for (Module.Category cat : categories) {
            String catName = cat.getDisplayName().toUpperCase();
            CategoryButtonWidget btn = categoryButtons.get(catName);
            btn.setBounds(new MvxmenuLayout.Bounds(x, y, buttonWidth, buttonHeight));
            widgets.add(btn);
            y += buttonHeight + gap;
        }

        int settingsY = layout.headerHeight() + 8 + 36 + (categories.length * (buttonHeight + gap)) + 16;
        settingsButton = new CategoryButtonWidget("settings", "SYSTEM", MvxmenuIcons.CLOCK);
        settingsButton.setBounds(new MvxmenuLayout.Bounds(x, settingsY, buttonWidth, buttonHeight));
        widgets.add(settingsButton);
    }

    private void layoutCategoryContent() {
        widgets.removeIf(w -> w instanceof ModuleCardWidget);
        int contentX = layout.contentX() + layout.padding();
        int contentY = layout.contentY() + layout.padding();
        int cardWidth = 200;
        int cardHeight = 80;
        int gapX = 8;
        int gapY = 8;

        String query = searchQuery.toLowerCase();
        boolean showEnabled = query.startsWith("enabled:");
        boolean showDisabled = query.startsWith("disabled:");
        final String searchTerm = (showEnabled || showDisabled) ? query.substring(query.indexOf(":") + 1).trim() : query;
        for (Module.Category cat : categories) {
            String catName = cat.getDisplayName().toUpperCase();
            List<ModuleCardWidget> modules = categoryModules.get(catName);
            List<ModuleCardWidget> filtered = modules.stream()
                    .filter(m -> {
                        boolean matchesSearch = searchTerm.isEmpty() ||
                                m.getName().toLowerCase().contains(searchTerm) ||
                                m.getDescription().toLowerCase().contains(searchTerm);
                        boolean matchesState = !showEnabled && !showDisabled ||
                                (showEnabled && m.isEnabled()) ||
                                (showDisabled && !m.isEnabled());
                        return matchesSearch && matchesState;
                    })
                    .collect(Collectors.toList());
            int col = 0;
            int row = 0;
            for (ModuleCardWidget card : filtered) {
                int cx = contentX + col * (cardWidth + gapX);
                int cy = contentY + row * (cardHeight + gapY);
                card.setBounds(new MvxmenuLayout.Bounds(cx, cy, cardWidth, cardHeight));
                widgets.add(card);
                col++;
                if (col >= 3) {
                    col = 0;
                    row++;
                }
            }
        }
    }

    private void layoutSettings() {
        if (config == null) return;
        widgets.removeIf(w -> w instanceof DropdownWidget || w instanceof ToggleWidget || w instanceof SliderWidget || w instanceof ButtonWidget);
        int contentX = layout.contentX() + layout.padding();
        int contentY = layout.contentY() + layout.padding() + 32;
        int width = layout.contentWidth() - layout.padding() * 2;
        int y = contentY;

        for (MvxmenuWidget w : settingsView.getWidgets()) {
            if (w instanceof DropdownWidget) {
                w.setBounds(new MvxmenuLayout.Bounds(contentX, y, width, 28));
            } else if (w instanceof ToggleWidget) {
                w.setBounds(new MvxmenuLayout.Bounds(contentX, y, 40, 24));
            } else if (w instanceof SliderWidget) {
                w.setBounds(new MvxmenuLayout.Bounds(contentX, y, width, 28));
            } else if (w instanceof ButtonWidget) {
                w.setBounds(new MvxmenuLayout.Bounds(contentX, y, 160, 28));
            }
            y += 40;
        }
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        renderSidebar(context, mouseX, mouseY);
        renderContent(context, mouseX, mouseY);
        renderHeader(context);
        renderFooter(context);
        renderTooltip(context, mouseX, mouseY);
        renderWidgets(context, mouseX, mouseY, delta);
    }

    private void renderBackground(DrawContext context) {
        context.fill(layout.sidebarX(), layout.sidebarY(),
                layout.sidebarX() + layout.sidebarWidth(), layout.sidebarY() + layout.sidebarContentHeight(),
                MvxmenuTheme.BG_1);
        context.fill(layout.contentX(), layout.contentY(),
                layout.contentX() + layout.contentWidth(), layout.contentY() + layout.contentHeight(),
                MvxmenuTheme.BG_0);
    }

    private void renderSidebar(DrawContext context, int mouseX, int mouseY) {
        int x = layout.sidebarX() + 6;
        int y = layout.headerHeight() + 8;
        int buttonWidth = layout.sidebarWidth() - 12;
        int buttonHeight = 32;
        int gap = 4;

        if (searchField != null) {
            searchField.render(context, mouseX, mouseY, 0.0f);
        }

        y += 36;

        for (Module.Category cat : categories) {
            String catName = cat.getDisplayName().toUpperCase();
            CategoryButtonWidget btn = categoryButtons.get(catName);
            btn.render(context, mouseX, mouseY, 0.0f);
            if (btn.isHovered(mouseX, mouseY)) {
                context.fill(x + 1, y + 1, x + buttonWidth - 1, y + buttonHeight - 1, MvxmenuTheme.AC_DIM);
            }
            y += buttonHeight + gap;
        }

        settingsButton.render(context, mouseX, mouseY, 0.0f);
        if (settingsButton.isHovered(mouseX, mouseY)) {
            context.fill(x + 1, y + 1, x + buttonWidth - 1, y + buttonHeight - 1, MvxmenuTheme.AC_DIM);
        }
    }

    private void renderContent(DrawContext context, int mouseX, int mouseY) {
        if (settingsActive) {
            renderSettingsView(context, mouseX, mouseY);
        } else if (selectedModule != null) {
            renderModuleDetail(context, mouseX, mouseY);
        } else {
            renderGenericView(context, mouseX, mouseY);
        }
    }

    private void renderGenericView(DrawContext context, int mouseX, int mouseY) {
        int contentX = layout.contentX() + layout.padding();
        int contentY = layout.contentY() + layout.padding();
        int panelWidth = layout.contentWidth() - 16;

        context.fill(contentX, contentY, contentX + panelWidth, contentY + 84, MvxmenuTheme.BG_1);
        context.fill(contentX, contentY, contentX + panelWidth, contentY + 2, MvxmenuTheme.AC);
        context.fill(contentX + 12, contentY + 12, contentX + 12 + 8, contentY + 26, MvxmenuTheme.AC);
        context.fill(contentX + 12, contentY + 34, contentX + panelWidth - 12, contentY + 36, MvxmenuTheme.BD_1);
        context.fill(contentX + 12, contentY + 84, contentX + panelWidth - 12, contentY + 85, MvxmenuTheme.AC_DIM);

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        if (textRenderer != null) {
            context.drawText(textRenderer, "MVX // TACTICAL HUD", contentX + 24, contentY + 12, MvxmenuTheme.TX_0, true);
            context.drawText(textRenderer, "STATUS // LINK STABLE", contentX + 24, contentY + 30, MvxmenuTheme.AC, true);
            context.drawText(textRenderer, "Select a module, tune the behavior, and keep the stack clean.", contentX + 12, contentY + 48, MvxmenuTheme.TX_1, true);
            context.drawText(textRenderer, "GRID // ONLINE    LINK // SECURE    STACK // OPTIMAL", contentX + 12, contentY + 66, MvxmenuTheme.PURPLE, true);
        }
    }

    private void renderModuleDetail(DrawContext context, int mouseX, int mouseY) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        if (textRenderer == null) return;
        int contentX = layout.contentX() + layout.padding();
        int contentY = layout.contentY() + layout.padding();
        context.fill(contentX, contentY, contentX + layout.contentWidth(), contentY + layout.contentHeight(), MvxmenuTheme.BG_1);
        context.fill(contentX, contentY, contentX + layout.contentWidth(), contentY + 2, MvxmenuTheme.AC);

        ModuleDetailView detailView = getCurrentDetailView();
        if (detailView != null && detailView.getModule() != null) {
            Module module = detailView.getModule();
            context.fill(contentX + 8, contentY + 10, contentX + 24, contentY + 22, module.isEnabled() ? MvxmenuTheme.SUCCESS : MvxmenuTheme.DANGER);
            context.drawText(textRenderer, module.getName().toUpperCase(), contentX + 32, contentY + 10, MvxmenuTheme.TX_0, true);
            context.drawText(textRenderer, module.getDescription(), contentX + 8, contentY + 22, MvxmenuTheme.TX_1, true);
            context.drawText(textRenderer, "STATE // " + (module.isEnabled() ? "ACTIVE" : "IDLE"), contentX + 8, contentY + 34, module.isEnabled() ? MvxmenuTheme.SUCCESS : MvxmenuTheme.DANGER, true);

if (detailView.hasParameters()) {
                context.drawText(textRenderer, "TUNING", contentX + 8, contentY + 52, MvxmenuTheme.AC, true);
                int y = contentY + 70;
                for (MvxmenuWidget widget : detailView.getSettingWidgets()) {
                    if (widget instanceof ToggleWidget tw) {
                        context.drawText(textRenderer, tw.getId().replace("module_" + module.getId() + "_", "").replace("_", " ").toUpperCase(), contentX + 8, y, MvxmenuTheme.TX_1, true);
                        widget.setBounds(new MvxmenuLayout.Bounds(contentX + layout.contentWidth() - 60, y - 2, 40, 24));
                        widget.render(context, mouseX, mouseY, 0);
                    } else if (widget instanceof SliderWidget sw) {
                        context.drawText(textRenderer, sw.getLabel() + ": " + sw.getValue(), contentX + 8, y, MvxmenuTheme.TX_1, true);
                        widget.setBounds(new MvxmenuLayout.Bounds(contentX + 8, y + 16, layout.contentWidth() - 16, 28));
                        widget.render(context, mouseX, mouseY, 0);
                        y += 20;
                    } else if (widget instanceof DropdownWidget dw) {
                        context.drawText(textRenderer, dw.getLabel() + ": " + dw.getValue(), contentX + 8, y, MvxmenuTheme.TX_1, true);
                        widget.setBounds(new MvxmenuLayout.Bounds(contentX + 8, y + 16, layout.contentWidth() - 16, 28));
                        widget.render(context, mouseX, mouseY, 0);
                        y += 20;
                    } else if (widget instanceof KeybindWidget kw) {
                        context.drawText(textRenderer, kw.getLabel() + ": " + kw.getValue(), contentX + 8, y, MvxmenuTheme.TX_1, true);
                        widget.setBounds(new MvxmenuLayout.Bounds(contentX + 8, y + 16, layout.contentWidth() - 16, 28));
                        widget.render(context, mouseX, mouseY, 0);
                        y += 20;
                    }
                    y += 40;
                }
            }
        }
    }

    private void renderSettingsView(DrawContext context, int mouseX, int mouseY) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        if (textRenderer == null) return;
        int contentX = layout.contentX() + layout.padding();
        int contentY = layout.contentY() + layout.padding();
        context.fill(contentX, contentY, contentX + layout.contentWidth(), contentY + layout.contentHeight(), MvxmenuTheme.BG_2);
        context.fill(contentX, contentY, contentX + layout.contentWidth(), contentY + 24, MvxmenuTheme.BD_0);
        context.drawText(textRenderer, "SYSTEM // CONFIG", contentX + 8, contentY + 8, MvxmenuTheme.TX_0, true);

        int y = contentY + 32;
        for (MvxmenuWidget w : settingsView.getWidgets()) {
            if (w instanceof DropdownWidget dw) {
                context.drawText(textRenderer, dw.getLabel() + ": " + dw.getValue(), contentX + 8, y, MvxmenuTheme.TX_1, true);
            } else if (w instanceof ToggleWidget tw) {
                context.drawText(textRenderer, tw.getId().replace("settings_", "").replace("_", " ").toUpperCase() + ": " + (tw.getEnabled() ? "ON" : "OFF"), contentX + 50, y, MvxmenuTheme.TX_1, true);
            } else if (w instanceof SliderWidget sw) {
                context.drawText(textRenderer, sw.getLabel() + ": " + sw.getValue() + "%", contentX + 8, y, MvxmenuTheme.TX_1, true);
            } else if (w instanceof ButtonWidget bw) {
                // Button labels handled by widget render
            }
            y += 40;
        }
    }

    private void renderHeader(DrawContext context) {
        context.fill(layout.headerX(), layout.headerY(),
                layout.headerX() + layout.headerWidth(), layout.headerHeight(),
                MvxmenuTheme.BG_2);
        context.fill(layout.headerX(), layout.headerY() + layout.headerHeight() - 1,
                layout.headerX() + layout.headerWidth(), layout.headerHeight(),
                MvxmenuTheme.BD_1);
        context.fill(layout.headerX() + 12, layout.headerY() + 8, layout.headerX() + 18, layout.headerY() + 16, MvxmenuTheme.AC);

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        if (textRenderer != null) {
            int x = layout.headerX() + 24;
            int y = layout.headerY() + 6;
            context.drawText(textRenderer, "MVX // HUD", x, y, MvxmenuTheme.TX_0, true);
            context.drawText(textRenderer, "LIVE", x + 150, y, MvxmenuTheme.AC, true);
            context.drawText(textRenderer, "SYNCED", x + 190, y, MvxmenuTheme.PURPLE, true);
        }
    }

    private void renderFooter(DrawContext context) {
        context.fill(layout.footerX(), layout.footerY(),
                layout.footerX() + layout.footerWidth(), layout.footerY() + layout.footerHeight(),
                MvxmenuTheme.BG_2);
        context.fill(layout.footerX(), layout.footerY(),
                layout.footerX() + layout.footerWidth(), layout.footerY() + 1,
                MvxmenuTheme.BD_1);
    }

    private void renderTooltip(DrawContext context, int mouseX, int mouseY) {
        if (mouseX >= 0 && mouseY >= 0) {
            for (MvxmenuWidget w : widgets) {
                if (w.isHovered(mouseX, mouseY)) {
                    String tooltip = w.getTooltipText(mouseX, mouseY);
                    if (tooltip != null && !tooltip.isEmpty()) {
                        int tx = mouseX + 12;
                        int ty = mouseY - 12;
                        if (tx + 120 > layout.screenWidth()) tx = mouseX - 132;
                        if (ty < 4) ty = mouseY + 16;
                        context.fill(tx - 2, ty - 2, tx + 122, ty + 12, 0xDD000000);
                        break;
                    }
                }
            }
        }
    }

    private float delta;

    public void setDelta(float delta) {
        this.delta = delta;
    }

    private void renderWidgets(DrawContext context, int mouseX, int mouseY, float delta) {
        this.delta = delta;
        for (MvxmenuWidget w : widgets) {
            w.render(context, mouseX, mouseY, delta);
        }
        if (settingsActive) {
            for (MvxmenuWidget w : settingsView.getWidgets()) {
                w.render(context, mouseX, mouseY, delta);
            }
        }
    }

    public boolean mouseClicked(double x, double y, int button) {
        boolean searchFieldClicked = false;
        if (searchField != null && searchField.mouseClicked(x, y, button)) {
            searchFieldClicked = true;
            searchField.setFocused(true);
        }

        for (int i = widgets.size() - 1; i >= 0; i--) {
            MvxmenuWidget w = widgets.get(i);
            if (w == searchField) continue;
            if (w.mouseClicked(x, y, button)) {
                if (searchField != null) searchField.setFocused(false);
                if (w instanceof CategoryButtonWidget) {
                    handleCategoryClick((CategoryButtonWidget) w);
                } else if (w instanceof ModuleCardWidget) {
                    handleModuleClick((ModuleCardWidget) w);
                } else if (w instanceof ButtonWidget) {
                    handleSettingsButtonClick((ButtonWidget) w);
                }
                return true;
            }
        }
        if (!searchFieldClicked && searchField != null) {
            searchField.setFocused(false);
        }
        if (settingsActive) {
            for (MvxmenuWidget w : settingsView.getWidgets()) {
                if (w.mouseClicked(x, y, button)) {
                    if (w instanceof ButtonWidget bw) {
                        handleSettingsButtonClick(bw);
                    }
                    return true;
                }
            }
        }
        if (selectedModule != null && activeView.equals("detail")) {
            ModuleDetailView detailView = getCurrentDetailView();
            if (detailView != null) {
                for (MvxmenuWidget w : detailView.getSettingWidgets()) {
                    if (w.mouseClicked(x, y, button)) {
                        detailView.applySettings();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void handleCategoryClick(CategoryButtonWidget btn) {
        if (btn == settingsButton) {
            openSettings();
            return;
        }
        for (CategoryButtonWidget b : categoryButtons.values()) {
            b.setActive(false);
        }
        btn.setActive(true);
        for (Module.Category cat : categories) {
            if (cat.getDisplayName().toUpperCase().equals(btn.getCategoryName())) {
                activeCategory = cat;
                break;
            }
        }
        activeView = "generic";
        selectedModule = null;
        settingsActive = false;
        for (int i = 0; i < categories.length; i++) {
            if (categories[i].getDisplayName().toUpperCase().equals(btn.getCategoryName())) {
                selectedCategoryIndex = i;
                break;
            }
        }
    }

    private void handleModuleClick(ModuleCardWidget card) {
        Module module = card.getModule();
        if (module != null) {
            boolean nextState = !module.isEnabled();
            module.setEnabled(nextState);
            if (nextState) {
                module.onEnable();
            } else {
                module.onDisable();
            }
            card.setEnabled(nextState);
            MvxmenuNetworking.sendModuleToggleToServer(module.getId(), nextState);
        }
        selectedModule = card;
        activeView = "detail";
    }

    private void handleSettingsButtonClick(ButtonWidget btn) {
        switch (btn.getId()) {
            case "settings_export" -> {
                if (config != null) {
                    settingsView.applyToConfig();
                    refreshTheme();
                    exportConfig();
                }
            }
            case "settings_import" -> {
                if (config != null) {
                    importConfig();
                }
            }
            case "settings_reset" -> {
                if (config != null) {
                    resetConfig();
                }
            }
        }
    }

    private void exportConfig() {
        if (config == null) return;
        try {
            MvxmenuConfigSerializer serializer = new MvxmenuConfigSerializer(
                    FabricLoader.getInstance().getConfigDir()
            );
            serializer.save(config);
        } catch (Exception ignored) {}
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
                settingsView.init(config);
                layoutSettings();
            }
        } catch (Exception ignored) {}
    }

    private void resetConfig() {
        if (config == null) return;
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
        settingsView.init(config);
        layoutSettings();
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (searchField != null && searchField.isFocused()) {
            if (searchField.keyPressed(keyCode, scanCode, modifiers)) {
                searchQuery = searchField.getText();
                layoutCategoryContent();
                return true;
            }
            if (keyCode == 256) {
                searchField.setFocused(false);
                return true;
            }
            return true;
        }

        if (keyCode == 1) {
            goBack();
            return true;
        }
        if (keyCode == 257) {
            if (selectedModule != null) {
                selectedModule = null;
                activeView = "generic";
                return true;
            }
        }
        if (!settingsActive && selectedModule == null) {
            if (keyCode == 262) {
                selectedCategoryIndex = Math.min(categories.length - 1, selectedCategoryIndex + 1);
                activeCategory = categories[selectedCategoryIndex];
                updateCategoryButtons();
                activeView = "generic";
                return true;
            }
            if (keyCode == 263) {
                selectedCategoryIndex = Math.max(0, selectedCategoryIndex - 1);
                activeCategory = categories[selectedCategoryIndex];
                updateCategoryButtons();
                activeView = "generic";
                return true;
            }
        }
        return false;
    }

    private void updateCategoryButtons() {
        String activeName = activeCategory.getDisplayName().toUpperCase();
        for (Map.Entry<String, CategoryButtonWidget> entry : categoryButtons.entrySet()) {
            entry.getValue().setActive(entry.getKey().equals(activeName));
        }
    }

    public boolean charTyped(char codePoint, int modifiers) {
        if (searchField != null && searchField.isFocused()) {
            if (searchField.charTyped(codePoint, modifiers)) {
                searchQuery = searchField.getText();
                layoutCategoryContent();
                return true;
            }
        }
        return false;
    }

    public void openSettings() {
        settingsView.applyToConfig();
        settingsActive = true;
        activeView = "settings";
        selectedModule = null;
    }

    public void closeSettings() {
        settingsActive = false;
        activeView = "generic";
    }

    public void goBack() {
        if (settingsActive) {
            closeSettings();
        } else if (selectedModule != null) {
            selectedModule = null;
            activeView = "generic";
        }
    }

    public void refreshTheme() {
        if (config != null) {
            settingsView.init(config);
            layoutSettings();
        }
    }

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
        return widgets;
    }

    public Map<String, List<ModuleCardWidget>> getCategoryModules() {
        return categoryModules;
    }

    public Map<String, CategoryButtonWidget> getCategoryButtons() {
        return categoryButtons;
    }

    public SettingsView getSettingsView() {
        return settingsView;
    }

    public GenericView getCurrentGenericView() {
        String catName = activeCategory.getDisplayName().toUpperCase();
        List<ModuleCardWidget> modules = categoryModules.get(catName);
        if (modules == null) return null;
        return new GenericView(modules);
    }

    public ModuleDetailView getCurrentDetailView() {
        if (selectedModule == null) return null;
        return new ModuleDetailView(selectedModule.getModule());
    }

    public List<String> getCategoryNames() {
        List<String> names = new ArrayList<>();
        for (Module.Category cat : categories) {
            names.add(cat.getDisplayName().toUpperCase());
        }
        return names;
    }

    public CategoryButtonWidget getSettingsButton() {
        return settingsButton;
    }
}
