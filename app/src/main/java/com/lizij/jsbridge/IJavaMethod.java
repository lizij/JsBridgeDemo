package com.lizij.jsbridge;

import org.json.JSONObject;

/**
 * @author lizijian
 */
public interface IJavaMethod {
    /**
     * java method for MyBridge
     * @param func method name
     * @param param json string
     * @param res json string
     * @throws Exception
     */
    void call(String func, String param, JSONObject res) throws Exception;
}
