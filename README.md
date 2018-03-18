# stitcher
Android组件化开发框架

框架包含3个部分：组件生命周期管理、页面交互、数据交互。我们依次对其进行解析。

### 依赖
```
//AS 3.0之前使用
compile 'bamboo.component:stitcher:1.0'
//AS 3.0之后建议使用
implementation 'bamboo.component:stitcher:1.0'
```

### 组件生命周期管理
每个组件相当于1个Module，许多组件的业务是需要在App启动时进行初始化的，比如运营商支付sdk基本都需要在Application的onCreate方法中进行初始化。

[stitcher](https://github.com/bambootang/stitcher) 框架中，组件将自己注入到[stitcher](https://github.com/bambootang/stitcher) 框架中，主工程再通过[stitcher](https://github.com/bambootang/stitcher)框架对组件生命周期进行统一管理。

具体使用方法：
##### 1. 继承ComponentApplication
```java
public abstract class ComponentApplication {
    //Application对象注入
    public void setApplication(Application application)；

    //控制组件的初始化顺序，参看ComponentPriority
    public int level();

    //代理Application的OnCreate方法
    public void onCreate();

    //延迟初始化生命周期，用来注入页面以及数据交互接口
    public void onCreateDelay(ComponentRouterRegistry routerRegistry, ActivityRouterRegistry activityRouterRegistry)；

    //代理Application的attachBaseContext方法
    public void attachBaseContext(Context baseContext) ;

    //代理Application的onTrimMemory方法
    public void onTrimMemory(int level) ;

    //代理Application的onConfigurationChanged方法
    public void onConfigurationChanged(Configuration newConfig);

    //代理Application的onLowMemory方法
    public void onLowMemory();
}
```
ComponentApplication是组件生命周期的代理类，代理了Application的常用关键方法。如果组件在App启动时进行某些初始化或需要监听生命周期，通过ComponentApplication即可实现。
##### 2. 在Module的AndroidManifest.xml中进行配置
```xml
<application>
    ...
    <meta-data
        android:name="bamboo.sample.account.component.AccountComponentApp"
        android:value="ComponentApplication" />
</application>
```
> 特别要注意：meta-data的value是ComponentApplication，name才是我们module的代理Application类。不要搞反了。
##### 3. 主工程里面的自定义Application修改
在主工程Application里面我们需要主动调用组件的代理Application的方法，[stitcher](https://github.com/bambootang/stitcher)提供了两种方式：

**1.  直接继承StitcherApplication**
```java
public class MainApplication extends StitcherApplication {
}
```
**2.  通过StitcherHelper调用组件的生命周期。**
参考StitcherApplication。
```java
public class StitcherApplication{
    public void onCreate() {
        super.onCreate();
        StitcherHelper.onCreate();
    }

    public void onCreateDelay() {
        StitcherHelper.onCreateDelay();
    }

    public void attachBaseContext(Context baseContext) {
        super.attachBaseContext(baseContext);
        StitcherHelper.init(this);
        StitcherHelper.attachBaseContext(baseContext);
    }

    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        StitcherHelper. onTrimMemory(level);
    }

    public void onLowMemory() {
        super.onLowMemory();
        StitcherHelper.onLowMemory();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        StitcherHelper.onConfigurationChanged(newConfig);
    }
}
```
通过上面几步，就能对组件的生命周期进行管理。

在使用页面交互以及数据交互之前我们先思考一个问题，这个问题是：
> 组件之间不能互相依赖，那如果要获取数据，我们就必须要借助于第三方Module进行中转。我们要怎么设计这个Module更方便呢？

没错，我们确实需要一个额外的Module来做交互中转，也就是路由Module，如果以开发人员的角度来看，这个Module应该要具备以下几个因素：
1.方便Module管理自己的接口。
2.其他Module最好能实时看到我们的修改。

所以在介绍交互功能之前，我们需要先来实现这个路由Module的配置。
#### 公用路由Module配置
##### 1. 创建一个Module：sampleouter
##### 2. 修改sampleouter的build.gradle文件，在里面加入以下代码：
这一步的目的是为了把所有Module的projectDir的router文件夹都加入到sampleouter的源码文件夹中，这样一来，我们就能在自己的Module的router文件夹内管理自己对外公开的接口以及页面。
```groovy
android {
    ...
    sourceSets {
        main {
            //将所有module里的router文件夹都作为路由module的源码文件夹，方便Module开发时的方便
            ArrayList<String> strings = new ArrayList<String>()
            File[] modules = rootDir.listFiles(new FileFilter() {
                boolean accept(File pathname) {
                    return (pathname.isDirectory()
                            && pathname.name != "gradle"
                            && pathname.name != "build"
                            && !pathname.name.startsWith("."))
                }
            })
            for (File f : modules) {
                strings.add(f.absolutePath + File.separator + "router")
            }
            //不要忘了把原始的源码目录添加进来
            strings.addAll(java.srcDirs)
            java.srcDirs = strings
        }
    }
}
```
配置完后，点一下refresh按钮，在Module里面创建router文件夹你会看到这样的效果。
![image.png](https://upload-images.jianshu.io/upload_images/4290785-d9b40ab7e37fb2d4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


##### 3.在Module中配置samplerouter的依赖
```groovy
implementation 'bamboo.component:stitcher:1.0'
implementation project(":samplerouter")
```

OK，sampleouter Module就配置好了，现在我们继续看页面交互要怎么实现。

### 页面交互
我们在讲述组件的生命周期管理时，可能你已经看到了组件生命周期代理类内有一个方法
```java
//延迟初始化生命周期，用来注入页面以及数据交互接口
public void onCreateDelay(ComponentRouterRegistry routerRegistry, ActivityRouterRegistry activityRouterRegistry)；
```
其中ActivityRouterRegistry就是我们进行页面交互的注册表，我们只需要将我们需要公开的页面注入到这里面就可以进行交互了。具体的实现步骤：
##### 1. 在router文件夹里创建一个TaskInfoPage.class并继承ActivityPage
```java
package bamboo.sample.tasksrouter;

//每个对外公开的页面都对应一个ActivityPage的子类
public class TaskInfoPage extends ActivityPage {
    public final String taskId;
    public TaskInfoPage(Context context, String taskId) {
        super(context);
        this.taskId = taskId;
    }
}
```
##### 2. 创建一个TasksPageConsumer.class
```java
public class TasksPageConsumer {
    //这个方法将会与TaskInfoPage进行连接。
    //所有的TaskInfoPage的页面交互请求都会最终调用到该方法中。
    public void consume(TaskInfoPage page) {
        Intent intent = new Intent(page.context, TaskInfoActivity.class);
        intent.putExtra("TaskInfoPage", page);
        page.context.startActivity(intent);
    }
}
```
##### 3. 在Module的ComponentApplication实现类中注册
```java
public class TasksComponentApp extends ComponentApplication {

    public void onCreateDelay(ComponentRouterRegistry routerRegistry, ActivityRouterRegistry activityRouterRegistry) {
        //将TasksPageConsumer注入到页面路由注册表中
        activityRouterRegistry.regiest(new TasksPageConsumer());
    }
```
##### 4. 交互调用
```java
//在需要调用TaskInfoPage的地方通过StitcherHelper使用
StitcherHelper.start(new TaskInfoPage(this, "taskId"));
StitcherHelper.start(new TaskInfoPage(this, "taskId"),1000/*requestCode*/);
```
##### 5. 更简单的使用方式(PageConsumer注解)
实际上我们还有简单的方式进行页面注入，为什么我要先说常规模式呢？ 因为如果万一简单的方式没办法满足你的需求的时候，你依然需要使用常规方式进行开发。

我们在继承实现ActivityPage时，还可以通过PageConsumer直接配置Activity或Action来进行Activity<->ActivityPage的连接。
```java
@PageConsumer(clasz = "bamboo.sample.tasks.ui.TaskCountActivity")
public class TaskListPage extends ActivityPage {
    public TaskListPage(Context context) {
        super(context);
    }
}
```
这种方式是不需要在TaskPageConsumer中注册的，框架会自动搜索。

##### 6. 特殊Intent参数配置
在页面交互时我们有时会需要对Intent设置Flag，或需要通过Action或Data方式进行交互，这个时候我们可以通过ActivityPage的targetIntent进行传递，并暂时关闭ActivityPage的连接，[stitcher](https://github.com/bambootang/stitcher)会在这种情况下放弃PageConsumer注解中class的参数链接，直接尝试启动Activity。
```java
public void onActionTest(View view) {
    ActionTestPage page = new ActionTestPage(this);
    Intent targetIntent = new Intent();
    targetIntent.setAction("bamboo.sample.actiontest");
    targetIntent.addCategory(Intent.CATEGORY_DEFAULT);
    page.setTargetIntent(targetIntent);
    page.setAutoLink(false);
    StitcherHelper.start(page);
}
```
>优先级：
**TaskPageConsumer > unAutolink > PageConsumer**

### 数据交互
组件之间的数据交互与页面交互原理是相似的。在onCreateDelay方法中的ComponentRouterRegistry就是数据交互的路由注册表，我们通过它来进行注册。
具体使用参看以下步骤：
##### 1.在router文件夹定义Module的ComponentOutput
```java
package bamboo.sample.tasksrouter;
public interface ITaskComponent extends ComponentOutput{
    int getTaskCount();
}
```
##### 2. 在Module 实现该接口
```java
public class TasksComponentOutput implements ITaskComponent {
    public int getTaskCount() {
        return 1000;
    }
}
```
##### 3. 在onCreateDelay方法中进行注册
```java
public class TasksComponentApp extends ComponentApplication {

    public void onCreateDelay(ComponentRouterRegistry routerRegistry, ActivityRouterRegistry activityRouterRegistry) {
        //将TasksComponentOutput注入到数据路由注册表中
        routerRegistry.regiest(registerComponentOutput，new TasksPageConsumer());
    }
}
```
##### 4.使用
```java
public class ComponentInput {
    public int getTaskCount() {
        ITaskComponent taskComponent = StitcherHelper.searchComponentOutput(ITaskComponent.class);
        return taskComponent == null ? -1 : taskComponent.getTaskCount();
    }
}

```

**具体的数据交互流程如下图：**
![数据交互流程](https://upload-images.jianshu.io/upload_images/4290785-dd4ba35f1117b32c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


详细示例请看源码示例。
