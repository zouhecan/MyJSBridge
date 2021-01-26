package com.example.myjsbridge;

import android.content.Context;
import android.text.TextUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class H5PluginFactory {
    private final static List<String> pluginForm = new ArrayList<>();
    private final static HashMap<String, PluginHandler> pluginHandlers = new HashMap<>();

    public static void init(Context context) {
        initPluginForm(context);
    }

    //初始化插件表
    private static void initPluginForm(Context context) {
        String apiStr = IOUtils.readStringFromAssets(context, "H5API.js");
        String[] apiArray = apiStr.split("\n|\r\n|\r");
        for (String api : apiArray) {
            String[] comps = api.split("\\.");
            if (comps.length != 2) {
                continue;
            }
            String className = comps[0];
            String methodName = comps[1];
            if (TextUtils.isEmpty(className) || TextUtils.isEmpty(methodName)) {
                continue;
            }
            pluginForm.add(api);
        }
    }

    //通过插件名或取对应的插件
    public static PluginHandler getPluginHandler(String pluginName) {
        if (TextUtils.isEmpty(pluginName) || !pluginForm.contains(pluginName)) {
            return null;
        }
        PluginHandler handler = pluginHandlers.get(pluginName);
        if (handler != null) {
            return handler;
        }
        return registerHandlers(pluginName);
    }

    //注册插件
    private static PluginHandler registerHandlers(String pluginName) {
        try {
            String[] comps = pluginName.split("\\.");
            if (comps.length != 2) {
                return null;
            }
            String className = "com.example.myjsbridge.plugin." + comps[0];
            Class<?> handlerClass = Class.forName(className);
            Method handlerMethod = handlerClass.getDeclaredMethod(comps[1]);
            handlerMethod.setAccessible(true);
            PluginHandler handler = (PluginHandler) handlerMethod.invoke(null);
            pluginHandlers.put(pluginName, handler);
            return handler;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
