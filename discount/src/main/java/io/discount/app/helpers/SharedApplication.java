package io.discount.app.helpers;
import android.app.Application;

/**
 * Created by Jan on 19.3.14.
 */
public class SharedApplication extends Application {
    private static final String userToken = "";

    private static SharedApplication sharedApplication;

    public static SharedApplication getInstance() {
        if(sharedApplication == null) {
            sharedApplication = new SharedApplication();
        }
        return sharedApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedApplication = this;
    }

    public String getUserToken() {
        return SharedApplication.userToken;
    }
}
