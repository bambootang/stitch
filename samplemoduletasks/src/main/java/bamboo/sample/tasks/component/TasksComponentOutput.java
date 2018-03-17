package bamboo.sample.tasks.component;

import bamboo.sample.tasksrouter.ITaskComponent;
import bamboo.sample.tasks.models.TasksRepository;

/**
 * Created by tangshuai on 2018/3/16.
 */

public class TasksComponentOutput implements ITaskComponent {

    TasksRepository tasksRepository;

    public TasksComponentOutput(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    @Override
    public int getTaskCount() {
        return tasksRepository.getTaskCount();
    }
}
