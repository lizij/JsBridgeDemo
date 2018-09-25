package com.lizij.binder;


import com.lizij.IWebBinder;
import com.lizij.jsbridge.MyRemoteJsBridge;

/**
 * For remote web process call java method in main process
 * @author lizijian
 */
public class WebBinder extends IWebBinder.Stub {

    /**
     * Call java method registered in main process
     * @param func method name
     * @param params json string
     * @return result json string
     */
    @Override
    public String invokeJavaMethod(String func, String params) {
        return MyRemoteJsBridge.inst().invokeJavaMethod(func, params);
    }
}
