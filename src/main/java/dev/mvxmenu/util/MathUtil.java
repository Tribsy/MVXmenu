package dev.mvxmenu.util;

/**
 * Math utilities for animations and color interpolation.
 */
public final class MathUtil {

    private MathUtil() {}

    /**
     * Linear interpolation between two floats.
     */
    public static float lerp(float a, float b, float t) {
        return a + (b - a) * Math.min(1f, Math.max(0f, t));
    }

    /**
     * Linear interpolation between two ARGB colors.
     */
    public static int lerpColor(int from, int to, float t) {
        t = Math.min(1f, Math.max(0f, t));
        int r = (int) ((((from >> 16) & 0xFF) * (1 - t)) + (((to >> 16) & 0xFF) * t));
        int g = (int) ((((from >> 8) & 0xFF) * (1 - t)) + (((to >> 8) & 0xFF) * t));
        int b = (int) (((from & 0xFF) * (1 - t)) + ((to & 0xFF) * t));
        int a = (int) ((((from >> 24) & 0xFF) * (1 - t)) + (((to >> 24) & 0xFF) * t));
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}