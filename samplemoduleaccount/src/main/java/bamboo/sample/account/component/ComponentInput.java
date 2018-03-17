package bamboo.sample.account.component;

import bamboo.component.StitcherHelper;
import bamboo.sample.tasksrouter.ITaskComponent;

public class ComponentInput {

    ITaskComponent taskComponent;

    private static final ComponentInput INSTANCE = new ComponentInput();

    private ComponentInput() {

    }

    public static ComponentInput get() {
        return INSTANCE;
    }

    public int getTaskCount() {
        if (taskComponent == null) {
            taskComponent = StitcherHelper.searchComponentOutput(ITaskComponent.class);
        }
        return taskComponent == null ? -1 : taskComponent.getTaskCount();
    }

}
