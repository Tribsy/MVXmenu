package dev.mvxmenu.ui.screen;

import dev.mvxmenu.module.Module;
import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.ui.widget.ModuleCardWidget;
import dev.mvxmenu.ui.widget.MvxmenuWidget;
import dev.mvxmenu.theme.MvxmenuTheme;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Module grid widget - responsive 1-4 column grid of module cards.
 * Handles search/filter logic and layout.
 */
public class ModuleGridWidget implements MvxmenuWidget {

    private final String id = "module_grid";
    private MvxmenuLayout.Bounds bounds;
    private final MvxmenuLayout layout;
    private final Map<String, List<ModuleCardWidget>> categoryModules;
    private Module.Category activeCategory;
    private String searchQuery = "";
    private List<ModuleCardWidget> visibleCards = new ArrayList<>();
    private boolean visible = true;

    public ModuleGridWidget(MvxmenuLayout layout, Map<String, List<ModuleCardWidget>> categoryModules) {
        this.layout = layout;
        this.categoryModules = categoryModules;
    }

    public void setActiveCategory(Module.Category category) {
        this.activeCategory = category;
        relayout();
    }

    public void setSearchQuery(String query) {
        this.searchQuery = query;
        relayout();
    }

    private void relayout() {
        visibleCards.clear();
        if (activeCategory == null || bounds == null) return;

        String catName = activeCategory.getDisplayName().toUpperCase();
        List<ModuleCardWidget> modules = categoryModules.get(catName);
        if (modules == null) return;

        String query = searchQuery.toLowerCase();
        boolean showEnabled = query.startsWith("enabled:");
        boolean showDisabled = query.startsWith("disabled:");
        String searchTerm = (showEnabled || showDisabled)
                ? query.substring(query.indexOf(":") + 1).trim()
                : query;

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

        int columns = layout.moduleGridColumns();
        int cardWidth = layout.moduleCardWidth();
        int cardHeight = layout.moduleCardHeight();
        int gapX = layout.moduleCardGap();
        int gapY = layout.moduleCardGap();

        int startX = bounds.x;
        int startY = bounds.y;

        int col = 0;
        int row = 0;
        for (ModuleCardWidget card : filtered) {
            int cx = startX + col * (cardWidth + gapX);
            int cy = startY + row * (cardHeight + gapY);
            card.setBounds(new MvxmenuLayout.Bounds(cx, cy, cardWidth, cardHeight));
            visibleCards.add(card);
            col++;
            if (col >= columns) {
                col = 0;
                row++;
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
        relayout();
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

        for (int i = visibleCards.size() - 1; i >= 0; i--) {
            ModuleCardWidget card = visibleCards.get(i);
            if (card.mouseClicked(x, y, button)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (bounds == null || !visible) return;

        for (ModuleCardWidget card : visibleCards) {
            if (card.isVisible()) {
                card.render(context, mouseX, mouseY, delta);
            }
        }
    }

    public List<ModuleCardWidget> getVisibleCards() {
        return visibleCards;
    }

    @Override
    public String getTooltipText(int mouseX, int mouseY) {
        return "Module Grid";
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