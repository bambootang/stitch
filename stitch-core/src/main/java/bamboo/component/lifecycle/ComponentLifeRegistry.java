package bamboo.component.lifecycle;


import android.app.Application;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ComponentLifeRegistry {


    private final Set<ComponentLife> componentLifeTreeSet;
    private final HashMap<String, ComponentLife> componentApplicationHashMap;

    public ComponentLifeRegistry() {
        componentLifeTreeSet = new HashSet<>();
        componentApplicationHashMap = new HashMap<>();
    }

    public void registerFromManifest(Application application) {
        List<ComponentLife> applicationCollections = ComponentAppResolve.findAllAppLibrary(application);
        for (ComponentLife component : applicationCollections) {
            register(component.getName(), component);
        }
    }


    public void register(String name, ComponentLife componentLife) {
        componentLifeTreeSet.add(componentLife);
        if (!name.equals(ComponentLife.class.getCanonicalName())) {
            componentApplicationHashMap.put(name, componentLife);
        }
        componentApplicationHashMap.put(componentLife.getClass().getCanonicalName(), componentLife);
    }

    public <T extends ComponentLife> T search(Class<T> clasz) {
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
    public Iterable<ComponentLife> getAll() {
        return componentLifeTreeSet;
    }

}
