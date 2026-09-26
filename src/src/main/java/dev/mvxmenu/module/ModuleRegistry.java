package dev.mvxmenu.module;

import dev.mvxmenu.module.Module;
import dev.mvxmenu.module.ModuleId;
import dev.mvxmenu.module.ModuleMetadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Central registry for all modules in MVXmenu.
 * <p>
 * The registry is responsible for:
 * <ul>
 *   <li>Storing registered modules.</li>
 *   <li>Resolving dependencies and providing a topological order for initialization and ticking.</li>
 *   <li>Managing the enabled state of modules.</li>
 *   <li>Providing lookup by ID, category, etc.</li>
 * </ul>
 * </p>
 * <p>
 * Modules are registered via the {@link ModuleLoader} which discovers {@link ModuleProvider} SPI implementations.
 * </p>
 */
public final class ModuleRegistry {

    /** The singleton instance. */
    private static final ModuleRegistry INSTANCE = new ModuleRegistry();

    /** Map of module ID to module instance. */
    private final Map<ModuleId, Module> modules = new HashMap<>();

    /** Set of enabled module IDs. */
    private final Set<ModuleId> enabledModules = new HashSet<>();

    /** List of modules in topological order (based on hard dependencies). */
    private List<Module> orderedModules = List.of();

    /** Flag indicating whether the registry has been initialized (modules sorted and onInitialize called). */
    private boolean initialized = false;

    private ModuleRegistry() {
    }

    /**
     * Returns the singleton instance of the module registry.
     *
     * @return the module registry
     */
    public static ModuleRegistry get() {
        return INSTANCE;
    }

    /**
     * Registers a module with the registry.
     * <p>
     * This method is intended for internal use by the {@link ModuleLoader}.
     * </p>
     *
     * @param module the module to register
     */
    public void register(Module module) {
        Objects.requireNonNull(module);
        ModuleId id = module.getId();
        if (modules.containsKey(id)) {
            throw new IllegalArgumentException("Module with ID " + id + " is already registered");
        }
        modules.put(id, module);
        // Invalidate the ordered list because a new module may change the topology
        this.orderedModules = List.of();
    }

    /**
     * Initializes the registry.
     * <p>
     * This method should be called after all modules have been registered.
     * It performs a topological sort based on hard dependencies and calls {@link Module#onInitialize()}
     * on each module in order.
     * </p>
     */
    public void initialize() {
        if (initialized) {
            return;
        }
        // Perform topological sort based on hard dependencies
        this.orderedModules = topologicalSort(modules.values());
        // Call onInitialize for each module in order
        for (Module module : orderedModules) {
            module.onInitialize();
        }
        initialized = true;
    }

    /**
     * Returns a topological ordering of the given modules based on their hard dependencies.
     * <p>
     * Uses Kahn's algorithm. Assumes there are no cyclic dependencies (if there are, an arbitrary order is returned).
     * </p>
     *
     * @param modules the modules to sort
     * @return a list of modules in topological order
     */
    private List<Module> topologicalSort(java.util.Collection<Module> modules) {
        Map<ModuleId, Module> moduleMap = new HashMap<>();
        for (Module module : modules) {
            moduleMap.put(module.getId(), module);
        }

        Map<ModuleId, Integer> inDegree = new HashMap<>();
        Map<ModuleId, List<ModuleId>> adjacency = new HashMap<>();

        // Initialize
        for (Module module : modules) {
            ModuleId id = module.getId();
            inDegree.putIfAbsent(id, 0);
            adjacency.putIfAbsent(id, new ArrayList<>());
            for (ModuleId dep : module.getDependencies()) {
                adjacency.computeIfAbsent(dep, k -> new ArrayList<>()).add(id);
                inDegree.put(id, inDegree.getOrDefault(id, 0) + 1);
            }
        }

        // Kahn's algorithm
        List<ModuleId> zeroInDegree = new ArrayList<>();
        for (Map.Entry<ModuleId, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                zeroInDegree.add(entry.getKey());
            }
        }

