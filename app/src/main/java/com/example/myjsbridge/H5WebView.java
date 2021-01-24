package com.example.myjsbridge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class H5WebView extends WebView {
    private Context mContext;

    public H5WebView(Context context) {
        super(context);
        mContext = context;
        initSetting();
        initWebViewClient();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initSetting() {
        WebSettings settings = getSettings();
        settings.setAllowFileAccess(true);
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
    }

    private void initWebViewClient() {
        this.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                result.cancel();
                parseInvokeUrl(message);
                return true;
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                result.cancel();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                return super.onConsoleMessage(consoleMessage);
            }

        });
    }

    private void parseInvokeUrl(String invokeUrl) {
        try {
            Uri uri = Uri.parse(invokeUrl);
            String scheme = uri.getScheme();
            if (!"jsbridge".equals(scheme)) {
                reportInvokeError();
                return;
            }
            String host = uri.getHost();
            if (TextUtils.isEmpty(host)) {
                reportInvokeError();
                return;
            }
            host = "H5" + host + "Plugin";
            String path = uri.getPath();
            if (TextUtils.isEmpty(path)) {
                reportInvokeError();
                return;
            }
            path = path.replace("/", "");
            PluginHandler handler = H5PluginFactory.pluginHandlers.get(host + "." + path);
            if (handler != null) {
                handler.h5WebView = this;
                handler.execute(uri.getQueryParameter("params"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reportInvokeError() {
        Toast.makeText(mContext, "无法识别的调用", Toast.LENGTH_SHORT).show();
    }

}

