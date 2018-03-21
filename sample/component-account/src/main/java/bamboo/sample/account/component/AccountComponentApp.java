package bamboo.sample.account.component;

import bamboo.component.lifecycle.ComponentApplication;
import bamboo.component.lifecycle.ComponentPriority;
import bamboo.component.stitch.anno.LifeCycle;

@LifeCycle
public class AccountComponentApp extends ComponentApplication {


    @Override
    public void onLowMemory() {
        System.out.println("onLowMemory");
    }

    @Override
    public int level() {
        return ComponentPriority.LEVEL_MID;
    }
}
