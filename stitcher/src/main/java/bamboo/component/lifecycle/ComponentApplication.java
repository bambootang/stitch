package bamboo.component.lifecycle;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import bamboo.component.pagerouter.ActivityRouterRegistry;
import bamboo.component.datarouter.ComponentRouterRegistry;

/**
 * 组件生命周期基类，组件module里面如果有需要在app启动时进行一些初始化工作
 * 需要继承该类，并将其添加到module对应的AndroidManifest.xml中，
 *
 * 像下面这样
 *
 * <application>
 *     ...
 *      <meta-data
 *              android:name="bamboo.sample.tasks.component.TasksComponentApp"
 *              android:value="ComponentApplication" />
 * </application>
 *
 * 其中ComponentApplication作为value，你的自定义的类TasksComponentApp作为name
 *
 */
public class ComponentApplication implements IComponentLifeCycle {

    private Application application;

    public ComponentApplication() {
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    @Override
    public int level() {
        return ComponentPriority.LEVEL_LOW;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onCreateDelay(ComponentRouterRegistry routerRegistry, ActivityRouterRegistry activityRouterRegistry) {

    }

    @Override
    public void attachBaseContext(Context baseContext) {

    }

    @Override
    public final Application getApplication() {
        return application;
    }

    @Override
    public void onTrimMemory(int level) {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }

    @Override
    public void onLowMemory() {

    }

}
