package bamboo.component;

/**
 * Created by tangshuai on 2018/3/28.
 */

public class Asserts {

    public static void assertTrue(boolean op, String warn) {
        if (!op) {
            throw new IllegalArgumentException(warn);
        }
    }

    public static void assertNotNull(Object ob, String warn) {
        if (ob == null) {
            throw new IllegalArgumentException(warn);
        }
    }

}
