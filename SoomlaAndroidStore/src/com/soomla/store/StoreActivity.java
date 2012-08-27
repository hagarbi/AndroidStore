/*
 * Copyright (C) 2012 Soomla Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.soomla.store;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import com.soomla.billing.ResponseHandler;

import java.util.LinkedList;
import java.util.Queue;

public class StoreActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        StoreConfig.debug        = bundle.getBoolean("debug");
        StoreConfig.publicKey    = bundle.getString("publicKey");

        setRequestedOrientation(StoreInfo.getInstance().getTemplate().isOrientationLandscape() ?
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mProgressDialog = ProgressDialog.show(StoreActivity.this, "",
                "Loading. Please wait...", true);

        mPendingJSMessages = new LinkedList<String>();
        mStoreJSInitialized = false;

        mHandler = new Handler();
        mStorePurchaseObserver = new StorePurchaseObserver(mHandler, this);
        ResponseHandler.register(mStorePurchaseObserver);

        mStoreController = new StoreController(mHandler, this);

        /* Setting up the store WebView */
        mWebView = new WebView(this);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(mStoreController, "SoomlaNative");

        mWebView.setWebChromeClient(new WebChromeClient() {
            public boolean onConsoleMessage(ConsoleMessage cm) {
                Log.d(TAG, cm.message() + " -- From line "
                        + cm.lineNumber() + " of "
                        + cm.sourceId());
                return true;
            }
        });

        mWebView.loadUrl("file:///android_asset/soomla_ui/store.html");
    }

    public void loadWebView(){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                setContentView(mWebView);
                mProgressDialog.dismiss();
            }
        });
    }

    public void sendToJS(String action, String data){
        final String urlToLoad = "javascript:SoomlaJS." + action + "(" + data + ")";

        if (!mStoreJSInitialized){
            mPendingJSMessages.add(urlToLoad);
        }
        else{
            if (StoreConfig.debug){
                Log.d(TAG, "sending message to JS: " + urlToLoad);
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl(urlToLoad);
                }
            });

            while(!mPendingJSMessages.isEmpty()){
                final String tmpPendingUrl = mPendingJSMessages.remove();
                if (StoreConfig.debug){
                    Log.d(TAG, "sending message to JS: " + tmpPendingUrl);
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mWebView.loadUrl(tmpPendingUrl);
                    }
                });
            }


        }
    }

    public void storeJSInitialized(){
        mStoreJSInitialized = true;
    }

    /** Protected overridden functions **/

    /**
     * Called when this activity becomes visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
        ResponseHandler.register(mStorePurchaseObserver);
    }

    /**
     * Called when this activity is no longer visible.
     */
    @Override
    protected void onStop() {
        super.onStop();
        ResponseHandler.unregister(mStorePurchaseObserver);
    }

    /**
     * Called when this activity is destroyed.
     */
    @Override
    protected void onDestroy() {
        mStoreController.onDestroy();

        mWebView.destroy();
        mWebView = null;

        super.onDestroy();
    }

    /** Private members **/
    private static String TAG = "SOOMLA Android";

    private StorePurchaseObserver mStorePurchaseObserver;

    private WebView         mWebView;
    private Handler         mHandler;
    private Queue<String>   mPendingJSMessages;
    private boolean         mStoreJSInitialized;
    private ProgressDialog  mProgressDialog;
    private StoreController mStoreController;
}