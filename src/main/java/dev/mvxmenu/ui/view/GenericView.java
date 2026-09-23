package dev.mvxmenu.ui.view;

import dev.mvxmenu.ui.widget.ModuleCardWidget;

import java.util.List;

public class GenericView {

    private final List<ModuleCardWidget> modules;

    public GenericView(List<ModuleCardWidget> modules) {
        this.modules = modules;
    }

    public List<ModuleCardWidget> getModules() {
        return modules;
    }

    public int getColumnCount() {
        return Math.min(3, Math.max(1, modules.size()));
    }

    public int getRowCount() {
        return (int) Math.ceil((double) modules.size() / getColumnCount());
    }
}
