package dev.mvxmenu.ui.screen;

import dev.mvxmenu.module.Module;
import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.ui.widget.CategoryButtonWidget;
import dev.mvxmenu.ui.widget.MvxmenuWidget;
import dev.mvxmenu.ui.widget.TextFieldWidget;
import dev.mvxmenu.theme.FontRenderer;
import dev.mvxmenu.theme.MvxmenuIcons;
import dev.mvxmenu.theme.MvxmenuTheme;
import dev.mvxmenu.theme.RoundedRectRenderer;
import net.minecraft.client.gui.DrawContext;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Sidebar widget - logo, search field, category navigation, system button.
 * Width: 260px (responsive), BG_1 background.
 */
public class SidebarWidget implements MvxmenuWidget {

    private final String id = "sidebar";
    private MvxmenuLayout.Bounds bounds;
    private final MvxmenuLayout layout;
    private final Map<String, CategoryButtonWidget> categoryButtons = new LinkedHashMap<>();
    private TextFieldWidget searchField;
    private CategoryButtonWidget settingsButton;
    private Module.Category[] categories;
    private Module.Category activeCategory;
    private boolean settingsActive;
    private float searchHoverProgress = 0f;
    private boolean visible = true;

    public SidebarWidget(MvxmenuLayout layout, Module.Category[] categories, Module.Category activeCategory) {
        this.layout = layout;
        this.categories = categories;
        this.activeCategory = activeCategory;
        initCategories();
    }

    private void initCategories() {
        for (Module.Category cat : categories) {
            String catName = cat.getDisplayName().toUpperCase();
            CategoryButtonWidget btn = new CategoryButtonWidget("cat_" + catName, catName, cat.getIcon());
            btn.setActive(cat == activeCategory);
            categoryButtons.put(catName, btn);
        }
    }

    private void layoutChildren() {
        if (bounds == null) return;

        int x = bounds.x + 8;
        int y = bounds.y + 8;
        int buttonWidth = bounds.width - 16;
        int buttonHeight = 32;
        int gap = 4;

        // Search field
        searchField = new TextFieldWidget("sidebar_search", "SCAN MODULES... (enabled: | disabled:)", 32);
        searchField.setBounds(new MvxmenuLayout.Bounds(x, y, buttonWidth, 28));
        y += 36;

        // Category buttons
        for (Module.Category cat : categories) {
            String catName = cat.getDisplayName().toUpperCase();
            CategoryButtonWidget btn = categoryButtons.get(catName);
            if (btn != null) {
                btn.setBounds(new MvxmenuLayout.Bounds(x, y, buttonWidth, buttonHeight));
                y += buttonHeight + gap;
            }
        }

        // Settings button (at bottom)
        int settingsY = bounds.y + bounds.height - buttonHeight - 8;
        settingsButton = new CategoryButtonWidget("sidebar_settings", "SYSTEM", MvxmenuIcons.CLOCK);
        settingsButton.setBounds(new MvxmenuLayout.Bounds(x, settingsY, buttonWidth, buttonHeight));
    }

    public void setActiveCategory(Module.Category category) {
        this.activeCategory = category;
        for (Map.Entry<String, CategoryButtonWidget> entry : categoryButtons.entrySet()) {
            entry.getValue().setActive(entry.getKey().equals(category.getDisplayName().toUpperCase()));
        }
    }

    public void setSettingsActive(boolean active) {
        this.settingsActive = active;
        if (settingsButton != null) {
            settingsButton.setActive(active);
        }
    }

    public TextFieldWidget getSearchField() {
        return searchField;
    }

    public CategoryButtonWidget getSettingsButton() {
        return settingsButton;
    }

    public CategoryButtonWidget getCategoryButton(String name) {
        return categoryButtons.get(name.toUpperCase());
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
        layoutChildren();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void setEnabled(boolean enabled) {}

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
        if (bounds == null || !bounds.contains((int)x, (int)y)) return false;

        if (searchField != null && searchField.mouseClicked(x, y, button)) {
            searchField.setFocused(true);
            return true;
        }

        for (CategoryButtonWidget btn : categoryButtons.values()) {
            if (btn.mouseClicked(x, y, button)) {
                if (searchField != null) searchField.setFocused(false);
                return true;
            }
        }

        if (settingsButton != null && settingsButton.mouseClicked(x, y, button)) {
            if (searchField != null) searchField.setFocused(false);
            return true;
        }

        if (searchField != null) searchField.setFocused(false);
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (searchField != null && searchField.isFocused()) {
            return searchField.keyPressed(keyCode, scanCode, modifiers);
        }
        return false;
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (searchField != null && searchField.isFocused()) {
            return searchField.charTyped(codePoint, modifiers);
        }
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (bounds == null || !visible) return;

        // Sidebar background with rounded corners (rx=12 for panel consistency)
        RoundedRectRenderer.render(context, bounds.x, bounds.y, bounds.width, bounds.height,
                MvxmenuTheme.R_PANEL, MvxmenuTheme.BG_1);

        // Search field
        if (searchField != null) {
            boolean searchHovered = searchField.isHovered(mouseX, mouseY);
            searchHoverProgress = lerp(searchHoverProgress, searchHovered ? 1f : 0f, delta * 10f);
            searchField.render(context, mouseX, mouseY, delta);
        }

        // Category buttons
        for (CategoryButtonWidget btn : categoryButtons.values()) {
            btn.render(context, mouseX, mouseY, delta);
        }

        // Settings button
        if (settingsButton != null) {
            settingsButton.render(context, mouseX, mouseY, delta);
        }

        // Logo at top (small)
        int logoX = bounds.x + (bounds.width - 24) / 2;
        int logoY = bounds.y + 8;
        MvxmenuIcons.SHIELD.render(context, logoX, logoY, 24, MvxmenuTheme.AC);
    }

    @Override
    public String getTooltipText(int mouseX, int mouseY) {
        return "Sidebar Navigation";
    }

    @Override
    public Type getType() {
        return Type.SCREEN;
    }

    @Override
    public boolean isFocused() {
        return searchField != null && searchField.isFocused();
    }

    @Override
    public void setFocused(boolean focused) {}

    private float lerp(float a, float b, float t) {
        return a + (b - a) * Math.min(1f, t);
    }
}