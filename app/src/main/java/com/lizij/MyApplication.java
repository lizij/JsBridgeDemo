package com.lizij;

import android.app.Application;
import android.content.Context;

/**
 * @author lizijian
 */
public class MyApplication extends Application {

    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        context = base;
    }
}
