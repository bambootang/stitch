package bamboo.component.lifecycle;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ComponentAppResolve {

    private static boolean inited = false;

    private static final List<ComponentApplication> applibraries = new ArrayList<>();

    private static final String COMPONENt_META_NAME = "ComponentApplication";

    static List<ComponentApplication> findAllAppLibrary(Application context) {
        if (!inited) {
            synchronized (ComponentAppResolve.class) {
                if (!inited) {
                    inited = true;
                    searchAllLibraryApplication(context);
                    Collections.sort(applibraries, new ComponentLevelComparator());
                }
            }
        }
        return applibraries;
    }

    /**
     * 从Manifest文件中查找LibraryApplication
     *
     * @param context
     */
    private static void searchAllLibraryApplication(Application context) {
        ApplicationInfo ai;
        try {
            ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            Set<String> keys = bundle.keySet();
            for (String key : keys) {
                String value = bundle.getString(key);
                if (value != null && value.equals(COMPONENt_META_NAME)) {
                    try {
                        Class aClass = Class.forName(key);
                        addApplibrary(aClass, context);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void addApplibrary(Class<? extends ComponentApplication> applibraryClass, Application application) {
        try {
            ComponentApplication app = applibraryClass.newInstance();
            app.setApplication(application);
            applibraries.add(app);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