        List<ModuleId> sortedIds = new ArrayList<>();
        while (!zeroInDegree.isEmpty()) {
            ModuleId id = zeroInDegree.remove(zeroInDegree.size() - 1);
            sortedIds.add(id);
            for (ModuleId dependent : adjacency.getOrDefault(id, List.of())) {
                int newInDegree = inDegree.get(dependent) - 1;
                inDegree.put(dependent, newInDegree);
                if (newInDegree == 0) {
                    zeroInDegree.add(dependent);
                }
            }
        }

        // If there are remaining edges, there is a cycle; we just append the rest in any order
        if (sortedIds.size() != moduleMap.size()) {
            for (ModuleId id : moduleMap.keySet()) {
                if (!sortedIds.contains(id)) {
                    sortedIds.add(id);
                }
            }
        }

        // Convert sorted IDs to module list
        List<Module> sorted = new ArrayList<>(sortedIds.size());
        for (ModuleId id : sortedIds) {
            sorted.add(moduleMap.get(id));
        }
        return sorted;
    }

    /**
     * Returns the module with the given ID, or {@code null} if not found.
     *
     * @param id the module ID
     * @return the module, or null
     */
    public Module get(ModuleId id) {
        return modules.get(id);
    }

    /**
     * Returns an immutable snapshot of all registered modules.
     * <p>
     * The order is the topological order based on hard dependencies.
     * </p>
     *
     * @return an immutable list of all modules
     */
    public List<Module> getAll() {
        return Collections.unmodifiableList(orderedModules);
    }

    /**
     * Returns an immutable snapshot of all modules in the given category.
     * <p>
     * The order is the topological order based on hard dependencies.
     * </p>
     *
     * @param category the category
     * @return an immutable list of modules in the category
     */
    public List<Module> getByCategory(Module.Category category) {
        List<Module> result = new ArrayList<>();
        for (Module module : orderedModules) {
            if (module.getCategory() == category) {
                result.add(module);
            }
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Returns an immutable snapshot of all enabled modules.
     * <p>
     * The order is the topological order based on hard dependencies.
     * </p>
     *
     * @return an immutable list of enabled modules
     */
    public List<Module> getEnabled() {
        List<Module> result = new ArrayList<>();
        for (Module module : orderedModules) {
            if (isEnabled(module.getId())) {
                result.add(module);
            }
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Returns true if the module with the given ID is enabled.
     *
     * @param id the module ID
     * @return true if enabled
     */
    public boolean isEnabled(ModuleId id) {
        return enabledModules.contains(id);
    }

    /**
     * Enables the module with the given ID.
     * <p>
     * If the module is already enabled, this method does nothing.
     * </p>
     *
     * @param id the module ID
     */
    public void enable(ModuleId id) {
        Module module = get(id);
        if (module == null) {
            return;
        }
        if (isEnabled(id)) {
            return;
        }
        enabledModules.add(id);
        module.setEnabled(true);
        module.onEnable();
    }

    /**
     * Disables the module with the given ID.
     * <p>
     * If the module is already disabled, this method does nothing.
     * </p>
     *
     * @param id the module ID
     */
    public void disable(ModuleId id) {
        Module module = get(id);
        if (module == null) {
            return;
        }
        if (!isEnabled(id)) {
            return;
        }
        enabledModules.remove(id);
        module.setEnabled(false);
        module.onDisable();
    }

    /**
     * Toggles the enabled state of the module with the given ID.
     *
     * @param id the module ID
     */
    public void toggle(ModuleId id) {
        if (isEnabled(id)) {
            disable(id);
        } else {
            enable(id);
        }
    }

    /**
     * Called every tick to update all enabled modules.
     * <p>
     * This method iterates over enabled modules in topological order and calls {@link Module#onTick()}
     * on each.
     * </p>
     */
    public void onTick() {
        for (Module module : getEnabled()) {
            module.onTick();
        }
    }
}