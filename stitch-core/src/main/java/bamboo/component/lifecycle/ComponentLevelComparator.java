package bamboo.component.lifecycle;

import java.util.Comparator;


public class ComponentLevelComparator implements Comparator<ComponentLife> {


    @Override
    public int compare(ComponentLife o1, ComponentLife o2) {
        return o1.level() - o2.level();
    }
}
