package bamboo.component.paramtest;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import bamboo.component.R;

/**
 * Created by tangshuai on 2018/3/17.
 */

public class SerializableTestActivity extends Activity {

    private TextView mParam1TextView;

    private TextView mParam2TextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_param_test);
        setTitle(getClass().getSimpleName());

        mParam1TextView = findViewById(R.id.tv_param1);
        mParam2TextView = findViewById(R.id.tv_param2);

        SerializableTestPage testPage = (SerializableTestPage) getIntent().getSerializableExtra("SerializableTestPage");
        if (testPage != null) {
            mParam1TextView.setText(testPage.getParam1() + "");
            mParam2TextView.setText(testPage.getParam2() + "");
        }
    }
}
