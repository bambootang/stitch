package bamboo.component.lifecycle;


import android.app.Application;

import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

public final class ComponentLifeRegistry {


    private final TreeSet<ComponentApplication> componentApplicationTreeSet;

    public ComponentLifeRegistry() {
        componentApplicationTreeSet = new TreeSet<>(new ComponentLevelComparator());
    }

    public void registerFromManifest(Application application) {
        List<ComponentApplication> applicationCollections = ComponentAppResolve.findAllAppLibrary(application);
        for (ComponentApplication component : applicationCollections) {
            register(component);
        }
    }

    public void register(ComponentApplication componentApplication) {
        componentApplicationTreeSet.add(componentApplication);
    }

    public <T extends ComponentApplication> T search(Class<T> clasz) {
        for (ComponentApplication application : componentApplicationTreeSet) {
            if (application.getClass() == clasz) {
                return (T) application;
            }
        }
        throw new IllegalStateException(clasz.getSimpleName() + " unregistered!");
    }

    /**
     * 只允许注册不允许注销，所以这里为了避免被误操作，复制一份返回
     *
     * @return
     */
    public Collection<ComponentApplication> getAll() {
        return (Collection<ComponentApplication>) componentApplicationTreeSet.clone();
    }

}
