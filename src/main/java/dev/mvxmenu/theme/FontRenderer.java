package dev.mvxmenu.theme;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

/**
 * Handles custom font loading from resource pack and provides rendering helpers.
 * Fonts registered via assets/mvxmenu/font/default.json
 */
public final class FontRenderer {

    private FontRenderer() {}

    /**
     * Called during client initialization to load custom fonts.
     * Fonts are automatically loaded by Minecraft from the resource pack.
     */
    public static void initialize() {
        // Fonts are auto-loaded from resource pack
        // This method exists for future extensibility
    }

    private static TextRenderer getTextRenderer() {
        MinecraftClient client = MinecraftClient.getInstance();
        return client != null ? client.textRenderer : null;
    }

    /**
     * Draws text using the default text renderer.
     * Custom font support requires more complex integration with Minecraft's font system.
     */
    public static void drawTextSimple(DrawContext context, String text, int x, int y,
                                       int color, boolean shadow, String fontId) {
        TextRenderer renderer = getTextRenderer();
        if (renderer != null) {
            context.drawText(renderer, text, x, y, color, shadow);
        }
    }

    public static boolean areFontsLoaded() {
        return getTextRenderer() != null;
    }
}