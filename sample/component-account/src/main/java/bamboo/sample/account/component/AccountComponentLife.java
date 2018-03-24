package bamboo.sample.account.component;

import bamboo.component.lifecycle.ComponentLife;
import bamboo.component.lifecycle.ComponentPriority;
import bamboo.component.stitch.anno.Component;

@Component
public class AccountComponentLife extends ComponentLife {


    @Override
    public void onLowMemory() {
        System.out.println("onLowMemory");
    }

    @Override
    public int level() {
        return ComponentPriority.LEVEL_MID;
    }
}
