package bamboo.component.actiontest;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;

import bamboo.component.stitch.anno.Exported;


/**
 * Created by tangshuai on 2018/3/17.
 */

@Exported(ActionTestPage.class)
public class ActionTestActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new Button(this));
    }
}
