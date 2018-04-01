package bamboo.component;


import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

import java.util.List;

import bamboo.component.lifecycle.ComponentLife;
import bamboo.component.lifecycle.ComponentLifeRegistry;
import bamboo.component.lifecycle.IComponentLifeCycle;
import bamboo.component.page.ActivityPage;
import bamboo.component.page.ActivityHolder;
import bamboo.component.page.ActivityRegistry;
import bamboo.component.service.ServiceRegistry;

public final class StitcherHelper {

    private static ActivityRegistry activityRegistry = new ActivityRegistry();

    private static ComponentLifeRegistry componentLifeRegistry = new ComponentLifeRegistry();

    private static ServiceRegistry outputRouterRegistry = new ServiceRegistry();

    private static Application application;

    private StitcherHelper() {

    }


    public static Application getApplication() {
        return application;
    }

    static void setComponentLifeRegistry(ComponentLifeRegistry componentLifeRegistry) {
        StitcherHelper.componentLifeRegistry = componentLifeRegistry;
    }

    /**
     * 初始化框架，查询所有注册的组件，并完成注册
     *
     * @param application
     */
    public static void init(Application application) {
        StitcherHelper.application = application;
        componentLifeRegistry.registerFromManifest(application);
        registerComponent();
    }

    private static void registerComponent() {
        Iterable<ComponentLife> componentApplications = componentLifeRegistry.getAll();
        for (ComponentLife componentLife : componentApplications) {
            if (componentLife instanceof IRegistry) {
                ((IRegistry) componentLife).register(outputRouterRegistry, activityRegistry);
            }
        }
    }


    /**
     * 获取给定组件类型已注册的实例
     *
     * @param clasz
     * @param <T>
     * @return
     */
    public static <T extends ComponentLife> T searchComponentApplication(Class<T> clasz) {
        return componentLifeRegistry.search(clasz);
    }

    /**
     * 获取组件接口的实现类
     *
     * @param serviceClass 接口class
     * @param <T>          接口类型
     * @return 接口的实现类，如果没有注册返回null
     */
    public static <T> T searchService(Class<T> serviceClass) {
        return outputRouterRegistry.search(serviceClass);
    }

    /**
     * 获取组件接口的所有实现类
     *
     * @param serviceClass 接口class
     * @param <T>          接口类型
     * @return 接口的实现类，如果没有注册返回list.size=0
     */
    public static synchronized <T> List<T> searchAllServices(Class<T> serviceClass) {
        return outputRouterRegistry.searchAll(serviceClass);
    }

    /**
     * 调用所有组件的onCreate生命周期方法
     *
     * @see IComponentLifeCycle#onCreate()
     */
    public static void onCreate() {
        Iterable<ComponentLife> componentApplications = componentLifeRegistry.getAll();
        for (ComponentLife componentLife : componentApplications) {
            componentLife.onCreate();
        }
    }

    /**
     * @see IComponentLifeCycle#onCreateDelay()
     */
    public static void onCreateDelay() {
        Iterable<ComponentLife> componentApplications = componentLifeRegistry.getAll();
        for (ComponentLife componentLife : componentApplications) {
            componentLife.onCreateDelay();
        }
    }

    /**
     * 调用所有组件的attachBaseContext生命周期方法
     *
     * @see IComponentLifeCycle#attachBaseContext(Context)
     */
    public static void attachBaseContext(Context baseContext) {
        Iterable<ComponentLife> componentApplications = componentLifeRegistry.getAll();
        for (ComponentLife componentLife : componentApplications) {
            componentLife.attachBaseContext(baseContext);
        }
    }


    /**
     * 调用所有组件的onTrimMemory生命周期方法
     *
     * @see IComponentLifeCycle#onTrimMemory(int)
     */
    public static void onTrimMemory(int level) {
        Iterable<ComponentLife> componentApplications = componentLifeRegistry.getAll();
        for (ComponentLife componentLife : componentApplications) {
            componentLife.onTrimMemory(level);
        }
    }

    /**
     * 调用所有组件的onLowMemory()生命周期方法
     *
     * @see IComponentLifeCycle#onLowMemory()
     */
    public static void onLowMemory() {
        Iterable<ComponentLife> componentApplications = componentLifeRegistry.getAll();
        for (ComponentLife componentLife : componentApplications) {
            componentLife.onLowMemory();
        }
    }

    /**
     * 调用所有组件的onConfigurationChanged生命周期方法
     *
     * @see IComponentLifeCycle#onConfigurationChanged(Configuration)
     */
    public static void onConfigurationChanged(Configuration newConfig) {
        Iterable<ComponentLife> componentApplications = componentLifeRegistry.getAll();
        for (ComponentLife componentLife : componentApplications) {
            componentLife.onConfigurationChanged(newConfig);
        }
    }

    /**
     * 将ActivitPage包装成Intent返回
     *
     * @param page
     */
    public static Intent pack(ActivityPage page) {
        return ActivityHolder.pack(activityRegistry, page);
    }


    /**
     * 启动某个页面
     *
     * @param page
     */
    public static void start(ActivityPage page) {
        startActivityForResult(page, -1);
    }


    /**
     * 启动某个页面，并将最终结果通过onActivityResult返回到之前的页面。RequestCode == -1 时不会有回调
     *
     * @param page        页面路由[针脚]
     * @param requestCode RequestCode，-1表示不接收返回值
     */
    public static void startActivityForResult(ActivityPage page, int requestCode) {
        ActivityHolder.startActivityForResult(activityRegistry, page, requestCode);
    }
}
