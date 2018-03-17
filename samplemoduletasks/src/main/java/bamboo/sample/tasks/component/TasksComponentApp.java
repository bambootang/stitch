package bamboo.sample.tasks.component;

import bamboo.component.datarouter.ComponentRouterRegistry;
import bamboo.component.lifecycle.ComponentApplication;
import bamboo.component.lifecycle.ComponentPriority;
import bamboo.component.pagerouter.ActivityRouterRegistry;
import bamboo.sample.tasksrouter.ITaskComponent;
import bamboo.sample.tasks.models.TasksRepository;

/**
 * Created by tangshuai on 2018/3/16.
 */

public class TasksComponentApp extends ComponentApplication {

    TasksRepository tasksRepository = new TasksRepository();

    public void onCreate() {

    }

    @Override
    public void onCreateDelay(ComponentRouterRegistry routerRegistry, ActivityRouterRegistry activityRouterRegistry) {
        routerRegistry.register(ITaskComponent.class, new TasksComponentOutput(tasksRepository));
        activityRouterRegistry.regiest(new TasksPageConsumer());
    }

    @Override
    public int level() {
        return ComponentPriority.LEVEL_LOW;
    }

    public TasksRepository getTasksRepository() {
        return tasksRepository;
    }
}
