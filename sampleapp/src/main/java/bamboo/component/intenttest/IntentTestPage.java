package bamboo.component.intenttest;

import android.content.Context;

import bamboo.component.pagerouter.ActivityPage;
import bamboo.component.pagerouter.PageConsumer;

/**
 * Created by tangshuai on 2018/3/17.
 */

@PageConsumer(clasz = "bamboo.component.intenttest.IntentTestActivity")
public class IntentTestPage extends ActivityPage {

    public IntentTestPage(Context context) {
        super(context);
    }

    public String text1;

    public String text2;

}
