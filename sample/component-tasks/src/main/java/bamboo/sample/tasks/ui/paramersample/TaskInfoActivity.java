package bamboo.sample.tasks.ui.paramersample;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import bamboo.component.stitch.anno.AutoLink;
import bamboo.sample.tasks.R;
import bamboo.sample.tasksrouter.TaskInfoPage;

/**
 * Created by tangshuai on 2018/3/17.
 */

@AutoLink(TaskInfoPage.class)
public class TaskInfoActivity extends Activity {

    private TextView mTaskIdTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasks_activity_taskinfo);

        mTaskIdTextView = findViewById(R.id.tv_task_id);

        TaskInfoPage infoPage = (TaskInfoPage) getIntent().getSerializableExtra(TaskInfoPage.class.getSimpleName());
        mTaskIdTextView.setText(infoPage.taskId);
    }
}
