package bamboo.component.stitch.compiler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by tangshuai on 2018/3/19.
 */

public class ActivityBinding {

    String targetActivity;

    String pageLink;


    public static final void test(String test) {
        System.out.println(test);
    }

    public static final void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = ActivityBinding.class.getMethod("test", String.class);
        method.invoke(null, "hello");
        method.invoke(null, "hello1");
        method.invoke(null, "hello2");
        method.invoke(null, "hello3");
        method.invoke(null, "hello4");
        method.invoke(null, "hello5");
        method.invoke(null, "hello6");
        Method method2 = ActivityBinding.class.getMethod("test", String.class);
        Method method3 = ActivityBinding.class.getMethod("test", String.class);
        System.out.println(method.toGenericString());
        System.out.println(method2.hashCode());
        System.out.println(method3.hashCode());
        System.out.println(method3 == method2);
        System.out.println(method3 == method);
    }
}
