package bamboo.component.pagerouter;

import java.lang.reflect.Method;
import java.util.HashMap;

public final class PageResolveHelper {


    /**
     * 解析PageConsumer对象，通过反射查找所有满足以下条件的方法，并将其作为跳转路由链路
     * 0、方法作用域限制符为 public
     * 1、有且仅有1个参数类型
     * 2、参数类型必须继承自 BasePage且不是BasePage
     *
     * @param pageConsumer 页面跳转事件的消费者
     * @param methodMap    页面事件与消费方法的链路
     * @param holderMap    页面事件与消费者对象的链路
     */
    public static void resolvePageConsumer(Object pageConsumer, HashMap<Class, Method> methodMap, HashMap<Class, Object> holderMap) {
        Method[] methods = pageConsumer.getClass().getMethods();
        for (Method method : methods) {
            Class[] parameterTypes = method.getParameterTypes();
            if (parameterTypes != null
                    && parameterTypes.length == 1
                    && parameterTypes[0].getSuperclass() == ActivityPage.class) {
                methodMap.put(parameterTypes[0], method);
                holderMap.put(parameterTypes[0], pageConsumer);
            }
        }
    }


}
