package io.discount.app.test;

import android.annotation.TargetApi;
import android.os.Build;
import android.test.ActivityInstrumentationTestCase2;
import io.discount.app.MainActivity;

/**
 * Created by Jan on 15.3.14.
 */
public class TestMainActivity extends ActivityInstrumentationTestCase2<MainActivity> {


    @TargetApi(Build.VERSION_CODES.FROYO)
    public TestMainActivity() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    public void testPreconditions() throws Exception {

    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }
}
