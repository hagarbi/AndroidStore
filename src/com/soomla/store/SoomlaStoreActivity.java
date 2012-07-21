package com.soomla.store;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import com.soomla.store.data.VGoodsStore;

import java.io.IOException;

public class SoomlaStoreActivity extends Activity {
    private WebView  mWebView;
    private SoomlaJS mSoomlaJS;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWebView = new WebView(this);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mSoomlaJS = new SoomlaJS();
        mWebView.addJavascriptInterface(mSoomlaJS, "Soomla");

        Settings.APPLICATION_CONTEXT = getApplicationContext();

        mWebView.loadUrl("file:///android_asset/store.html");

        setContentView(mWebView);
    }

    class SoomlaJS {
        void purchased(String itemData){

        }
    }
}