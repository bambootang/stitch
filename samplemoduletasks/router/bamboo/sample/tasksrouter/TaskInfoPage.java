package bamboo.sample.tasksrouter;

import android.content.Context;

import bamboo.component.pagerouter.ActivityPage;

/**
 * Created by tangshuai on 2018/3/17.
 */

public class TaskInfoPage extends ActivityPage {

    public final String taskId;

    public TaskInfoPage(Context context, String taskId) {
        super(context);
        this.taskId = taskId;
    }

}
