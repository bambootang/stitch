package bamboo.sample.tasksrouter;


import bamboo.stitch.router.anno.Wrapper;

@Wrapper
public interface ITaskComponent {

    /**
     * @return 任务总数
     */
    int getTaskCount();

}
