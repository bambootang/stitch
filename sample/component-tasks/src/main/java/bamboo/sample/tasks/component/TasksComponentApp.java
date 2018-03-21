package bamboo.sample.tasks.component;

import bamboo.component.lifecycle.ComponentApplication;
import bamboo.component.lifecycle.ComponentPriority;
import bamboo.component.stitch.anno.LifeCycle;
import bamboo.sample.tasks.models.TasksRepository;

/**
 * Created by tangshuai on 2018/3/16.
 */

@LifeCycle
public class TasksComponentApp extends ComponentApplication {

    TasksRepository tasksRepository = new TasksRepository();

    public void onCreate() {

    }

    @Override
    public int level() {
        return ComponentPriority.LEVEL_LOW;
    }

    public TasksRepository getTasksRepository() {
        return tasksRepository;
    }
}
