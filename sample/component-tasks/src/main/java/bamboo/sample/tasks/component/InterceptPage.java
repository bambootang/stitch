package bamboo.sample.tasks.component;

import android.app.Activity;
import android.content.Intent;

import bamboo.component.StitcherHelper;
import bamboo.component.stitch.anno.Intercept;
import bamboo.sample.tasks.ui.paramersample.TaskInfoActivity;
import bamboo.sample.tasksrouter.TaskInfoPage;

/**
 * Created by tangshuai on 2018/3/27.
 */

public class InterceptPage {

    @Intercept
    public static void receive(TaskInfoPage page) {
        Intent intent = StitcherHelper.pack(page);
        intent.setClass(page.context, TaskInfoActivity.class);

        if (page.context instanceof Activity && page.getRequestCode() != -1) {
            ((Activity) page.context).startActivityForResult(intent, page.getRequestCode());
        } else {
            page.context.startActivity(intent);
        }
    }
}
