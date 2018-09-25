package com.lizij.binder;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * @author lizijian
 */
public class BinderPoolService extends Service {
    private BinderManager binder;

    @Override
    public void onCreate() {
        super.onCreate();
        binder = new BinderManager();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
