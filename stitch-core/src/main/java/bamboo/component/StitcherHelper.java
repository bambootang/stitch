package bamboo.component;


import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import java.util.Collection;

import bamboo.component.lifecycle.ComponentApplication;
import bamboo.component.lifecycle.ComponentLifeRegistry;
import bamboo.component.lifecycle.IComponentLifeCycle;
import bamboo.component.page.ActivityPage;
import bamboo.component.page.PageHolder;
import bamboo.component.page.PageRegistry;
import bamboo.component.service.ServiceRegistry;

public final class StitcherHelper {

    private static PageRegistry pageRegistry = new PageRegistry();

    private static ComponentLifeRegistry componentLifeRegistry = new ComponentLifeRegistry();

    private static ServiceRegistry outputRouterRegistry = new ServiceRegistry();

    private StitcherHelper() {
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
        componentLifeRegistry.registerFromManifest(application);
    }


    /**
     * 获取给定组件类型已注册的实例
     *
     * @param clasz
     * @param <T>
     * @return
     */
    public static <T extends ComponentApplication> T searchComponentApplication(Class<T> clasz) {
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
     * 调用所有组件的onCreate生命周期方法
     *
     * @see IComponentLifeCycle#onCreate()
     */
    public static void onCreate() {
        Iterable<ComponentApplication> componentApplications = componentLifeRegistry.getAll();
        for (ComponentApplication componentApplication : componentApplications) {
            componentApplication.onCreate();
        }
    }

    /**
     * @see IComponentLifeCycle#onCreateDelay()
     */
    public static void onCreateDelay() {
        Iterable<ComponentApplication> componentApplications = componentLifeRegistry.getAll();
        for (ComponentApplication componentApplication : componentApplications) {
            componentApplication.onCreateDelay();
            if (componentApplication instanceof IRegistry) {
                ((IRegistry) componentApplication).register(outputRouterRegistry, pageRegistry);
            }
        }
    }

    /**
     * 调用所有组件的attachBaseContext生命周期方法
     *
     * @see IComponentLifeCycle#attachBaseContext(Context)
     */
    public static void attachBaseContext(Context baseContext) {
        Iterable<ComponentApplication> componentApplications = componentLifeRegistry.getAll();
        for (ComponentApplication componentApplication : componentApplications) {
            componentApplication.attachBaseContext(baseContext);
        }
    }


    /**
     * 调用所有组件的onTrimMemory生命周期方法
     *
     * @see IComponentLifeCycle#onTrimMemory(int)
     */
    public static void onTrimMemory(int level) {
        Iterable<ComponentApplication> componentApplications = componentLifeRegistry.getAll();
        for (ComponentApplication componentApplication : componentApplications) {
            componentApplication.onTrimMemory(level);
        }
    }

    /**
     * 调用所有组件的onLowMemory()生命周期方法
     *
     * @see IComponentLifeCycle#onLowMemory()
     */
    public static void onLowMemory() {
        Iterable<ComponentApplication> componentApplications = componentLifeRegistry.getAll();
        for (ComponentApplication componentApplication : componentApplications) {
            componentApplication.onLowMemory();
        }
    }

    /**
     * 调用所有组件的onConfigurationChanged生命周期方法
     *
     * @see IComponentLifeCycle#onConfigurationChanged(Configuration)
     */
    public static void onConfigurationChanged(Configuration newConfig) {
        Iterable<ComponentApplication> componentApplications = componentLifeRegistry.getAll();
        for (ComponentApplication componentApplication : componentApplications) {
            componentApplication.onConfigurationChanged(newConfig);
        }
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
        PageHolder.startActivityForResult(pageRegistry, page, requestCode);
    }
}
