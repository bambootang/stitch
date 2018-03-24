package bamboo.component;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by tangshuai on 2018/3/16.
 */

public class StitcherApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        StitcherHelper.onCreate();
    }


    public void attachBaseContext(Context baseContext) {
        super.attachBaseContext(baseContext);
        StitcherHelper.init(this);
        StitcherHelper.attachBaseContext(baseContext);
    }


    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        StitcherHelper.onTrimMemory(level);
    }

    public void onLowMemory() {
        super.onLowMemory();
        StitcherHelper.onLowMemory();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        StitcherHelper.onConfigurationChanged(newConfig);
    }

}
