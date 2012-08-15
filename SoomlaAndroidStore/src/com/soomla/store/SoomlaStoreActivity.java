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
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import com.soomla.billing.BillingService;
import com.soomla.billing.Consts;
import com.soomla.billing.ResponseHandler;
import com.soomla.store.data.FileStorage;
import com.soomla.store.data.StorageManager;

import java.util.LinkedList;
import java.util.Queue;

public class SoomlaStoreActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        mPendingJSMessages = new LinkedList<String>();
        mStoreJSInitialized = false;

        /* Billing */
        mHandler = new Handler();
        mSoomlaPurchaseObserver = new SoomlaPurchaseObserver(mHandler, this);
        mBillingService = new BillingService();
        mBillingService.setContext(this);
        ResponseHandler.register(mSoomlaPurchaseObserver);
        if (!mBillingService.checkBillingSupported(Consts.ITEM_TYPE_INAPP)){
            if (SoomlaConsts.DEBUG){
                Log.d(TAG, "There's no connectivity with the billing service.");
            }
        }

        // The Native<->JS implementation
        mSoomlaJS = new SoomlaJS(getApplicationContext(), mBillingService, mHandler, this);

        Bundle bundle = getIntent().getExtras();

        StoreInfo.getInstance().initialize(getApplicationContext());
        StorageManager.getInstance().initialize(
                new FileStorage(getApplicationContext(), "soomla.virtualcurrency"),
                new FileStorage(getApplicationContext(), "soomla.virtualgoods"),
                new FileStorage(getApplicationContext(), "soomla.marketpurchase"));

        /* Setting up the store WebView */
        mWebView = new WebView(this);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(mSoomlaJS, "SoomlaNative");

        mWebView.setWebChromeClient(new WebChromeClient() {
            public boolean onConsoleMessage(ConsoleMessage cm) {
                Log.d(TAG, cm.message() + " -- From line "
                        + cm.lineNumber() + " of "
                        + cm.sourceId());
                return true;
            }
        });

        mWebView.loadUrl("file:///android_asset/store.html");

        setContentView(mWebView);
    }

    public void sendSoomlaJS(String action, String data){
        final String urlToLoad = "javascript:SoomlaJS." + action + "(" + data + ")";

        if (!mStoreJSInitialized){
            mPendingJSMessages.add(urlToLoad);
        }
        else{
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl(urlToLoad);
                }
            });

            while(!mPendingJSMessages.isEmpty()){
                final String tmpPendingUrl = mPendingJSMessages.remove();
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
        ResponseHandler.register(mSoomlaPurchaseObserver);
    }

    /**
     * Called when this activity is no longer visible.
     */
    @Override
    protected void onStop() {
        super.onStop();
        ResponseHandler.unregister(mSoomlaPurchaseObserver);
    }

    /**
     * Called when this activity is destroyed.
     */
    @Override
    protected void onDestroy() {
        mWebView.destroy();
        mWebView = null;

        super.onDestroy();
        mBillingService.unbind();
    }

    /** Private members **/
    private static String TAG = "SOOMLA Soomla Android";

    private SoomlaPurchaseObserver  mSoomlaPurchaseObserver;
    private BillingService          mBillingService;

    private WebView     mWebView;
    private Context     mContext;
    private SoomlaJS    mSoomlaJS;
    private Handler     mHandler;
    private Queue<String> mPendingJSMessages;
    private boolean     mStoreJSInitialized;
}