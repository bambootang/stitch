# stitch
Android组件化开发框架

### 依赖
在项目根目录的build.gradle文件中添加classpath依赖
```groovy
buildscript {
    repositories {
        //stitch发布在这里面
        maven {
            url 'https://raw.githubusercontent.com/bambootang/maven/master'
        }
        ...
    }
    dependencies {
        ...
        classpath 'bamboo.components.stitch:stitch-gradle-plugin:1.1'
    }
}

allprojects {

    repositories {
        maven {
            url 'https://raw.githubusercontent.com/bambootang/maven/master'
        }
        ...
    }
}
```
在需要使用stitch的module的build.gradle文件中加入：
```groovy
//apply plugin: 'com.android.application'
//apply plugin: 'com.android.library'
//需要放在android的plugin后面
apply plugin: 'stitch.plugin'
```


### @Component、ComponentLife
```ComponentLife```类是组件生命周期代理基类，我们通过继承```ComponentLife```类并使用```@Component```进行标记，即可将我们的组件注入到stitch中。

每一个Module只允许有1个@Component标注类，如果标注了多个，只会有一个生效。


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
    
    //可以通过TestComponetLife获取Application对象
    TestComponetLife  testComponent = StitcherHelper.searchComponentApplication(TestComponetLife.class);
    Application application = testComponent.getApplication();
}
```
#### 2. 传递Application生命周期

在Application里面我们需要主动传递Application的生命周期给stitch，[stitch](https://github.com/bambootang/stitch)提供了两种实现方式：

**1.  直接继承StitcherApplication**

StitcherApplication继承自Application，只是添加了stitch的生命周期调用

**2.  通过StitcherHelper调用组件的生命周期。**

如果不能直接继承StitcherApplication，在你自己的Application中主动调用StitcherHelper.onCreate()、StitcherHelper. attachBaseContext()等方法，具体可参考StitcherApplication的实现。

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
StitcherHelper.start(new TestPage(context,"text test"));

//3.在ModuleA中创建TestActivity并关联TestPage
@Exported(TestPage.class)
public class TestActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
       ...
       //接收页面启动时传递的参数
       TestPage infoPage = (TestPage) getIntent().getSerializableExtra("TestPage");
       mTestTextView.setText(infoPage.text);
    }
}
```

#### 2. Intent Flag设置
在页面交互时我们有时会需要对Intent设置Flag，这个时候我们可以通过ActivityPage的targetIntent进行传递，在启动TestActivity时[stitch](https://github.com/bambootang/stitch)会clone targetIntent的所有参数。
```java
public void onActionTest(View view) {
    ActionTestPage page = new ActionTestPage(context);
    Intent targetIntent = new Intent();
    targetIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    page.setTargetIntent(targetIntent);
    StitcherHelper.start(page);
}
```

#### 3.Activity跳转时的参数传递
在页面交互时，有时候我们需要往下一个页面传递参数，上面已经提到过Serializable方式，stitch还支持另一种方式：Parcelable

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
        if(AppUtils.isHuaweiChannel()){
            Intent intent = StitcherHelper.pack(page);
            intent.setClass(page.context, TestActivityA.class);
            page.context.startActivity(intent);
        } else {
            Intent intent = StitcherHelper.pack(page);
            intent.setClass(page.context, TestActivity.class);
            page.context.startActivity(intent);
        }
    }
}
```

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
        ITestService testService = StitcherHelper.searchService(ITestService.class);
        //如果组件没有引用（未打包到apk中）时，testService为null
        String testText =  testService == null ? "" : testService.getTestText();
    }
}

```
欢迎大家star 或提交pr


组件化脚本优化配置：[Android组件化：build.gradle配置](https://www.jianshu.com/p/9620a40c203f)

组件化基本概念：[Android组件化开发框架](https://www.jianshu.com/p/3ed9f4c87990)


详细示例请看源码示例。
