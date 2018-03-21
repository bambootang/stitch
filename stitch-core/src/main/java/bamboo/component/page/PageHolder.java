package bamboo.component.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Parcelable;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;


public class PageHolder {


    public static void start(PageRegistry pageRegistry, ActivityPage page) {
        startActivityForResult(pageRegistry, page, -1);
    }

    public static void startActivityForResult(PageRegistry pageRegistry, ActivityPage page, int requestCode) {
        if (page == null) {
            throw new IllegalArgumentException("page must not be null");
        }
        startActivityForRegistry(pageRegistry, page, requestCode);
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

    private static void startActivityForRegistry(PageRegistry pageRegistry, ActivityPage page, int requestCode) {
        assert page == null;
        String activityClass = pageRegistry.search(page.getClass().getName());
        if (activityClass == null) {
            System.err.println("无效的页面跳转");
            return;
        }
        Intent intent = page.getTargetIntent() != null ? page.getTargetIntent() : new Intent();
        intent.setClassName(page.context, activityClass);

        doStartActivity(intent, page, requestCode);
    }

    private static void doStartActivity(Intent intent, ActivityPage page, int requestCode) {
        if (page instanceof Parcelable) {
            intent.putExtra(page.getClass().getSimpleName(), (Parcelable) page);
        } else {
            try {
                intent.putExtra(page.getClass().getSimpleName(), page);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!isIntentAvailable(page.context, intent)) {
            System.err.println("无效的页面跳转");
            return;
        }

        if (page.context instanceof Activity && requestCode != -1) {
            ((Activity) page.context).startActivityForResult(intent, requestCode);
        } else {
            page.context.startActivity(intent);
        }
    }
}
