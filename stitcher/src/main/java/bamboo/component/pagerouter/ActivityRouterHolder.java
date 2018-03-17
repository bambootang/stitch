package bamboo.component.pagerouter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Parcelable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import bamboo.component.StitcherHelper;


public class ActivityRouterHolder {


    public static void start(ActivityRouterRegistry activityRouterRegistry, ActivityPage page) {
        startActivityForResult(activityRouterRegistry, page, -1);
    }

    public static void startActivityForResult(ActivityRouterRegistry activityRouterRegistry, ActivityPage page, int requestCode) {
        if (page == null) {
            throw new IllegalArgumentException("page must not be null");
        }

        Method method = activityRouterRegistry.searchMethod(page.getClass());
        Object consumer = activityRouterRegistry.searchConsumer(page.getClass());
        if (method != null && consumer != null) {
            try {
                method.invoke(consumer, page);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (page.isAutoLink()) {
                startActivityForAnnotation(page, requestCode);
            } else if (isIntentAvailable(page.context, page.getTargetIntent())) {
                doStartActivity(page.getTargetIntent(), page, requestCode);
            } else {
                throw new IllegalArgumentException("unsupported ActivityPage");
            }
        }
    }

    private static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            list = packageManager.queryIntentActivities(intent,
                    PackageManager.MATCH_ALL);
        } else {
            list = packageManager.queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);
        }
        return list.size() > 0;
    }

    private static void startActivityForAnnotation(ActivityPage page, int requestCode) {
        assert page == null;
        PageConsumer consumer = page.getClass().getAnnotation(PageConsumer.class);
        String activityClass = null;
        if (consumer != null) {
            activityClass = consumer.clasz();
        }
        if (activityClass == null) {
            throw new IllegalArgumentException("class cannot be null when none of PageConsumer consume it");
        }
        Intent intent = page.getTargetIntent() != null ? page.getTargetIntent() : new Intent();
        intent.setClassName(page.context, activityClass);

        doStartActivity(intent, page, requestCode);
    }

    private static void doStartActivity(Intent intent, ActivityPage page, int requestCode) {
        if (page instanceof Parcelable) {
            intent.putExtra(page.getClass().getSimpleName(), (Parcelable) page);
        } else {
            intent.putExtra(page.getClass().getSimpleName(), page);
        }

        if (page.context instanceof Activity && requestCode != -1) {
            ((Activity) page.context).startActivityForResult(intent, requestCode);
        } else {
            page.context.startActivity(intent);
        }
    }
}
