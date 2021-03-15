package com.example.myjsbridge.plugin;

import android.text.TextUtils;

import com.example.myjsbridge.PluginHandler;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("unused")
public class H5DevicePlugin {
    public static PluginHandler deviceInfo() {
        return new PluginHandler() {
            @Override
            public void execute(String params) {
                try {
                    JSONObject paramJSON = new JSONObject(params);
                    String callback = null;
                    if (paramJSON.has("callback")) {
                        callback = paramJSON.getString("callback");
                    }
                    JSONObject response = new JSONObject();
                    response.put("platform", "Android");
                    response.put("deviceId", "1233456");

                    if (h5WebView != null && !TextUtils.isEmpty(callback)) {
//                        String func = "javascript:(function() {" +
//                                callback + "(JSON.stringify(" + response.toString() + "));" +
//                                "})()";
                        String func = "javascript:(" + callback + "(JSON.stringify(" + response.toString() + "))";
                        h5WebView.evaluateJavascript(func, null);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

}
