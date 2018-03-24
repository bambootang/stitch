package bamboo.sample.tasks.ui.paramersample;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import bamboo.component.StitcherHelper;
import bamboo.component.stitch.anno.Exported;
import bamboo.sample.tasks.R;
import bamboo.sample.tasks.component.ComponentInput;
import bamboo.sample.tasks.component.TasksComponentLife;
import bamboo.sample.tasks.models.TasksRepository;
import bamboo.sample.tasksrouter.TaskListPage;


@Exported(TaskListPage.class)
public class TaskCountActivity extends Activity {

    private TextView mUserTextView;

    private TextView mTaskSizeTextView;

    private TasksRepository tasksRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tasks_activity_taskcount);

        tasksRepository = StitcherHelper.searchComponentApplication(TasksComponentLife.class).getTasksRepository();

        mUserTextView = findViewById(R.id.tv_user);
        mTaskSizeTextView = findViewById(R.id.tv_tasks_size);

        refreshView();

    }

    private void refreshView() {
        mUserTextView.setText("userName: " + ComponentInput.get().getUserName());
        mTaskSizeTextView.setText("tasksize: " + tasksRepository.getTaskCount() + "");
    }

    public void onTaskAdd(View view) {
        tasksRepository.addTask(System.currentTimeMillis() + "");
        refreshView();
    }
}
