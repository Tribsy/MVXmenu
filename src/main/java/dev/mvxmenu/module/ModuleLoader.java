package dev.mvxmenu.module;

import dev.mvxmenu.module.ModuleProvider;
import java.util.Collection;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;

/**
 * Loads modules via the Fabric entrance point {@code mvxmenu:modules}.
 * <p>
 * This class is responsible for discovering all {@link ModuleProvider} implementations,
 * collecting their modules, and registering them with the {@link ModuleRegistry}.
 * After all modules are registered, it initializes the registry.
 * </p>
 */
public final class ModuleLoader {

    private ModuleLoader() {
    }

    /**
     * Loads all module providers and registers their modules.
     * <p>
     * This method should be called early during client initialization (e.g., in {@link MvxmenuClient#onInitialize()}).
     * </p>
     */
    public static void loadModules() {
        ModuleRegistry registry = ModuleRegistry.get();
        Collection<EntrypointContainer<ModuleProvider>> containers = FabricLoader.getInstance()
                .getEntrypointContainers("mvxmenu:modules", ModuleProvider.class);
        for (EntrypointContainer<ModuleProvider> container : containers) {
            ModuleProvider provider = container.getEntrypoint();
            Collection<Module> modules = provider.getModules();
            for (Module module : modules) {
                registry.register(module);
            }
        }
        // Initialize the registry (topological sort and onInitialize)
        registry.initialize();
    }
}