package com.example.myjsbridge;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.HashMap;

public class H5PluginFactory {

    public static HashMap<String, PluginHandler> pluginHandlers;

    public static void init(Context context) {
        pluginHandlers = new HashMap<>();
        registerHandlers(context);
    }

    private static void registerHandlers(Context context) {
        String apiStr = IOUtils.readStringFromAssets(context, "H5API.js");
        String[] apiArray = apiStr.split("\n|\r\n|\r");
        for (String api : apiArray) {
            try {
                String[] comps = api.split("\\.");
                if (comps.length != 2) {
                    return;
                }
                String className = comps[0];
                if (TextUtils.isEmpty(className)) {
                    Log.e("H5PluginFactory", "no such HandlerType");
                    return;
                }
                className = "com.example.myjsbridge.plugin." + className;
                Class<?> handlerClass = Class.forName(className);
                Method handlerMethod = handlerClass.getDeclaredMethod(comps[1]);
                handlerMethod.setAccessible(true);
                PluginHandler handler = (PluginHandler) handlerMethod.invoke(null);
                pluginHandlers.put(api, handler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
