package com.lizij.jsbridge;

import android.os.Process;
import org.json.JSONObject;

/**
 * @author lizijian
 */
public class TestJsBridge implements IJavaMethod {
    @Override
    public void call(String func, String param, JSONObject res) throws Exception {
        res.put("res", param);
        res.put("called from process", Process.myPid());
    }
}
