package bamboo.component.lifecycle;

import java.util.Comparator;


public class ComponentLevelComparator implements Comparator<ComponentApplication> {


    @Override
    public int compare(ComponentApplication o1, ComponentApplication o2) {
        return o1.level() - o2.level();
    }
}
