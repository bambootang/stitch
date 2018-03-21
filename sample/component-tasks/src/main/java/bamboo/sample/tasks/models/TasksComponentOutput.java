package bamboo.sample.tasks.models;

import bamboo.component.StitcherHelper;
import bamboo.component.service.ServiceRegistry;
import bamboo.component.stitch.anno.Service;
import bamboo.sample.accountrouter.IAccount;
import bamboo.sample.tasks.component.TasksComponentApp;
import bamboo.sample.tasksrouter.ITaskComponent;
import bamboo.sample.tasks.models.TasksRepository;

/**
 * Created by tangshuai on 2018/3/16.
 */

@Service
public class TasksComponentOutput implements ITaskComponent, IAccount {


    public TasksComponentOutput() {
    }

    @Override
    public int getTaskCount() {
        return StitcherHelper.searchComponentApplication(TasksComponentApp.class).getTasksRepository().getTaskCount();
    }

    @Override
    public String getUserName() {
        return null;
    }
}
