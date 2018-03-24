package bamboo.component.lifecycle;


public interface ComponentPriority {

    /**
     * 组件在初始化时的层级：顶层
     */
    int LEVEL_HIGH = 10;

    /**
     * 组件在初始化时的层级：中层
     */
    int LEVEL_MID = 100;

    /**
     * 组件在初始化时的层级：低层
     */
    int LEVEL_LOW = 1000;

}
