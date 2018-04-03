package bamboo.stitch.router.compiler;

/**
 * Created by tangshuai on 2018/3/21.
 */

public class Name {

    public static String toUpperStart(String str) {
        if (str == null || "".equals(str.trim())) {
            return "";
        }
        String c = str.charAt(0) + "";
        return c.toUpperCase() + str.substring(1);
    }
}
