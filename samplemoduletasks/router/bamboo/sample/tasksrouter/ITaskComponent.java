package bamboo.sample.tasksrouter;

import bamboo.component.datarouter.ComponentOutput;

public interface ITaskComponent extends ComponentOutput{

    /**
     * @return 任务总数
     */
    int getTaskCount();

}
