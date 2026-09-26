package dev.mvxmenu.module.impl;

import dev.mvxmenu.module.Module;
import dev.mvxmenu.module.ModuleProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Built-in module provider that supplies all modules included in the MVXmenu mod.
 * <p>
 * This provider is discovered via the {@code mvxmenu:modules} entrance point.
 * </p>
 */
public final class BuiltInModuleProvider implements ModuleProvider {

    private BuiltInModuleProvider() {
    }

    @Override
    public List<Module> getModules() {
        List<Module> modules = new ArrayList<>();
        modules.add(new KillAuraModule());
        modules.add(new AutoEatModule());
        modules.add(new AutoToolModule());
        modules.add(new ESPModule());
        modules.add(new FlightModule());
        modules.add(new FreecamModule());
        modules.add(new NoFallModule());
        modules.add(new ScaffoldModule());
        modules.add(new SprintModule());
        modules.add(new TimerModule());
        modules.add(new XRayModule());
        return Collections.unmodifiableList(modules);
    }
}