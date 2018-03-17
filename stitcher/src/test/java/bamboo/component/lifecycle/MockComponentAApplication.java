package bamboo.component.lifecycle;

import bamboo.component.lifecycle.ComponentApplication;
import bamboo.component.lifecycle.ComponentPriority;

/**
 * Created by tangshuai on 2018/3/17.
 */

public class MockComponentAApplication extends ComponentApplication {

    @Override
    public int level() {
        return ComponentPriority.LEVEL_HIGH;
    }

}
