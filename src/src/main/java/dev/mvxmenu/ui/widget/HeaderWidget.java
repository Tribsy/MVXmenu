package dev.mvxmenu.ui.widget;

import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.theme.MvxmenuTheme;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public class HeaderWidget implements MvxmenuWidget {
    private String id = "header";
    private MvxmenuLayout.Bounds bounds;
    private boolean enabled = true;
    private boolean visible = true;

    @Override
    public String getId() { return id; }
    @Override
    public MvxmenuLayout.Bounds getBounds() { return bounds; }
    @Override
    public void setBounds(MvxmenuLayout.Bounds bounds) { this.bounds = bounds; }
    @Override
    public boolean isEnabled() { return enabled; }
    @Override
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    @Override
    public boolean isVisible() { return visible; }
    @Override
    public void setVisible(boolean visible) { this.visible = visible; }
    @Override
    public boolean isHovered(int mouseX, int mouseY) { return false; }
    @Override
    public boolean mouseClicked(double x, double y, int button) { return false; }
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) { return false; }
    @Override
    public String getTooltipText(int mouseX, int mouseY) { return null; }
    @Override
    public Type getType() { return Type.LABEL; }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (bounds == null) return;
        
        context.fill(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height, MvxmenuTheme.BG_2);
        context.fill(bounds.x, bounds.y + bounds.height - 1, bounds.x + bounds.width, bounds.y + bounds.height, MvxmenuTheme.BD_1);
        context.fill(bounds.x + 12, bounds.y + 8, bounds.x + 18, bounds.y + 16, MvxmenuTheme.AC);

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        if (textRenderer != null) {
            int x = bounds.x + 24;
            int y = bounds.y + 6;
            context.drawText(textRenderer, "MVX // HUD", x, y, MvxmenuTheme.TX_0, true);
            context.drawText(textRenderer, "LIVE", x + 150, y, MvxmenuTheme.AC, true);
            context.drawText(textRenderer, "SYNCED", x + 190, y, MvxmenuTheme.PURPLE, true);
        }
    }
}
