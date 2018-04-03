package bamboo.component.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bamboo.component.Asserts;


public final class ServiceRegistry {


    final HashMap<String, String> serviceMap = new HashMap<>();

    private final HashMap<String, Object> serviceObjMap = new HashMap<>();

    final HashMap<String, List<String>> serviceSetMap = new HashMap<>();


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
        Asserts.assertNotNull(impClassName, "impClassName can not be null !");
        String serviceName = serviceClass.getName();
        if (!serviceMap.containsKey(serviceName)) {
            serviceMap.put(serviceName, impClassName);
        }
        if (!serviceSetMap.containsKey(serviceName)) {
            List<String> impClassSet = new ArrayList<>();
            impClassSet.add(impClassName);
            serviceSetMap.put(serviceName, impClassSet);
        } else {
            List<String> impClassSet = serviceSetMap.get(serviceName);
            if (!impClassSet.contains(impClassName)) {
                impClassSet.add(0, impClassName);
            }
        }
    }

    public synchronized <T> T search(Class<T> serviceClass) {
        Asserts.assertNotNull(serviceClass, "serviceClass can not be null !");
        String serviceName = serviceClass.getName();
        if (serviceObjMap.containsKey(serviceName)) {
            return (T) serviceObjMap.get(serviceName);
        }
        if (serviceMap.containsKey(serviceName)) {
            T component = (T) instanceServiceImp(serviceMap.get(serviceName));
            serviceObjMap.put(serviceName, component);
            serviceObjMap.put(serviceMap.get(serviceName), component);
            return component;
        }
        return null;
    }

    private Object instanceServiceImp(String serviceImpClass) {
        try {
            Class impClass = Class.forName(serviceImpClass);
            Object object = impClass.newInstance();
            return object;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized <T> List<T> searchAll(Class<T> serviceClass) {
        String serviceName = serviceClass.getName();
        List<String> impClassNameList = serviceSetMap.get(serviceName);
        List<T> impObject = new ArrayList<>();
        if (impClassNameList != null && impClassNameList.size() > 0) {
            for (String impClassName : impClassNameList) {
                if (serviceObjMap.containsKey(impClassName)) {
                    impObject.add((T) serviceObjMap.get(impClassName));
                } else {
                    T t = (T) instanceServiceImp(impClassName);
                    serviceObjMap.put(impClassName, t);
                    impObject.add(t);
                }
            }
        }
        return impObject;
    }
}
