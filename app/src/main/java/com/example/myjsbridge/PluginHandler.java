package com.example.myjsbridge;

public abstract class PluginHandler {
    protected H5WebView h5WebView;

    protected abstract void execute(String params);
}
