package dev.mvxmenu.theme;

/**
 * Design tokens generated from docs/brand/colors/palette.md
 * DO NOT EDIT MANUALLY — run scripts/generate_theme.py
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

    // Accent / Semantic
    public static final int AC = 0xFF8B5CF6;
    public static final int SUCCESS = 0xFF4ADE80;
    public static final int WARNING = 0xFFFCD34D;
    public static final int DANGER = 0xFFF87171;
    public static final int INFO = 0xFF60A5FA;
    public static final int PURPLE = 0xFFA78BFA;
    public static final int ORANGE = 0xFFFB923C;

    // Semantic Backgrounds (10% Opacity)
    public static final int SUCCESS_BG = 0x1A4ADE80;
    public static final int WARNING_BG = 0x1AFCD34D;
    public static final int DANGER_BG = 0x1AF87171;
    public static final int INFO_BG = 0x1A60A5FA;

    // Overlay / Accent Variants
    public static final int AC_DIM = 0x128B5CF6;
    public static final int AC_BORDER = 0x388B5CF6;
    public static final int AC_FG = 0xFF1A0D2E;
    public static final int AC_GLOW = 0x2E8B5CF6;
    public static final int PURPLE_BG = 0x1AA78BFA;

    // Radius (pixels)
    public static final int R_WINDOW = 19;
    public static final int R_PANEL = 12;
    public static final int R_CARD = 12;
    public static final int R_BUTTON = 8;
    public static final int R_INPUT = 6;
    public static final int R_BADGE = 999;

    // Spacing (pixels)
    public static final int SP_1 = 4;
    public static final int SP_2 = 8;
    public static final int SP_3 = 12;
    public static final int SP_4 = 16;
    public static final int SP_6 = 24;
    public static final int SP_8 = 32;
    public static final int SP_12 = 48;
    public static final int SP_16 = 64;

    // Typography
    public static final int TYPE_HERO = 28;
    public static final int TYPE_DISPLAY = 20;
    public static final int TYPE_HEADING = 16;
    public static final int TYPE_SUBHEADING = 13;
    public static final int TYPE_DEFAULT = 12;
    public static final int TYPE_BODY = 11;
    public static final int TYPE_LABEL = 10;
    public static final int TYPE_MICRO = 9;

    // Font Resource IDs
    public static final String FONT_UI = "mvxmenu:jetbrains_mono";
    public static final String FONT_BODY = "mvxmenu:inter";
    public static final String FONT_PIXEL = "mvxmenu:press_start_2p";

    // Motion Durations (ms)
    public static final int MOTION_MICRO = 80;
    public static final int MOTION_SNAP = 120;
    public static final int MOTION_SLIDE = 200;
    public static final int MOTION_FADE = 200;
    public static final int MOTION_SPRING = 300;

    // Elevation Shadows
    public static final int SHADOW_SM = 0x40000000;
    public static final int SHADOW_MD = 0x60000000;
    public static final int SHADOW_LG = 0x80000000;
    public static final int SHADOW_PANEL = 0x08FFFFFF;

    // High Contrast Mode
    public static final int HC_BORDER = 0xFFFFFFFF;
    public static final int HC_TEXT = 0xFFFFFFFF;
    public static final int HC_BG = 0xFF000000;

    private MvxmenuTheme() {}
}