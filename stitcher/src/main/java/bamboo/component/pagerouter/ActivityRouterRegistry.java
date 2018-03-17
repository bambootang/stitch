package bamboo.component.pagerouter;

import java.lang.reflect.Method;
import java.util.HashMap;

import bamboo.component.pagerouter.PageResolveHelper;


public final class ActivityRouterRegistry {


    private final HashMap<Class, Method> methodMap = new HashMap<>();

    private final HashMap<Class, Object> holderMap = new HashMap<>();

    public void regiest(Object pageConsumer) {
        if (pageConsumer == null) {
            throw new IllegalArgumentException("pageConsumer must not be null");
        }
        PageResolveHelper.resolvePageConsumer(pageConsumer, methodMap, holderMap);
    }

    public Method searchMethod(Class pageClass) {
        return methodMap.get(pageClass);
    }

    public Object searchConsumer(Class pageClass) {
        return holderMap.get(pageClass);
    }

}
