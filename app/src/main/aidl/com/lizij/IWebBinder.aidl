// IWebBinder.aidl
package com.lizij;

// Declare any non-default types here with import statements

interface IWebBinder {
    String invokeJavaMethod(in String methodName, in String params);
}
