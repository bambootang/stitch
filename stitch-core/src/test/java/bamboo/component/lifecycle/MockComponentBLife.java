package bamboo.component.lifecycle;

/**
 * Created by tangshuai on 2018/3/17.
 */

public class MockComponentBLife extends ComponentLife {

    @Override
    public int level() {
        return ComponentPriority.LEVEL_LOW;
    }
}
