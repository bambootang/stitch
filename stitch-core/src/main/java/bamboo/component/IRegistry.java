package bamboo.component;

import bamboo.component.service.ServiceRegistry;
import bamboo.component.page.ActivityRegistry;

/**
 * Created by tangshuai on 2018/3/19.
 */

public interface IRegistry {

    void register(ServiceRegistry serviceRegistry, ActivityRegistry activityRegistry);

}
