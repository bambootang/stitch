# stitch
Android组件化开发框架

**stitch**的目标是：降低不同module进行交互时的沟通成本，让开发人员更容易做正确的事情。

## 功能支持
0. 不同module都能接收Application的onCreate等生命周期回调进行本module的初始化
1. 不同module之间的页面跳转，自动打包传递参数
2. 不同module之间的数据交互，接口参数对开发友好可见
3. 页面跳转支持所有Intent可配置的参数
4. 支持页面跳转拦截
5. 自动绑定并注入到框架
6. 支持Multdex
7. 支持单接口多module多实现方案

## 基础依赖
在项目根目录的build.gradle文件中添加classpath依赖
```groovy
buildscript {
    dependencies {
        classpath 'bamboo.components.stitch:stitch-gradle-plugin:1.2'
    }
}

```
在需要使用stitch的module的build.gradle文件中加入：
```groovy
//apply plugin: 'com.android.application'
apply plugin: 'com.android.library'
//需要放在android的plugin后面，目前只支持与application及library的plugin共同使用
apply plugin: 'stitch.plugin'
```

## 组件（module）生命周期

### @Component、ComponentLife
```ComponentLife```类是组件生命周期代理基类，我们通过继承```ComponentLife```类并使用```@Component```进行标记，即可将我们的组件注入到stitch中。

每一个Module只允许有1个@Component标注类，如果标注了多个，只会有一个生效。一个显然已经满足需求了，^_^

具体使用方法：
#### 1. 自定义ComponentLife子类
如果Module不需要在Application的onCreate、attachBaseContext等生命周期时进行初始化操作，不需要自定义ComponentLife类。
```java
@Component
public class TestComponetLife extends ComponentLife {

    /*
     * 代理Application的OnCreate方法
     */
    public void onCreate(){
        //TODO 进行需要的初始化操作
    }

    public void attachBaseContext(Context baseContext){

    }
}

//获取TestComponetLife对象
TestComponetLife  testComponent = Stitch.searchComponentApplication(TestComponetLife.class);
//获取Application对象
Application application = testComponent.getApplication();
//或者
Application application = Stitch.getApplication();
```
#### 2. 生命周期回调

在Application里面我们需要主动传递Application的生命周期给stitch，stitch提供了两种实现方式：

**1.  直接继承StitchApplication**

StitchApplication继承自Application，只是添加了stitch的生命周期调用

**2.  通过Stitch调用组件的生命周期。**

如果不能直接继承StitchApplication，在你自己的Application中主动调用Stitch.onCreate()、Stitch.attachBaseContext()等方法，具体可参考StitchApplication的实现。

## 页面跳转

### @Exported、ActivityPage
```@Exported```注解用于将Module中的Activity开放给其他Module调用，

ActivityPage 用于与开放的Activity进行关联，同时它实现了Serializable接口，所以也作为Activity的数据传递的封装类使用。

>在实际使用之前，我们需要新建1个公用Module作为路由Module，这里我们假设我们所有的Module都依赖于名为**Router**的Module,而我们开发的Module名为**ModuleA**，其他Module名为**ModuleB**

#### 1. 使用
每一个需要共享给其他Module使用的Activity都需要与一个ActivityPage进行关联，其他Module通过ActivityPage即可与Activity进行交互。

```java
//1.在Router中创建TestPage.java
public class TestPage extends ActivityPage{
    public final String text;
    public TestPage(Context context,String text) {
        super(context);
        this.text = text;
    }
}

//2.在ModuleB中启动TestPage，开发ModuleB的人不需要知道TestPage对应到具体哪个页面
Stitch.start(new TestPage(context,"text test"));
//或者使用
new TestPage(context,"text test").start();

//3.在ModuleA中创建TestActivity并关联TestPage
@Exported(TestPage.class)
public class TestActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
       ...
       //接收页面启动时传递的参数,TestPage的simpleClassName为参数key
       TestPage infoPage = (TestPage) getIntent().getSerializableExtra("TestPage");
       mTestTextView.setText(infoPage.text);
    }
}
```

#### 2. Intent Flags等设置
```java
public void onActionTest(View view) {

    new ActionTestPage(context)
        .setTargetIntent(new Intent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        .start();

    //如果需要在Activity接收返回值
    new ActionTestPage(context)
        .setTargetIntent(new Intent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        .setRequestCode(100)
        .startForResult();
}
```

