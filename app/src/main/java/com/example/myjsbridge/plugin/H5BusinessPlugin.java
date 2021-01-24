package com.example.myjsbridge.plugin;

import android.widget.Toast;

import com.example.myjsbridge.PluginHandler;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("unused")
public class H5BusinessPlugin {
    public static PluginHandler trackJSMessage() {
        return new PluginHandler() {
            @Override
            public void execute(String params) {
                try {
                    JSONObject jsonObject = new JSONObject(params);
                    String message = jsonObject.getString("message");
                    Toast.makeText(h5WebView.getContext(), "Android received \"" + message + "\" from JS!", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

}
