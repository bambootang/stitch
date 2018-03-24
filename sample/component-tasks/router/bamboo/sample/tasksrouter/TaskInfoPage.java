package bamboo.sample.tasksrouter;

import android.content.Context;

import java.io.Serializable;

import bamboo.component.page.ActivityPage;

/**
 * Created by tangshuai on 2018/3/17.
 */

public class TaskInfoPage extends ActivityPage implements Serializable{

    public final String taskId;

    public TaskInfoPage(Context context, String taskId) {
        super(context);
        this.taskId = taskId;
    }

}
