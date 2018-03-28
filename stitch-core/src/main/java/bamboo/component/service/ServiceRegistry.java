package bamboo.component.service;


import java.util.HashMap;

import bamboo.component.Asserts;


public final class ServiceRegistry {

    final HashMap<String, Class> serviceMap = new HashMap<>();

    private final HashMap<String, Object> serviceObjMap = new HashMap<>();

    public synchronized <I, IMP extends I> void register(Class<I> serviceClass, Class<IMP> component) {
        Asserts.assertNotNull(serviceClass, "serviceClass can not be null !");
        Asserts.assertNotNull(component, "component can not be null !");
        String serviceName = serviceClass.getName();
        if (!serviceMap.containsKey(serviceName)) {
            serviceMap.put(serviceName, component);
        }
    }

    public synchronized <T> T search(Class<T> serviceClass) {
        Asserts.assertNotNull(serviceClass, "serviceClass can not be null !");
        String serviceName = serviceClass.getName();
        if (serviceMap.containsKey(serviceName)) {
            T component;
            if (!serviceObjMap.containsKey(serviceName)) {
                try {
                    component = (T) serviceMap.get(serviceName).newInstance();
                    serviceObjMap.put(serviceName, component);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            component = (T) serviceObjMap.get(serviceName);
            return component;
        }
        return null;
    }

}
