package com.lizij.jsbridge;

import android.text.TextUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.lizij.jsbridge.MyJsBridge.RESULT_CODE;
import static com.lizij.jsbridge.MyJsBridge.RESULT_FAILED;
import static com.lizij.jsbridge.MyJsBridge.RESULT_SUCCESS;

/**
 * @author lizijian
 */
public class MyRemoteJsBridge {
    private static volatile MyRemoteJsBridge bridge;

    private Map<String, IJavaMethod> mRegisteredMethods = new HashMap<>();

    private MyRemoteJsBridge() {
    }

    public static MyRemoteJsBridge inst() {
        if (bridge == null) {
            synchronized (MyRemoteJsBridge.class) {
                if (bridge == null) {
                    bridge = new MyRemoteJsBridge();
                }
            }
        }
        return bridge;
    }

    public void registerJavaMethod(String func, IJavaMethod method) {
        if (!TextUtils.isEmpty(func) && method != null) {
            mRegisteredMethods.put(func, method);
        }
    }

    public String invokeJavaMethod(String func, String params) {
        try {
            JSONObject res = new JSONObject();
            res.put(RESULT_CODE, RESULT_FAILED);
            if (mRegisteredMethods.containsKey(func)) {
                mRegisteredMethods.get(func).call(func, params, res);
                res.put(RESULT_CODE, RESULT_SUCCESS);
            }
            return res.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
