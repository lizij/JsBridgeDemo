package com.lizij.binder;


import android.os.IBinder;

import com.lizij.IBinderManager;


/**
 * query binder service by binderCode
 * @author lizijian
 */
public class BinderManager extends IBinderManager.Stub {

    public static final int BINDER_WEB_AIDL_CODE = 0x101;

    @Override
    public IBinder queryBinder(int binderCode) {
        IBinder binder = null;
        switch (binderCode) {
            case BINDER_WEB_AIDL_CODE:
                binder = new WebBinder();
                break;
            default:
                break;
        }

        return binder;
    }
}
