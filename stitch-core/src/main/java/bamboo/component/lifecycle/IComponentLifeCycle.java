package bamboo.component.lifecycle;

import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;

public interface IComponentLifeCycle extends ComponentCallbacks2 {

    /**
     * 表示组件的优先级，组件初始化时以该方法的返回值进行排序
     * 优先级越高的越先进行初始化，
     * 我们将优先级默认分为3级：HIGH、MID、LOW
     * 数字越大，优先级越低，如果在3级无法满足的情况下，可适当调整该值。
     * <p>
     * 举例：
     * <p>
     * 因为Push组件需要获取Account的信息，所以Push组件必须要在Account组件初始化之后才能初始化
     * 我们可以设置Account组件的层级为{@link ComponentPriority#LEVEL_MID}
     * 设置PUSH组件的层级为{@link ComponentPriority#LEVEL_LOW}.
     * <p>
     * 而我们的路由组件是所有其他组件都需要依赖的组件，所以我们设置为{@link ComponentPriority#LEVEL_HIGH}
     *
     * @return {@link ComponentPriority#LEVEL_HIGH ,ComponentPriority#LEVEL_MID ,ComponentPriority#LEVEL_LOW}
     */
    int level();

    /**
     * 在Application的onCreate生命周期方法中会调用该方法实现组件的生命周期调用
     * <p>
     * 调用顺序依赖于{@link #level()}的值
     */
    void onCreate();

    /**
     * 某些组件初始化并不需要在Application初始化时进行，我们可以对其进行延后处理，来提升App的启动速度
     *
     */
    void onCreateDelay();

    /**
     * 在Application的attachBaseContext生命周期方法中会调用该方法实现组件的生命周期调用
     *
     * @param baseContext
     */
    void attachBaseContext(Context baseContext);

    /**
     * 获取APP的Application对象
     *
     * @return
     */
    Application getApplication();


    void onTrimMemory(int level);

    void onLowMemory();

    void onConfigurationChanged(Configuration newConfig);

}
