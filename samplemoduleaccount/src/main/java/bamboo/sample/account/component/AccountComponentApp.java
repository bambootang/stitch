package bamboo.sample.account.component;

import bamboo.component.datarouter.ComponentRouterRegistry;
import bamboo.component.lifecycle.ComponentApplication;
import bamboo.component.lifecycle.ComponentPriority;
import bamboo.component.pagerouter.ActivityRouterRegistry;
import bamboo.sample.accountrouter.IAccount;

public class AccountComponentApp extends ComponentApplication {

    @Override
    public void onCreateDelay(ComponentRouterRegistry routerRegistry, ActivityRouterRegistry activityRouterRegistry) {
        routerRegistry.register(IAccount.class, new AccountComponentOutput());
        activityRouterRegistry.regiest(new AccountPageConsumer());
    }

    @Override
    public void onLowMemory() {
        System.out.println("onLowMemory");
    }

    @Override
    public int level() {
        return ComponentPriority.LEVEL_MID;
    }
}
