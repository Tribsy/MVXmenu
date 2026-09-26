package dev.mvxmenu.ui.views;

import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.ui.widget.ModuleCardWidget;
import dev.mvxmenu.theme.MvxmenuTheme;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

public class GenericView extends BaseView {

    private final List<ModuleCardWidget> modules;

    public GenericView(MvxmenuLayout layout, List<ModuleCardWidget> modules) {
        super(layout);
        this.modules = modules;
    }

    @Override
    public void init() {
        clearWidgets();
        int contentX = layout.contentX() + layout.padding();
        int contentY = layout.contentY() + layout.padding();
        
        // Responsive Calculation: Calculate card width based on available space and column count
        int cols = layout.columnCount();
        int gapX = MvxmenuTheme.SP_2;
        int gapY = MvxmenuTheme.SP_2;
        
        // Calculate width to perfectly fill the content area
        int cardWidth = (layout.contentWidth() - (cols - 1) * gapX) / cols;
        int cardHeight = 80;

        int col = 0;
        int row = 0;
        for (ModuleCardWidget card : modules) {
            int cx = contentX + col * (cardWidth + gapX);
            int cy = contentY + row * (cardHeight + gapY);
            card.setBounds(new MvxmenuLayout.Bounds(cx, cy, cardWidth, cardHeight));
            addWidget(card);
            col++;
            if (col >= cols) {
                col = 0;
                row++;
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
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

        for (MvxmenuWidget w : widgets) {
            w.render(context, mouseX, mouseY, delta);
        }
    }

    public List<ModuleCardWidget> getModules() {
        return modules;
    }
}
