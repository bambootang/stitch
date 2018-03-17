package bamboo.sample.tasksrouter;

import android.content.Context;

import bamboo.component.pagerouter.ActivityPage;
import bamboo.component.pagerouter.PageConsumer;


@PageConsumer(clasz = "bamboo.sample.tasks.ui.paramersample.TaskCountActivity")
public class TaskListPage extends ActivityPage {

    public TaskListPage(Context context) {
        super(context);
    }

}
