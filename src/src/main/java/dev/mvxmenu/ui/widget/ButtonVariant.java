package dev.mvxmenu.ui.widget;

import dev.mvxmenu.theme.MvxmenuTheme;

public enum ButtonVariant {
    PRIMARY(MvxmenuTheme.AC, MvxmenuTheme.TX_0),
    GHOST(MvxmenuTheme.BD_1, MvxmenuTheme.TX_1),
    DANGER(MvxmenuTheme.DANGER, MvxmenuTheme.TX_0),
    SUCCESS(MvxmenuTheme.SUCCESS, MvxmenuTheme.TX_0);

    public final int bgColor;
    public final int textColor;

    ButtonVariant(int bgColor, int textColor) {
        this.bgColor = bgColor;
        this.textColor = textColor;
    }
}
