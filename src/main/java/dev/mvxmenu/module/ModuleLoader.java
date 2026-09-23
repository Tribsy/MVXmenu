package dev.mvxmenu.module;

import dev.mvxmenu.module.impl.*;

public class ModuleLoader {

    public static void loadAll() {
        ModuleRegistry registry = ModuleRegistry.get();

        registry.register(new SprintModule());
        registry.register(new FlightModule());
        registry.register(new ScaffoldModule());
        registry.register(new KillAuraModule());
        registry.register(new AutoEatModule());
        registry.register(new AutoToolModule());
        registry.register(new NoFallModule());
        registry.register(new TimerModule());
        registry.register(new FreecamModule());
        registry.register(new ESPModule());
        registry.register(new XRayModule());
    }
}