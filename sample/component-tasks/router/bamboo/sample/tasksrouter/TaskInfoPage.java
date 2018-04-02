package bamboo.sample.tasksrouter;

import android.content.Context;

import java.io.Serializable;
import java.lang.annotation.Documented;

import bamboo.component.page.ActivityPage;
import bamboo.stitch.router.anno.Parameter;
import bamboo.stitch.router.anno.Wrapper;

/**
 * Created by tangshuai on 2018/3/17.
 */

@Wrapper
public class TaskInfoPage extends ActivityPage implements Serializable {

    @Parameter
    public String taskId;

    public TaskInfoPage(Context context) {
        super(context);
    }

}
