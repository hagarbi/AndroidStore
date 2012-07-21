package com.soomla.store;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;
import com.soomla.store.data.VGoodsStore;

import java.io.IOException;

public class SoomlaStoreActivity extends Activity {
    private WebView  mWebView;
    private Context  mContext;
    private SoomlaJS mSoomlaJS;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        mWebView = new WebView(this);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mSoomlaJS = new SoomlaJS();
        mWebView.addJavascriptInterface(mSoomlaJS, "Soomla");

        Settings.APPLICATION_CONTEXT = getApplicationContext();

        mWebView.setWebChromeClient(new WebChromeClient() {
//            public boolean onConsoleMessage(ConsoleMessage cm) {
//                Log.d("MyApplication", cm.message() + " -- From line "
//                        + cm.lineNumber() + " of "
//                        + cm.sourceId() );
//                return true;
//            }
        });

        mWebView.loadUrl("file:///android_asset/store.html");

        setContentView(mWebView);
    }

    class SoomlaJS {
        public void purchased(String itemData){
            Toast toast = Toast.makeText(mContext, itemData, Toast.LENGTH_LONG);
            toast.show();
        }
    }
}