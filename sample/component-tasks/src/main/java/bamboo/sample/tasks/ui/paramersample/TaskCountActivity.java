package bamboo.sample.tasks.ui.paramersample;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import bamboo.component.StitcherHelper;
import bamboo.component.stitch.anno.Exported;
import bamboo.component.testrouter.ActivityPageManager;
import bamboo.component.testrouter.ServiceManager;
import bamboo.sample.accountrouter.AccountInfoPage;
import bamboo.sample.accountrouter.IAccount;
import bamboo.sample.tasks.R;
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
        mUserTextView.setText("userName: " + ServiceManager.getUserName());
        mTaskSizeTextView.setText("tasksize: " + tasksRepository.getTaskCount() + "");
        List<IAccount> iAccounts = StitcherHelper.searchAllServices(IAccount.class);
        for (IAccount iAccount : iAccounts) {
            mTaskSizeTextView.append("\n" + iAccount.getUserName());
            mTaskSizeTextView.append("\n" + iAccount.toString());
            mTaskSizeTextView.append("\n" + iAccount.getClass());
        }
    }

    public void onTaskAdd(View view) {
        tasksRepository.addTask(System.currentTimeMillis() + "");
        refreshView();
    }
}
