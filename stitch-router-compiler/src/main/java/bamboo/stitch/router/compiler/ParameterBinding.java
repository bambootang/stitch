package bamboo.stitch.router.compiler;

/**
 * Created by tangshuai on 2018/4/1.
 */

public class ParameterBinding {

    //字段名称
    String name;

    //字段对应的method的参数
    String[] paramerTypes;

    String[] paramerNames;

    //字段对应的方法名称
    String methodName;

    //字段注入类型，0=field，1=method;
    int type = 0;

}
