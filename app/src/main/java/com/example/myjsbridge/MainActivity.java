package com.example.myjsbridge;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private H5WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewGroup webContainer = findViewById(R.id.root);
        webContainer.addView(initWebView());
    }

    private H5WebView initWebView() {
        webView = new H5WebView(this);
        webView.loadUrl("file:///android_asset/web/test.html");
        return webView;
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.postDelayed(new Runnable() {
            @Override
            public void run() {
                webView.evaluateJavascript("javascript:(function() {" +
                        "notifyWebViewReady(\"WebView onCreated at \");" +
                        "})()", null);
            }
        }, 1000);
    }
}