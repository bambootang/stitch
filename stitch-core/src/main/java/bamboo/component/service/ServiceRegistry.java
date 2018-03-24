package bamboo.component.service;


import java.util.HashMap;


public final class ServiceRegistry {

    final HashMap<String, Object> serviceMap = new HashMap<>();

    public synchronized <I, IMP extends I> I register(Class<I> serviceClass, IMP component) {
        assert serviceClass != null;
        assert component != null;
        String serviceName = serviceClass.getName();
        if (serviceMap.containsKey(serviceName)) {
            I oldComponent = (I) serviceMap.get(serviceName);
            return oldComponent;
        } else {
            serviceMap.put(serviceName, component);
            return component;
        }
    }

    public synchronized <T> T search(Class<T> serviceClass) {
        assert serviceClass != null;
        String serviceName = serviceClass.getName();
        if (serviceMap.containsKey(serviceName)) {
            T component = (T) serviceMap.get(serviceName);
            return component;
        }
        return null;
    }

}
