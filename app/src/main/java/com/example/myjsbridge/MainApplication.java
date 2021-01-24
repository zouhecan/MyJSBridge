package com.example.myjsbridge;

import android.app.Application;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        H5PluginFactory.init(this);
    }
}
