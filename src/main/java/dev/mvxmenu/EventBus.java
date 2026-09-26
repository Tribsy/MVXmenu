package dev.mvxmenu;

import java.util.*;
import java.util.function.Consumer;

/**
 * Simple lightweight EventBus for decoupling UI and Module logic.
 */
public class EventBus {
    private static final Map<Class<?>, List<Consumer<Object>>> listeners = new HashMap<>();

    public static <T> void subscribe(Class<T> eventClass, Consumer<T> listener) {
        listeners.computeIfAbsent(eventClass, k -> new ArrayList<>())
                 .add(obj -> listener.accept((T) obj));
    }

    public static void publish(Object event) {
        List<Consumer<Object>> targets = listeners.get(event.getClass());
        if (targets != null) {
            targets.forEach(l -> l.accept(event));
        }
    }

    public static class ModuleStateEvent {
        public final String moduleId;
        public final boolean enabled;

        public ModuleStateEvent(String moduleId, boolean enabled) {
            this.moduleId = moduleId;
            this.enabled = enabled;
        }
    }
}

