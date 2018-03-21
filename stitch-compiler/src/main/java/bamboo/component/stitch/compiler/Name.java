package bamboo.component.stitch.compiler;

/**
 * Created by tangshuai on 2018/3/21.
 */

public class Name {

    public static String toCamlStyle(String str) {
        if (str == null || "".equals(str.trim())) {
            return "";
        }
        int length = str.length();
        boolean continuous = false;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c = str.charAt(i);
            if (('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z')) {
                sb.append(continuous ? c : ((c + "").toUpperCase()));
                continuous = true;
            } else {
                continuous = false;
            }
        }
        return sb.toString();
    }
}
