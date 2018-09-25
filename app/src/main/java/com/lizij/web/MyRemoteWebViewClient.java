package com.lizij.web;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Base64;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lizij.IWebBinder;
import com.lizij.jsbridge.CallJsUtil;

import org.json.JSONObject;

import static com.lizij.jsbridge.MyJsBridge.DEFAULT_SCHEMA;

/**
 * @author lizijian
 */
public class MyRemoteWebViewClient extends WebViewClient {

    private IWebBinder webBinder;
    private String mSchema = DEFAULT_SCHEMA;
    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    public MyRemoteWebViewClient(IWebBinder webBinder) {
        this.webBinder = webBinder;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return tryInvokeJavaMethod(request.getUrl(), view);
    }

    public boolean tryInvokeJavaMethod(final Uri uri, final WebView view) {
        if (uri != null && TextUtils.equals(uri.getScheme(), mSchema)) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    invokeJavaMethod(uri.getSchemeSpecificPart(), view);
                }
            });
            return true;
        }
        return false;
    }

    private void invokeJavaMethod(String data, final WebView webView) {
        try {
            String msg = new String(Base64.decode(data, Base64.DEFAULT));
            JSONObject jo = new JSONObject(msg);
            String func = jo.optString("func");
            String params = jo.optString("params");
            final String callbackFunc = jo.optString("callback_func");

            String res = webBinder.invokeJavaMethod(func, params);
            String url = String.format("javascript:%s('%s')", callbackFunc, res);
            CallJsUtil.loadUrl(webView, url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
