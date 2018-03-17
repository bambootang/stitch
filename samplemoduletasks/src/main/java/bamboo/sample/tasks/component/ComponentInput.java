package bamboo.sample.tasks.component;

import bamboo.component.StitcherHelper;
import bamboo.sample.accountrouter.IAccount;

/**
 * Created by tangshuai on 2018/3/16.
 */

public class ComponentInput {

    IAccount iAccount;

    private static final ComponentInput INSTANCE = new ComponentInput();

    private ComponentInput() {
    }

    public static ComponentInput get() {
        return INSTANCE;
    }

    public String getUserName() {
        if (iAccount == null) {
            iAccount = StitcherHelper.searchComponentOutput(IAccount.class);
        }
        return iAccount != null ? iAccount.getUserName() : "未注册";
    }
}
