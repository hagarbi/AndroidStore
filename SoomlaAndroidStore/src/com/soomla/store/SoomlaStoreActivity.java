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
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;
import com.soomla.billing.BillingService;
import com.soomla.billing.Consts;
import com.soomla.billing.PurchaseObserver;
import com.soomla.billing.ResponseHandler;
import com.soomla.billing.Consts.PurchaseState;
import com.soomla.billing.Consts.ResponseCode;
import com.soomla.billing.BillingService.RestoreTransactions;
import com.soomla.billing.BillingService.RequestPurchase;
import com.soomla.store.data.StorageManager;
import com.soomla.store.domain.VirtualCurrencyPack;
import com.soomla.store.domain.VirtualGood;
import com.soomla.store.exceptions.VirtualItemNotFoundException;
import com.soomla.store.utils.Utils;

import java.util.Locale;

public class SoomlaStoreActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

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

    /** Protected overridden functions**/

    /**
     * Called when this activity becomes visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
        ResponseHandler.register(mSoomlaPurchaseObserver);

        // TODO: initialize store balance (from File or DB)
    }

    /**
     * Called when this activity is no longer visible.
     */
    @Override
    protected void onStop() {
        super.onStop();
        ResponseHandler.unregister(mSoomlaPurchaseObserver);
    }

    @Override
    protected void onDestroy() {
        mWebView.destroy();
        mWebView = null;

        super.onDestroy();
        // TODO: think what to do with persistence mechanism (Server / File)
        mBillingService.unbind();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        return Utils.createDialog(this, id);
    }

    /** Private functions **/

    public void sendSoomlaJS(String action, String data){
        final String tmpAction = action;
        final String tmpData   = data;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl("javascript:SoomlaJS." + tmpAction + "(" + tmpData + ")");
            }
        });
    }

    /** Private members **/
    private static String TAG = "Soomla Android";

    private SoomlaPurchaseObserver  mSoomlaPurchaseObserver;
    private BillingService          mBillingService;

    private WebView     mWebView;
    private Context     mContext;
    private SoomlaJS    mSoomlaJS;
    private Handler     mHandler;

}