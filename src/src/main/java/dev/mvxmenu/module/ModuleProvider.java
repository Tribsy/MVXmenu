package dev.mvxmenu.module;

import java.util.Collection;

/**
 * Service provider interface for modules.
 * <p>
 * Implementations of this interface are discovered via the Fabric entrance point {@code mvxmenu:modules}
 * and are responsible for providing a collection of modules to be registered with the {@link ModuleRegistry}.
 * </p>
 */
public interface ModuleProvider {

    /**
     * Returns a collection of modules provided by this provider.
     * <p>
     * The modules returned should be ready to be registered (i.e., they have been instantiated).
     * </p>
     *
     * @return a collection of modules
     */
    Collection<Module> getModules();
}