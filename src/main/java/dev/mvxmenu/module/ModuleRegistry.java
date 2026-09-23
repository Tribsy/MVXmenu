package dev.mvxmenu.module;

import java.util.Collections;
import java.util.List;

public class ModuleRegistry {

    private static final ModuleRegistry INSTANCE = new ModuleRegistry();

    private ModuleRegistry() {
    }

    public static ModuleRegistry get() {
        return INSTANCE;
    }

    public Module get(String id) {
        return null;
    }

    public List<Module> getAll() {
        return Collections.emptyList();
    }

    public List<Module> getByCategory(Module.Category category) {
        return Collections.emptyList();
    }

    public List<Module> getEnabled() {
        return Collections.emptyList();
    }

    public void enable(String id) {
    }

    public void disable(String id) {
    }

    public void toggle(String id) {
    }

    public void onTick() {
    }
}