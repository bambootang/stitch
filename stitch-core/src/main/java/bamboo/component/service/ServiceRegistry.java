package bamboo.component.service;


import java.util.HashMap;

import bamboo.component.Asserts;


public final class ServiceRegistry {

    final HashMap<String, String> serviceMap = new HashMap<>();

    private final HashMap<String, Object> serviceObjMap = new HashMap<>();

    public synchronized <I, IMP extends I> void register(Class<I> serviceClass, IMP imp) {
        Asserts.assertNotNull(serviceClass, "serviceClass can not be null !");
        Asserts.assertNotNull(imp, "component can not be null !");
        String serviceName = serviceClass.getName();
        if (!serviceObjMap.containsKey(serviceName)) {
            serviceObjMap.put(serviceName, imp);
        }
    }

    public synchronized <I, IMP extends I> void register(Class<I> serviceClass, String impClassName) {
        Asserts.assertNotNull(serviceClass, "serviceClass can not be null !");
        Asserts.assertNotNull(impClassName, "component can not be null !");
        String serviceName = serviceClass.getName();
        if (!serviceMap.containsKey(serviceName)) {
            serviceMap.put(serviceName, impClassName);
        }
    }

    public synchronized <T> T search(Class<T> serviceClass) {
        Asserts.assertNotNull(serviceClass, "serviceClass can not be null !");
        String serviceName = serviceClass.getName();
        if (serviceObjMap.containsKey(serviceName)) {
            return (T) serviceObjMap.get(serviceName);
        }
        if (serviceMap.containsKey(serviceName)) {
            T component;
            try {
                Class impClass = Class.forName(serviceMap.get(serviceName));
                component = (T) impClass.newInstance();
                serviceObjMap.put(serviceName, component);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            component = (T) serviceObjMap.get(serviceName);
            return component;
        }
        return null;
    }

}
