package dev.mvxmenu.ui.navigation;

import dev.mvxmenu.ui.ViewType;
import dev.mvxmenu.ui.widget.ModuleCardWidget;
import java.util.Stack;

public class ViewManager {
    private ViewType activeView = ViewType.GENERIC;
    private ModuleCardWidget selectedModule;
    private final Stack<ViewType> history = new Stack<>();

    public ViewType getActiveView() {
        return activeView;
    }

    public void setView(ViewType view) {
        if (activeView != ViewType.GENERIC) {
            history.push(activeView);
        }
        activeView = view;
    }

    public void setView(ViewType view, ModuleCardWidget module) {
        if (activeView != ViewType.GENERIC) {
            history.push(activeView);
        }
        this.activeView = view;
        this.selectedModule = module;
    }

    public ModuleCardWidget getSelectedModule() {
        return selectedModule;
    }

    public void setSelectedModule(ModuleCardWidget module) {
        this.selectedModule = module;
    }

    public void goBack() {
        if (!history.isEmpty()) {
            activeView = history.pop();
        } else {
            activeView = ViewType.GENERIC;
        }
        selectedModule = null;
    }

    public void clearHistory() {
        history.clear();
    }
} 
