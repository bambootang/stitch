package bamboo.sample.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import bamboo.component.StitcherHelper;
import bamboo.sample.account.ui.AccountInfoActivity;

/**
 * Created by tangshuai on 2018/3/17.
 */

public class TestActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity_test);

        StitcherHelper.onCreateDelay();
    }

    public void onAccountInfo(View view) {
        startActivity(new Intent(this, AccountInfoActivity.class));
    }
}
