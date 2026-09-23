package dev.mvxmenu.theme;

/**
 * Design tokens extracted from the Figma Make export.
 *
 * <p>Each constant maps to a CSS custom property from
 * {@code Minecraft In-Game Menu UI/src/index.css}.
 *
 * <p>Background colors: BG_0 (#060606) through BG_5 (#303030)
 * Border colors: BD_0 (#181818) through BD_3 (#444444)
 * Text colors: TX_0 (#EFEFEF) through TX_3 (#333333)
 * Accent colors: AC, SUCCESS, WARNING, DANGER, INFO, PURPLE, ORANGE
 * Radius: R_1 (2px) through R_4 (6px)
 * Spacing: SP_1 (4px) through SP_12 (48px)
 */
public final class MvxmenuTheme {

    // Backgrounds
    public static final int BG_0 = 0xFF060606;
    public static final int BG_1 = 0xFF0E0E0E;
    public static final int BG_2 = 0xFF161616;
    public static final int BG_3 = 0xFF1E1E1E;
    public static final int BG_4 = 0xFF272727;
    public static final int BG_5 = 0xFF303030;

    // Borders
    public static final int BD_0 = 0xFF181818;
    public static final int BD_1 = 0xFF242424;
    public static final int BD_2 = 0xFF333333;
    public static final int BD_3 = 0xFF444444;

    // Text
    public static final int TX_0 = 0xFFEFEFEF;
    public static final int TX_1 = 0xFFA0A0A0;
    public static final int TX_2 = 0xFF5C5C5C;
    public static final int TX_3 = 0xFF333333;

    // Accent / Semantic - Figma uses #4ADE80 for accent
    public static final int AC = 0xFF4ADE80;
    public static final int SUCCESS = 0xFF4ADE80;
    public static final int WARNING = 0xFFFCD34D;
    public static final int DANGER = 0xFFF87171;
    public static final int INFO = 0xFF60A5FA;
    public static final int PURPLE = 0xFFA78BFA;
    public static final int ORANGE = 0xFFFB923C;

    // Semantic backgrounds (10% opacity)
    public static final int SUCCESS_BG = 0x1A4ADE80;
    public static final int WARNING_BG = 0x1AFCD34D;
    public static final int DANGER_BG = 0x1AF87171;
    public static final int INFO_BG = 0x1A60A5FA;

    // Overlays
    public static final int AC_DIM = 0x124ADE80;
    public static final int AC_BORDER = 0x384ADE80;
    public static final int AC_FG = 0xFF052E16;

    // Radius (pixels) - Figma: R_1=2px, R_2=3px, R_3=4px, R_4=6px
    public static final int R_1 = 2;
    public static final int R_2 = 3;
    public static final int R_3 = 4;
    public static final int R_4 = 6;

    // Spacing (pixels) - Figma: SP_1=4, SP_2=8, SP_3=12, SP_4=16, SP_6=24, SP_8=32, SP_12=48
    public static final int SP_1 = 4;
    public static final int SP_2 = 8;
    public static final int SP_3 = 12;
    public static final int SP_4 = 16;
    public static final int SP_6 = 24;
    public static final int SP_8 = 32;
    public static final int SP_12 = 48;

    // Type sizes (Minecraft font pixels)
    public static final int TYPE_HERO = 14;
    public static final int TYPE_DISPLAY = 10;
    public static final int TYPE_HEADING = 8;
    public static final int TYPE_SUBHEADING = 6;
    public static final int TYPE_DEFAULT = 6;
    public static final int TYPE_BODY = 5;
    public static final int TYPE_LABEL = 5;
    public static final int TYPE_MICRO = 4;

    // Shadows (render as offset alpha rects)
    public static final int SHADOW_SM_OFFSET_Y = 1;
    public static final int SHADOW_SM_BLUR = 3;
    public static final int SHADOW_SM_ALPHA = 0x66000000;
    public static final int SHADOW_MD_OFFSET_Y = 4;
    public static final int SHADOW_MD_BLUR = 12;
    public static final int SHADOW_MD_ALPHA = 0x80000000;
    public static final int SHADOW_LG_OFFSET_Y = 8;
    public static final int SHADOW_LG_BLUR = 24;
    public static final int SHADOW_LG_ALPHA = 0xB3000000;
    public static final int SHADOW_PANEL_OFFSET_Y = 0;
    public static final int SHADOW_PANEL_BLUR = 0;
    public static final int SHADOW_PANEL_ALPHA = 0x05FFFFFF;

    // Motion durations (ms)
    public static final int MOTION_MICRO_DURATION = 80;
    public static final int MOTION_SNAP_DURATION = 120;
    public static final int MOTION_SLIDE_DURATION = 200;
    public static final int MOTION_FADE_DURATION = 200;

    // Accent glow (render effect — custom rendering via alpha glow rect)
    public static final int AC_GLOW_COLOR = 0x2E4ADE80;

    // High contrast mode (renders with enhanced borders)
    public static final int HC_BORDER = 0xFFFFFFFF;
    public static final int HC_TEXT = 0xFFFFFFFF;
    public static final int HC_BG = 0xFF000000;

    private MvxmenuTheme() {}
}
