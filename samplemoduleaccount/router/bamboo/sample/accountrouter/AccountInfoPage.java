package bamboo.sample.accountrouter;

import android.content.Context;

import bamboo.component.pagerouter.ActivityPage;
import bamboo.component.pagerouter.PageConsumer;


@PageConsumer(clasz = "bamboo.sample.account.ui.AccountInfoActivity")
public class AccountInfoPage extends ActivityPage {

    public AccountInfoPage(Context context) {
        super(context);
    }
}
