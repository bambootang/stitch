package bamboo.component.intenttest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import bamboo.component.R;
import bamboo.component.StitcherHelper;
import bamboo.component.stitch.anno.AutoLink;


/**
 * Created by tangshuai on 2018/3/17.
 */

@AutoLink(IntentTestPage.class)
public class IntentTestActivity extends Activity {

    private TextView mThisTextView;

    private TextView mTimeTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        setTitle(getClass().getSimpleName());

        mThisTextView = findViewById(R.id.tv_this);
        mTimeTextView = findViewById(R.id.tv_time);

        mThisTextView.setText(this.toString());
        mTimeTextView.append(System.currentTimeMillis() + "\n");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mThisTextView.append("\nonNewIntent");
        mTimeTextView.append(System.currentTimeMillis() + "\n");
    }



}
