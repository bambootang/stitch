package bamboo.stitch.router.compiler;

/**
 * Created by tangshuai on 2018/4/1.
 */

public class ActivityDelegate {

    public static final String CLASS = "" +
            "    public static class ActivityPageDelegate<T extends ActivityPage> {\n" +
            "    \n" +
            "        T activityPage;\n" +
            "    \n" +
            "        public ActivityPageDelegate(T activityPage) {\n" +
            "            this.activityPage = activityPage;\n" +
            "        }\n" +
            "    \n" +
            "        public T getActivityPage() {\n" +
            "            return activityPage;\n" +
            "        }\n" +
            "    \n" +
            "        public ActivityPageDelegate setTargetIntent(Intent intent) {\n" +
            "            activityPage.setTargetIntent(intent);\n" +
            "            return this;\n" +
            "        }\n" +
            "    \n" +
            "        public ActivityPageDelegate setRequestCode(int requestCode) {\n" +
            "            activityPage.setRequestCode(requestCode);\n" +
            "            return this;\n" +
            "        }\n" +
            "    \n" +
            "        public Intent pack() {\n" +
            "            return StitcherHelper.pack(activityPage);\n" +
            "        }\n" +
            "    \n" +
            "        public void start() {\n" +
            "            StitcherHelper.start(activityPage);\n" +
            "        }\n" +
            "    \n" +
            "        public void startForResult() {\n" +
            "            StitcherHelper.startActivityForResult(activityPage, activityPage.getRequestCode());\n" +
            "        }\n" +
            "    \n" +
            "    }\n";

    public static String getActivityDelegateClassCode(String packageName) {
        return String.format(CLASS, packageName);
    }
}
