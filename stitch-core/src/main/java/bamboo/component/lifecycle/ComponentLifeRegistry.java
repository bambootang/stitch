package bamboo.component.lifecycle;


import android.app.Application;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public final class ComponentLifeRegistry {


    private final Set<ComponentApplication> componentApplicationTreeSet;
    private final HashMap<String, ComponentApplication> componentApplicationHashMap;

    public ComponentLifeRegistry() {
        componentApplicationTreeSet = new HashSet<>();
        componentApplicationHashMap = new HashMap<>();
    }

    public void registerFromManifest(Application application) {
        List<ComponentApplication> applicationCollections = ComponentAppResolve.findAllAppLibrary(application);
        for (ComponentApplication component : applicationCollections) {
            register(component.getName(), component);
        }
    }


    public void register(String name, ComponentApplication componentApplication) {
        componentApplicationTreeSet.add(componentApplication);
        if (!name.equals(ComponentApplication.class.getCanonicalName())) {
            componentApplicationHashMap.put(name, componentApplication);
        }
        componentApplicationHashMap.put(componentApplication.getClass().getCanonicalName(), componentApplication);
    }

    public <T extends ComponentApplication> T search(Class<T> clasz) {
        if (componentApplicationHashMap.containsKey(clasz.getCanonicalName())) {
            return (T) componentApplicationHashMap.get(clasz.getCanonicalName());
        }
        System.err.println(clasz.getSimpleName() + " unregistered!");
        return null;
//        throw new IllegalStateException(clasz.getSimpleName() + " unregistered!");
    }

    /**
     * 只允许注册不允许注销，所以这里为了避免被误操作，复制一份返回
     *
     * @return
     */
    public Iterable<ComponentApplication> getAll() {
        return componentApplicationTreeSet;
    }

}
