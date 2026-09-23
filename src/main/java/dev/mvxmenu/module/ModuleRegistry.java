package dev.mvxmenu.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleRegistry {

    private static final ModuleRegistry INSTANCE = new ModuleRegistry();

    private final Map<String, Module> modules = new HashMap<>();
    private final Map<Module.Category, List<Module>> modulesByCategory = new HashMap<>();

    private ModuleRegistry() {
        for (Module.Category cat : Module.Category.values()) {
            modulesByCategory.put(cat, new ArrayList<>());
        }
    }

    public static ModuleRegistry get() {
        return INSTANCE;
    }

    public void register(Module module) {
        if (modules.containsKey(module.getId())) {
            throw new IllegalArgumentException("Module with id " + module.getId() + " already registered");
        }
        modules.put(module.getId(), module);
        modulesByCategory.get(module.getCategory()).add(module);
    }

    public Module get(String id) {
        return modules.get(id);
    }

    public List<Module> getAll() {
        return new ArrayList<>(modules.values());
    }

    public List<Module> getByCategory(Module.Category category) {
        return new ArrayList<>(modulesByCategory.getOrDefault(category, List.of()));
    }

    public List<Module> getEnabled() {
        return modules.values().stream().filter(Module::isEnabled).toList();
    }

    public void enable(String id) {
        Module module = modules.get(id);
        if (module != null && !module.isEnabled()) {
            module.setEnabled(true);
            module.onEnable();
        }
    }

    public void disable(String id) {
        Module module = modules.get(id);
        if (module != null && module.isEnabled()) {
            module.setEnabled(false);
            module.onDisable();
        }
    }

    public void toggle(String id) {
        Module module = modules.get(id);
        if (module != null) {
            if (module.isEnabled()) {
                disable(id);
            } else {
                enable(id);
            }
        }
    }

    public void onTick() {
        for (Module module : modules.values()) {
            if (module.isEnabled()) {
                module.onTick();
            }
        }
    }
}