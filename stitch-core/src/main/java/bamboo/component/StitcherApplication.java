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
        onStitchOnCreate();
    }

    protected void attachBaseContext(Context baseContext) {
        super.attachBaseContext(baseContext);
        attachStitchBaseContext(baseContext);
    }


    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        onStitchTrimMemory(level);
    }

    public void onLowMemory() {
        super.onLowMemory();
        onStitchLowMemory();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        onStitchConfigurationChanged(newConfig);
    }


    protected void onStitchOnCreate() {
        StitcherHelper.onCreate();
    }

    protected void attachStitchBaseContext(Context baseContext) {
        StitcherHelper.init(this);
        StitcherHelper.attachBaseContext(baseContext);
    }


    protected void onStitchTrimMemory(int level) {
        StitcherHelper.onTrimMemory(level);
    }

    protected void onStitchLowMemory() {
        StitcherHelper.onLowMemory();
    }

    protected void onStitchConfigurationChanged(Configuration newConfig) {
        StitcherHelper.onConfigurationChanged(newConfig);
    }

}
