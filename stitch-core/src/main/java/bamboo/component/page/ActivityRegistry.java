package bamboo.component.page;


import java.util.HashMap;


public final class ActivityRegistry {

    final HashMap<String, String> autoLinkMap = new HashMap<>();

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

    public synchronized String search(String linkBean) {
        assert linkBean != null;
        if (autoLinkMap.containsKey(linkBean)) {
            return autoLinkMap.get(linkBean);
        }
        return null;
    }

}
