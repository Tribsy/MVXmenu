package dev.mvxmenu.ui.screen;

import dev.mvxmenu.config.MvxmenuConfig;
import dev.mvxmenu.config.ConfigService;
import dev.mvxmenu.module.Module;
import dev.mvxmenu.networking.MvxmenuNetworking;
import dev.mvxmenu.ui.ViewType;
import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.ui.navigation.ViewManager;
import dev.mvxmenu.ui.views.BaseView;
import dev.mvxmenu.ui.views.GenericView;
import dev.mvxmenu.ui.views.ModuleDetailView;
import dev.mvxmenu.ui.views.SettingsView;
import dev.mvxmenu.ui.widget.ButtonWidget;
import dev.mvxmenu.ui.widget.CategoryButtonWidget;
import dev.mvxmenu.ui.widget.DropdownWidget;
import dev.mvxmenu.ui.widget.HeaderWidget;
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
    private final ConfigService configService = new ConfigService();
    private final ViewManager viewManager = new ViewManager();
    private HeaderWidget headerWidget;

private Module.Category activeCategory;
    private ModuleCardWidget selectedModule;
    private boolean settingsActive;
    private int selectedCategoryIndex;
    private String searchQuery = "";
    private TextFieldWidget searchField;
    private Module.Category[] categories;

    // View caching - avoids GC pressure from creating views every frame
    private GenericView cachedGenericView;
    private ModuleDetailView cachedDetailView;
    private ModuleCardWidget cachedDetailModule;
    private String cachedSearchQuery;
    private Module.Category cachedCategory;

    // Dragging State
    private boolean isDragging = false;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;

    public MvxmenuScreen() {
        this.layout = new MvxmenuLayout();
        this.widgets = new ArrayList<>();
        this.categoryModules = new LinkedHashMap<>();
        this.categoryButtons = new LinkedHashMap<>();
        this.settingsView = new SettingsView(layout);
        this.activeCategory = Module.Category.COMBAT;
        this.settingsActive = false;
        this.selectedCategoryIndex = 0;
        this.categories = Module.Category.values();
        this.headerWidget = new HeaderWidget();
        initCategories();
    }

    public void setConfig(MvxmenuConfig config) {
        this.config = config;
        settingsView.init(config);
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
        
        headerWidget.setBounds(new MvxmenuLayout.Bounds(layout.headerX(), layout.headerY(), layout.headerWidth(), layout.headerHeight()));
        widgets.add(headerWidget);
        
        layoutSidebarButtons();
    }

    private void layoutSidebarButtons() {
        int x = layout.sidebarX() + MvxmenuTheme.SP_2;
        int y = layout.headerHeight() + MvxmenuTheme.SP_2;
        int buttonWidth = layout.sidebarWidth() - (MvxmenuTheme.SP_2 * 2);
        int buttonHeight = MvxmenuTheme.SP_8;
        int gap = MvxmenuTheme.SP_1;

        searchField = new TextFieldWidget("search", "SCAN MODULES... (enabled: | disabled:)", 32);
        searchField.setBounds(new MvxmenuLayout.Bounds(x, y, buttonWidth, 28));
        widgets.add(searchField);
        y += MvxmenuTheme.SP_8 + MvxmenuTheme.SP_1;

        for (Module.Category cat : categories) {
            String catName = cat.getDisplayName().toUpperCase();
            CategoryButtonWidget btn = categoryButtons.get(catName);
            btn.setBounds(new MvxmenuLayout.Bounds(x, y, buttonWidth, buttonHeight));
            widgets.add(btn);
            y += buttonHeight + gap;
        }

        int settingsY = layout.headerHeight() + MvxmenuTheme.SP_2 + 36 + (categories.length * (buttonHeight + gap)) + MvxmenuTheme.SP_4;
        settingsButton = new CategoryButtonWidget("settings", "SYSTEM", MvxmenuIcons.CLOCK);
        settingsButton.setBounds(new MvxmenuLayout.Bounds(x, settingsY, buttonWidth, buttonHeight));
        widgets.add(settingsButton);
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        renderSidebar(context, mouseX, mouseY);
        renderContent(context, mouseX, mouseY, delta);
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
        int x = layout.sidebarX() + MvxmenuTheme.SP_2;
        int y = layout.headerHeight() + MvxmenuTheme.SP_2;
        int buttonWidth = layout.sidebarWidth() - (MvxmenuTheme.SP_2 * 2);
        int buttonHeight = MvxmenuTheme.SP_8;
        int gap = MvxmenuTheme.SP_1;

        if (searchField != null) {
            searchField.render(context, mouseX, mouseY, 0.0f);
        }

        y += MvxmenuTheme.SP_8 + MvxmenuTheme.SP_1;

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

private void renderContent(DrawContext context, int mouseX, int mouseY, float delta) {
        ViewType active = viewManager.getActiveView();
        BaseView currentView = null;

        if (active == ViewType.SETTINGS) {
            currentView = settingsView;
        } else if (active == ViewType.DETAIL && viewManager.getSelectedModule() != null) {
            ModuleCardWidget selectedMod = viewManager.getSelectedModule();
            if (cachedDetailView == null || cachedDetailModule != selectedMod) {
                cachedDetailModule = selectedMod;
                cachedDetailView = (ModuleDetailView) selectedMod.getModule().createDetailView(layout);
                cachedDetailView.init();
            }
            currentView = cachedDetailView;
        } else {
            // Check if we need to recreate the generic view (category or search changed)
            String currentSearchQuery = searchQuery;
            Module.Category currentCategory = activeCategory;
            
            if (cachedGenericView == null || !currentSearchQuery.equals(cachedSearchQuery) || currentCategory != cachedCategory) {
                cachedSearchQuery = currentSearchQuery;
                cachedCategory = currentCategory;
                cachedGenericView = new GenericView(layout, getModulesForActiveCategory());
                cachedGenericView.init();
            }
            currentView = cachedGenericView;
        }

        if (currentView != null) {
            currentView.render(context, mouseX, mouseY, delta);
        }
    }

    private List<ModuleCardWidget> getModulesForActiveCategory() {
        String catName = activeCategory.getDisplayName().toUpperCase();
        List<ModuleCardWidget> modules = categoryModules.get(catName);
        if (modules == null) return new ArrayList<>();
        
        String query = searchQuery.toLowerCase();
        boolean showEnabled = query.startsWith("enabled:");
        boolean showDisabled = query.startsWith("disabled:");
        final String searchTerm = (showEnabled || showDisabled) ? query.substring(query.indexOf(":") + 1).trim() : query;
        
        return modules.stream()
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
        if (viewManager.getActiveView() == ViewType.SETTINGS) {
            for (MvxmenuWidget w : settingsView.getWidgets()) {
                w.render(context, mouseX, mouseY, delta);
            }
        }
    }

    public boolean mouseClicked(double x, double y, int button) {
        // Check for dragging start on HeaderWidget
        if (headerWidget != null && headerWidget.isHovered((int)x, (int)y)) {
            isDragging = true;
            dragOffsetX = (int)x - layout.getHudX();
            dragOffsetY = (int)y - layout.getHudY();
            return true;
        }

        boolean searchFieldClicked = false;
        if (searchField != null && searchField.mouseClicked(x, y, button)) {
            searchFieldClicked = true;
            searchField.setFocused(true);
        }

        for (int i = widgets.size() - 1; i >= 0; i--) {
            MvxmenuWidget w = widgets.get(i);
            if (w == searchField || w == headerWidget) continue;
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
        
        if (viewManager.getActiveView() == ViewType.SETTINGS) {
            for (MvxmenuWidget w : settingsView.getWidgets()) {
                if (w.mouseClicked(x, y, button)) {
                    if (w instanceof ButtonWidget bw) {
                        handleSettingsButtonClick(bw);
                    }
                    return true;
                }
            }
        }
        
if (viewManager.getActiveView() == ViewType.DETAIL && viewManager.getSelectedModule() != null) {
            ModuleCardWidget selectedMod = viewManager.getSelectedModule();
            BaseView detailView;
            if (cachedDetailView == null || cachedDetailModule != selectedMod) {
                cachedDetailModule = selectedMod;
                cachedDetailView = (ModuleDetailView) selectedMod.getModule().createDetailView(layout);
                cachedDetailView.init();
            }
            detailView = cachedDetailView;
            for (MvxmenuWidget w : detailView.getWidgets()) {
                if (w.mouseClicked(x, y, button)) {
                    if (detailView instanceof ModuleDetailView mdv) {
                        mdv.applySettings();
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public void mouseReleased(double x, double y, int button) {
        isDragging = false;
    }

    public void mouseMoved(double x, double y) {
        if (isDragging) {
            int newX = (int)x - dragOffsetX;
            int newY = (int)y - dragOffsetY;
            
            // Boundary Clamping
            int maxWidth = layout.screenWidth() - layout.sidebarWidth() - layout.contentWidth() - (layout.padding() * 2); // This is actually the total width
            // Let's use the real calculated total width
            int totalWidth = layout.sidebarWidth() + layout.contentWidth() + (layout.padding() * 2);
            int totalHeight = layout.screenHeight; // a bit simplified

            newX = Math.max(0, Math.min(newX, layout.screenWidth() - totalWidth));
            newY = Math.max(0, Math.min(newY, layout.screenHeight() - layout.footerHeight())); // simple clamp
            
            layout.setHudX(newX);
            layout.setHudY(newY);
        }
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
        viewManager.setView(ViewType.GENERIC);
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
            MvxmenuNetworking.sendModuleToggleToServer(module.getId().toString(), nextState);
        }
        viewManager.setView(ViewType.DETAIL, card);
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
        if (config != null) {
            configService.exportConfig(config);
        }
    }

    private void importConfig() {
        if (config == null) return;
        MvxmenuConfig imported = configService.importConfig();
        if (imported != null) {
            this.config = imported;
            settingsView.init(config);
        }
    }

    private void resetConfig() {
        if (config != null) {
            configService.resetConfig(config);
            settingsView.init(config);
        }
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (searchField != null && searchField.isFocused()) {
            if (searchField.keyPressed(keyCode, scanCode, modifiers)) {
                searchQuery = searchField.getText();
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
            viewManager.setView(ViewType.GENERIC);
            return true;
        }
        if (viewManager.getActiveView() == ViewType.GENERIC) {
            if (keyCode == 262) {
                selectedCategoryIndex = Math.min(categories.length - 1, selectedCategoryIndex + 1);
                activeCategory = categories[selectedCategoryIndex];
                updateCategoryButtons();
                return true;
            }
            if (keyCode == 263) {
                selectedCategoryIndex = Math.max(0, selectedCategoryIndex - 1);
                activeCategory = categories[selectedCategoryIndex];
                updateCategoryButtons();
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
                return true;
            }
        }
        return false;
    }

    public void openSettings() {
        settingsView.applyToConfig();
        settingsActive = true;
        viewManager.setView(ViewType.SETTINGS);
    }

    public void closeSettings() {
        settingsActive = false;
        viewManager.setView(ViewType.GENERIC);
    }

    public void goBack() {
        if (settingsActive) {
            closeSettings();
        } else {
            viewManager.goBack();
        }
    }

    public void refreshTheme() {
        if (config != null) {
            settingsView.init(config);
        }
    }

    public Module.Category getActiveCategory() {
        return activeCategory;
    }

    public void setActiveCategory(Module.Category category) {
        this.activeCategory = category;
    }

    public ViewType getActiveView() {
        return viewManager.getActiveView();
    }

    public ModuleCardWidget getSelectedModule() {
        return viewManager.getSelectedModule();
    }

    public void setSelectedModule(ModuleCardWidget module) {
        viewManager.setSelectedModule(module);
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
