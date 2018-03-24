package bamboo.component.page;


import android.content.Context;
import android.content.Intent;

import java.io.Serializable;

/**
 * 每一个对其他module公开的Activity都需要有一个与之对应的ActivityPage
 * <p>
 * ActivityPage 相当于是注册表中的注册信息，
 * 该注册信息包含了页面名称、上下文、以及在启动时的额外配置
 * <p>
 * 同时为了节约类的数量，ActivityPage本身也是参数传递的可序列化Bean类
 * 在定义自己的ActivityPage时，我们采用实现类的类名（simplename）作为参数的key
 * <p>
 * 不想进行序列化的字段需要用transient进行描述，同时你也可以使用Parcelable的方式
 * 如果自定义的ActivityPage实现了Parcelable接口，框架会使用Parcelable方式进行数据传递，
 * 所以在接收数据时，需要自己注意这一块.
 */
public abstract class ActivityPage implements Serializable {

    public transient final Context context;

    private transient Intent targetIntent;

    public ActivityPage(Context context) {
        this.context = context;
    }

    public ActivityPage(Context context, Intent targetIntent) {
        this.context = context;
        this.targetIntent = targetIntent;
    }

    public Intent getTargetIntent() {
        return targetIntent;
    }

    public void setTargetIntent(Intent targetIntent) {
        this.targetIntent = targetIntent;
    }

}
