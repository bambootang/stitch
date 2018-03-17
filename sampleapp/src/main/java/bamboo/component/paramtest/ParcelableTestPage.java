package bamboo.component.paramtest;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import bamboo.component.pagerouter.ActivityPage;
import bamboo.component.pagerouter.PageConsumer;

/**
 * Created by tangshuai on 2018/3/17.
 */

@PageConsumer(clasz = "bamboo.component.paramtest.ParcelableTestActivity")
public class ParcelableTestPage extends ActivityPage implements Parcelable {

    public ParcelableTestPage(Context context) {
        super(context);
    }

    private String param1;

    private String param2;

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(param1);
        dest.writeString(param2);
    }

    public static final Creator<ParcelableTestPage> CREATOR = new Creator<ParcelableTestPage>() {
        @Override
        public ParcelableTestPage createFromParcel(Parcel source) {
            ParcelableTestPage testPage = new ParcelableTestPage(null);
            testPage.setParam1(source.readString());
            testPage.setParam2(source.readString());
            return testPage;
        }

        @Override
        public ParcelableTestPage[] newArray(int size) {
            return new ParcelableTestPage[size];
        }
    };
}
