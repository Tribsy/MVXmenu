package dev.mvxmenu.ui.views;

import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.ui.widget.MvxmenuWidget;
import net.minecraft.client.gui.DrawContext;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseView {
    protected final List<MvxmenuWidget> widgets = new ArrayList<>();
    protected final MvxmenuLayout layout;

    public BaseView(MvxmenuLayout layout) {
        this.layout = layout;
    }

    public abstract void init();
    public abstract void render(DrawContext context, int mouseX, int mouseY, float delta);

    public List<MvxmenuWidget> getWidgets() {
        return widgets;
    }

    public void addWidget(MvxmenuWidget widget) {
        widgets.add(widget);
    }

    public void clearWidgets() {
        widgets.clear();
    }
} 
