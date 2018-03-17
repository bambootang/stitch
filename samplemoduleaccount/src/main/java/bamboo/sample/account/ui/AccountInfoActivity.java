package bamboo.sample.account.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import bamboo.sample.account.R;
import bamboo.sample.account.component.ComponentInput;
import bamboo.sample.account.models.AccountRepository;

/**
 * Created by tangshuai on 2018/3/16.
 */
public class AccountInfoActivity extends Activity {

    private TextView mUserTextView;

    private TextView mTaskSizeTextView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.account_activity_accountinfo);


        mUserTextView = findViewById(R.id.tv_user);
        mTaskSizeTextView = findViewById(R.id.tv_tasks_size);

        mUserTextView.setText("userName: " + AccountRepository.getUserName());
        mTaskSizeTextView.setText("taskCount: " + ComponentInput.get().getTaskCount());

    }
}
