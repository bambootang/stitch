package bamboo.component.paramtest;

import android.content.Context;

import bamboo.component.page.ActivityPage;


/**
 * Created by tangshuai on 2018/3/17.
 */

public class SerializableTestPage extends ActivityPage {

    public SerializableTestPage(Context context) {
        super(context);
    }

    private String param1;

    private transient String param2;

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }
}
