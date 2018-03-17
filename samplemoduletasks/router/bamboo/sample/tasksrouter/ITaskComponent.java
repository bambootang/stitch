package bamboo.sample.tasksrouter;

import bamboo.component.datarouter.ComponentOutput;

public interface ITaskComponent extends ComponentOutput{

    /**
     * 获取任务总数
     *
     * @return
     */
    int getTaskCount();

}
