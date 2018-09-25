package com.lizij.jsbridge;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Common JsBridge Demo without message queue
 * @author lizijian
 */
public class MyJsBridge {
    public static final String DEFAULT_SCHEMA = "jsbridge";

    public static final String RESULT_CODE = "code";

    public static final String RESULT_SUCCESS = "1";
    public static final String RESULT_FAILED = "0";

    private String mSchema = DEFAULT_SCHEMA;

    private Map<String, IJavaMethod> mRegisteredMethods = new HashMap<>();

    private WebView mWebView;

    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    public MyJsBridge(WebView mWebView) {
        this.mWebView = mWebView;
    }

    public String getSchema() {
        return mSchema;
    }

    public void setSchema(String mSchema) {
        this.mSchema = mSchema;
    }

    /**
     * register java method with {@link IJavaMethod} instance and its name
     * @param func function name
     * @param method {@link IJavaMethod} instance
     */
    public void registerJavaMethod(String func, IJavaMethod method) {
        if (!TextUtils.isEmpty(func) && method != null) {
            mRegisteredMethods.put(func, method);
        }
    }

    public WebView getWebView() {
        return mWebView;
    }

    public void setWebView(WebView mWebView) {
        this.mWebView = mWebView;
    }

    /**
     * try compare the schema of uri with {@link MyJsBridge#mSchema}
     * @param uri
     * @return true if the schemas are equal
     */
    public boolean tryInvokeJavaMethod(final Uri uri) {
        if (uri != null && TextUtils.equals(uri.getScheme(), mSchema)) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    invokeJavaMethod(uri.getSchemeSpecificPart());
                }
            });
            return true;
        }
        return false;
    }

    /**
     * extract func, params and callback_func from uri and invoke the pre-registered java method
     * then invoke callback_func of js.
     * @param data
     */
    private void invokeJavaMethod(String data) {
        try {
            String msg = new String(Base64.decode(data, Base64.DEFAULT));
            JSONObject jo = new JSONObject(msg);
            String func = jo.optString("func");
            String params = jo.optString("params");
            String callbackFunc = jo.optString("callback_func");

            JSONObject res = new JSONObject();
            res.put(RESULT_CODE, RESULT_FAILED);

            if (mRegisteredMethods.containsKey(func)) {
                try {
                    mRegisteredMethods.get(func).call(func, params, res);
                    res.put(RESULT_CODE, RESULT_SUCCESS);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            sendJsMessage(callbackFunc, res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * invoke js function jsFunc by {@link WebView#evaluateJavascript(String, ValueCallback)} or {@link WebView#loadUrl(String)}
     * @param jsFunc
     * @param msg
     */
    protected void sendJsMessage(String jsFunc, JSONObject msg) {
        if (mWebView == null) {
            return;
        }

        String url = String.format("javascript:%s('%s')", jsFunc, msg.toString());
        CallJsUtil.loadUrl(mWebView, url);
    }
}
