package bamboo.sample.tasks.models;

import bamboo.component.StitcherHelper;
import bamboo.component.stitch.anno.Service;
import bamboo.sample.accountrouter.IAccount;
import bamboo.sample.tasks.component.TasksComponentLife;
import bamboo.sample.tasksrouter.ITaskComponent;

/**
 * Created by tangshuai on 2018/3/16.
 */

@Service
public class TasksComponentOutput implements ITaskComponent, IAccount {


    public TasksComponentOutput() {
    }

    @Override
    public int getTaskCount() {
        return StitcherHelper.searchComponentApplication(TasksComponentLife.class).getTasksRepository().getTaskCount();
    }

    @Override
    public String getUserName() {
        return null;
    }
}