#### 3.Activity跳转时的参数传递
在页面交互时，有时候我们需要往下一个页面传递参数，stitch默认支持Serializable，同时也支持Parcelable方式

如果你定义的TestPage实现了Parcelable接口，stitch会自动切换为Parcelable方式进行参数传递。
```java
// 默认的Serializable方式
public class TestPage extends ActivityPage {
    ....
    // 不想传递的参数用transient描述
    public transient final int noParamer = 100;
}

// Parcelable方式，需要自己写序列化方法
public class TestPage extends ActivityPage implements Parcelable {
    ....
}
```

### @Intercept
@Intercept用来作为@Exported的补充，在某些情况下，Module对外公开的是一个页面入口，但是在不同的条件下可能该入口打开的页面并不是同一个。

```java
public class InterceptTest{

    @Intercept
    public static void receive(TestPage page){
        if (AppUtils.isHuaweiChannel()) {
            Intent intent = Stitch.pack(page);
            intent.setClass(page.context, TestActivityA.class);
            page.context.startActivity(intent);
        } else {
            Intent intent = Stitch.pack(page);
            intent.setClass(page.context, TestActivity.class);
            page.context.startActivity(intent);
        }
    }
}
```
## 数据交互

### @Service
```@Service```注解用于向stitch中注入Module对外公开的实现接口。

```java
// 1.在 Router 中创建 ITestService.java
public interface ITestService {
    String getTestText();
}

// 2. 在 ModuleA 中创建 TestServiceImp.java 并实现 ITestService
@Service
public class TestServiceImp implements ITestService {
    public String getTestText() {
        return "hello world!";
    }
}

// 3. 在 ModuleB 中使用该接口
public class TestServiceTest {
    public void test() {
        ITestService testService = Stitch.searchService(ITestService.class);
        //如果组件没有引用（未打包到apk中）时，testService为null
        String testText =  testService == null ? "" : testService.getTestText();
    }
}

```

## 高级功能
在不同module想要调用其他module对外公开的页面，就需要知道有哪些页面被公开或公开的页面叫什么名称。同样的，想要调用其他module对外公开的接口，也需要知道接口是什么以及在哪个service中。为解决这个问题，stitch提供了整合ActivityPage及Service的功能。

### 在module中添加依赖
```
    implementation 'bamboo.components.stitch:stitch-router-anno:1.2'
    annotationProcessor 'bamboo.components.stitch:stitch-router-compiler:1.2'
```

#### 打包整合ActivityPage

将所有的ActivityPage统合到ActivityPageManager中，我们只需要通过ActivityPageManager就可以知道有哪些页面是全局可见的。
```
@Wrapper
public class TestPageA extends ActivityPage {
    @Parameter
    public Strint text;

    private String param;

    public TestPageA(Context context) {
        super(context);
    }

    @Parameter("param")
    public void setParam(String param){
        this.param = param;
    }
}

@Wrapper
public class TestPageB extends ActivityPage {
    public TestPageB(Context context,String text) {
        super(context);
        this.text = text;
    }
}

//使用时通过ActivityPageManager访问
ActivityPageManager.newTestPageA(context).setText("test text").setParam("param test").setRequestCode(1000).startForResult();
ActivityPageManager.newTestPageB(context,"test text").start();
```
#### 打包整合接口方法

将所有Service实现的接口方法统合ServiceManager中，可跳过Class层直接调用到方法。
```
@Wrapper
public interface ITestService {
    
    String getTestText();

    //容易混淆或重名的方法，用@Alias指定别名
    @Alias("getTestServiceName")
    String getName();
}

@Wrapper
public interface ITestService2 {
    //容易混淆或重名的方法，用@Alias指定别名
    @Alias("getTestService2Name")
    String getName();
}
//调用时通过ServiceManager直接访问方法
String text = ServiceManager.getTestText();
String testName = ServiceManager.getTestServiceName();
String testName2 = ServiceManager.getTestService2Name();

//设定默认值，如果接口没有实现或者实现的module没有被打包到Apk，接口会返回默认值
//原始数据类型默认值是其数据值的最小值，boolean默认值为false，其他引用类型为null，如果要覆盖这个设定可以通过以下方式
String defaultText = "defaultText";
//如果getTestText没有实现，会返回defaultText
String text = ServiceManager.getTestText(defaultText);
```

## 技术交流

*    ![stitch技术交流群](https://raw.githubusercontent.com/bambootang/stitch/master/sample/stitch-group.png)

**stitch还很年轻**

**欢迎大家star 或提交pr**
