package bamboo.component.pagerouter;

import android.content.Context;

public class TestPageConsume {

    public String msg;

    /**
     * 不匹配，因为是BasePage基类。
     *
     * @param activityPage
     */
    public void consume(ActivityPage activityPage) {
        msg = "ActivityPage";
    }

    /**
     * 匹配
     *
     * @param testPage
     */
    public void consume(TestPage testPage) {
        msg = "TestPage";

    }

    /**
     * 匹配
     *
     * @param testPage2
     */
    public void consume(TestPage2 testPage2) {
        msg = "TestPage2";
    }

    /**
     * 不匹配，因为有两个参数
     *
     * @param testPage2
     * @param testPage
     */
    public void consume(TestPage2 testPage2, TestPage testPage) {
        msg = "TestPage2 TestPage";
    }

    public void consume(Object object) {
        msg = "object";
    }

    public static class TestPage extends ActivityPage {

        public TestPage(Context context) {
            super(context);
        }
    }

    public static class TestPage2 extends ActivityPage {

        public TestPage2(Context context) {
            super(context);
        }
    }

    public static class TestPage3 extends ActivityPage {

        public TestPage3(Context context) {
            super(context);
        }
    }
}
