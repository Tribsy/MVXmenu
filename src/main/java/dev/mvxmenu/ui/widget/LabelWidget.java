package dev.mvxmenu.ui.widget;

import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.theme.MvxmenuTheme;
import dev.mvxmenu.theme.FontRenderer;
import net.minecraft.client.gui.DrawContext;

public class LabelWidget implements MvxmenuWidget {

    public enum Variant {
        HERO,       // TYPE_HERO (28px) - Brand only
        DISPLAY,    // TYPE_DISPLAY (20px) - Category headings
        HEADING,    // TYPE_HEADING (16px) - Panel titles
        SUBHEADING, // TYPE_SUBHEADING (13px) - Sub-sections
        DEFAULT,    // TYPE_DEFAULT (12px) - Body text, buttons
        BODY,       // TYPE_BODY (11px) - Descriptions
        LABEL,      // TYPE_LABEL (10px) - ALL CAPS, tracking
        MICRO       // TYPE_MICRO (9px) - Tooltips, metadata
    }

    private String id;
    private MvxmenuLayout.Bounds bounds;
    private String text;
    private Variant variant;
    private int color;
    private String fontId;
    private boolean visible = true;

    public LabelWidget(String id, String text, Variant variant) {
        this.id = id;
        this.text = text;
        this.variant = variant;
        this.color = MvxmenuTheme.TX_0;
        this.fontId = variant == Variant.BODY ? "body" : "ui";
    }

    public LabelWidget(String id, String text, int color) {
        this.id = id;
        this.text = text;
        this.variant = Variant.DEFAULT;
        this.color = color;
        this.fontId = "ui";
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
        return false;
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (bounds == null || !visible) return;

        int textSize = switch (variant) {
            case HERO -> MvxmenuTheme.TYPE_HERO;
            case DISPLAY -> MvxmenuTheme.TYPE_DISPLAY;
            case HEADING -> MvxmenuTheme.TYPE_HEADING;
            case SUBHEADING -> MvxmenuTheme.TYPE_SUBHEADING;
            case DEFAULT -> MvxmenuTheme.TYPE_DEFAULT;
            case BODY -> MvxmenuTheme.TYPE_BODY;
            case LABEL -> MvxmenuTheme.TYPE_LABEL;
            case MICRO -> MvxmenuTheme.TYPE_MICRO;
        };

        // Render text
        FontRenderer.drawTextSimple(context, text, bounds.x, bounds.y, color, true, fontId);
    }

    @Override
    public String getTooltipText(int mouseX, int mouseY) {
        return text;
    }

    @Override
    public Type getType() {
        return Type.LABEL;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Variant getVariant() {
        return variant;
    }

    public void setVariant(Variant variant) {
        this.variant = variant;
        this.fontId = variant == Variant.BODY ? "body" : "ui";
    }
}