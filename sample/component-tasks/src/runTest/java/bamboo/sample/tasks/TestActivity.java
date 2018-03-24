package bamboo.sample.tasks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import bamboo.component.StitcherHelper;
import bamboo.sample.tasks.ui.paramersample.TaskCountActivity;
import bamboo.sample.tasksrouter.TaskInfoPage;

/**
 * Created by tangshuai on 2018/3/17.
 */

public class TestActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasks_activity_test);
        StitcherHelper.onCreateDelay();
    }


    public void onTasksList(View view) {
        startActivity(new Intent(this, TaskCountActivity.class));
    }

    public void onTaskInfo(View view) {
        StitcherHelper.start(new TaskInfoPage(this, "testId-" + System.currentTimeMillis()));
    }
}
