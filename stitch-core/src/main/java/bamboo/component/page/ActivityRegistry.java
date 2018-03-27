package bamboo.component.page;


import java.lang.reflect.Method;
import java.util.HashMap;


public final class ActivityRegistry {

    final HashMap<String, String> autoLinkMap = new HashMap<>();
    final HashMap<String, Method> receiveMethodMap = new HashMap<>();

    public synchronized String register(String linkBean, String activityClass) {
        assert linkBean != null;
        assert activityClass != null;
        if (autoLinkMap.containsKey(linkBean)) {
            return autoLinkMap.get(linkBean);
        } else {
            autoLinkMap.put(linkBean, activityClass);
            return activityClass;
        }
    }

    public synchronized Method register(String linkBean, Method method) {
        assert linkBean != null;
        assert method != null;
        if (receiveMethodMap.containsKey(linkBean)) {
            return receiveMethodMap.get(linkBean);
        } else {
            receiveMethodMap.put(linkBean, method);
            return method;
        }
    }

    public synchronized String search(String linkBean) {
        assert linkBean != null;
        if (autoLinkMap.containsKey(linkBean)) {
            return autoLinkMap.get(linkBean);
        }
        return null;
    }

    public synchronized Method searchMethod(String linkBean) {
        assert linkBean != null;
        if (receiveMethodMap.containsKey(linkBean)) {
            return receiveMethodMap.get(linkBean);
        }
        return null;
    }

}
