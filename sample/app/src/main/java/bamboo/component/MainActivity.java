package bamboo.component;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import bamboo.component.actiontest.ActionTestPage;
import bamboo.component.intenttest.IntentTestPage;
import bamboo.component.lifecycle.ComponentLife;
import bamboo.component.paramtest.ParcelableTestPage;
import bamboo.component.paramtest.SerializableTestPage;
import bamboo.sample.account.component.AccountComponentLife;
import bamboo.sample.accountrouter.AccountInfoPage;
import bamboo.sample.tasks.component.TasksComponentLife;
import bamboo.sample.tasksrouter.TaskInfoPage;
import bamboo.sample.tasksrouter.TaskListPage;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StitcherHelper.onCreateDelay();
        ComponentLife componentApplication = StitcherHelper.searchComponentApplication(ComponentLife.class);
        System.out.println(componentApplication);
        System.out.println(StitcherHelper.searchComponentApplication(AccountComponentLife.class));
        System.out.println(StitcherHelper.searchComponentApplication(TasksComponentLife.class));
    }

    public void onTaskList(View view) {
        StitcherHelper.start(new TaskListPage(this));
    }

    public void onAccountInfo(View view) {
        StitcherHelper.start(new AccountInfoPage(this));
    }

    public void onTaskInfo(View view) {
        StitcherHelper.start(new TaskInfoPage(this, "main-" + System.currentTimeMillis()));
    }

    public void onClearTask(View view) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        IntentTestPage page = new IntentTestPage(this);
        page.setTargetIntent(intent);
        StitcherHelper.start(page);
    }

    public void onClearTop(View view) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        IntentTestPage page = new IntentTestPage(this);
        page.setTargetIntent(intent);
        StitcherHelper.start(page);
    }

    public void onParamersTrancefer(View view) {
        ParcelableTestPage page = new ParcelableTestPage(this);
        page.setParam1("this is text1");
        page.setParam2("this is text2");
        StitcherHelper.start(page);
    }

    public void onSerializable(View view) {
        SerializableTestPage page = new SerializableTestPage(this);
        page.setParam1("this is text1");
        page.setParam2("this is text2");
        StitcherHelper.start(page);
    }

    public void onActionTest(View view) {
        ActionTestPage page = new ActionTestPage(this);
        Intent targetIntent = new Intent();
        targetIntent.setAction("bamboo.sample.actiontest");
        targetIntent.addCategory(Intent.CATEGORY_DEFAULT);
        page.setTargetIntent(targetIntent);
        StitcherHelper.start(page);
    }
}
