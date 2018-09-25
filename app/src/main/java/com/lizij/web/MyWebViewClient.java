package com.lizij.web;

import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lizij.jsbridge.MyJsBridge;

/**
 * @author lizijian
 */
public class MyWebViewClient extends WebViewClient {

    private MyJsBridge mJsBridge;

    public MyWebViewClient(MyJsBridge mJsBridge) {
        this.mJsBridge = mJsBridge;
    }

    public MyJsBridge getJsBridge() {
        return mJsBridge;
    }

    public void setJsBridge(MyJsBridge mJsBridge) {
        this.mJsBridge = mJsBridge;
    }

    /**
     * intercept url and try invoke pre-registered java methods
     * @param view
     * @param request
     * @return
     */
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return mJsBridge != null && mJsBridge.tryInvokeJavaMethod(request.getUrl());
    }
}
