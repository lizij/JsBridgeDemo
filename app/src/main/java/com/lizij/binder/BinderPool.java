package com.lizij.binder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.WorkerThread;

import com.lizij.IBinderManager;

import java.util.concurrent.CountDownLatch;

/**
 * @author lizijian
 */
public class BinderPool {
    private static final String TAG = BinderPool.class.getSimpleName();

    private static volatile BinderPool inst;

    private CountDownLatch countDownLatch;

    private IBinderManager binderManager;

    private BinderPool() {
    }

    public static BinderPool inst() {
        if (inst == null) {
            synchronized (BinderPool.class) {
                if (inst == null) {
                    inst = new BinderPool();
                }
            }
        }
        return inst;
    }


    /**
     * bind {@link BinderPoolService} in main process and get {@link BinderManager} binder instance
     * @param context
     */
    @WorkerThread
    public synchronized void bind(final Context context) {
        // should be call in WorkThread
        if (context == null) {
            return;
        }

        countDownLatch = new CountDownLatch(1);
        Intent intent = new Intent(context, BinderPoolService.class);
        context.bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                // called in MainThread
                binderManager = IBinderManager.Stub.asInterface(service);
                try {
                    // when binder die, rebind binderManager with context
                    binderManager.asBinder().linkToDeath(new IBinder.DeathRecipient() {
                        @Override
                        public void binderDied() {
                            binderManager.asBinder().unlinkToDeath(this, 0);
                            binderManager = null;
                            bind(context);
                        }
                    }, 0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                countDownLatch.countDown();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, Context.BIND_AUTO_CREATE);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public IBinder getWebBinder() {
        IBinder binder = null;
        try {
            if (binderManager != null) {
                binder = binderManager.queryBinder(BinderManager.BINDER_WEB_AIDL_CODE);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return binder;
    }
}
