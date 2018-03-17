package bamboo.sample.tasks.component;

import android.content.Intent;

import bamboo.sample.tasks.ui.paramersample.TaskInfoActivity;
import bamboo.sample.tasksrouter.TaskInfoPage;

/**
 * Created by tangshuai on 2018/3/16.
 */

public class TasksPageConsumer {

    public void consume(TaskInfoPage page) {
        Intent intent = new Intent(page.context, TaskInfoActivity.class);
        intent.putExtra("TaskInfoPage", page);
        page.context.startActivity(intent);
    }
}
