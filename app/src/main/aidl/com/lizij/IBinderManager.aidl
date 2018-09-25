// IBinderManager.aidl
package com.lizij;
import android.os.IBinder;

// Declare any non-default types here with import statements

interface IBinderManager {
    IBinder queryBinder(int binderCode);
}
