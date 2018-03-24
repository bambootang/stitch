package bamboo.sample.tasks.component;

import bamboo.component.lifecycle.ComponentLife;
import bamboo.component.lifecycle.ComponentPriority;
import bamboo.component.stitch.anno.Component;
import bamboo.sample.tasks.models.TasksRepository;

/**
 * Created by tangshuai on 2018/3/16.
 */

@Component
public class TasksComponentLife extends ComponentLife {

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
