package bamboo.component.lifecycle;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

/**
 * 组件生命周期基类，组件module里面如果有需要在app启动时进行一些初始化工作
 * 需要继承该类，并将其添加到module对应的AndroidManifest.xml中，
 * <p>
 * 像下面这样
 * <p>
 * &#x3C;application&#x3E;
 * ...
 * &#x3C;meta-data
 * android:name="bamboo.sample.tasks.component.TasksComponentApp"
 * android:value="ComponentLife" /&#x3E;
 * &#x3C;/application&#x3E;
 * <p>
 * 其中ComponentApplication作为value，你的自定义的类TasksComponentApp作为name
 */
public class ComponentLife implements IComponentLifeCycle {

    private Application application;

    public ComponentLife() {
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public int level() {
        return ComponentPriority.LEVEL_LOW;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onCreateDelay() {

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
