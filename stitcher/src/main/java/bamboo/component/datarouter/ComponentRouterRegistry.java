package bamboo.component.datarouter;


import java.util.HashMap;

import bamboo.component.datarouter.ComponentOutput;

public final class ComponentRouterRegistry {

    final HashMap<Class, Object> routerMap = new HashMap<>();

    public synchronized <T extends ComponentOutput> T register(Class<T> componentClass, ComponentOutput component) {
        assert componentClass != null;
        assert component != null;
        if (routerMap.containsKey(componentClass)) {
            ComponentOutput oldComponent = (ComponentOutput) routerMap.get(componentClass);
            return (T) oldComponent;
        } else {
            routerMap.put(componentClass, component);
            return (T) component;
        }
    }

    public synchronized <T> T search(Class<T> componentClass) {
        assert componentClass != null;
        if (routerMap.containsKey(componentClass)) {
            T component = (T) routerMap.get(componentClass);
            return component;
        }
        return null;
    }

}
