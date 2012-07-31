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

import java.util.Locale;

public class SoomlaStoreActivity extends Activity {

    // Billing
    private SoomlaPurchaseObserver  mSoomlaPurchaseObserver;
    private Handler                 mHandler;
    private BillingService          mBillingService;

    private WebView     mWebView;
    private Context     mContext;
    private SoomlaJS    mSoomlaJS;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        /* Billing */
        mHandler = new Handler();
        mSoomlaPurchaseObserver = new SoomlaPurchaseObserver(mHandler);
        mBillingService = new BillingService();
        mBillingService.setContext(this);
        ResponseHandler.register(mSoomlaPurchaseObserver);
        if (!mBillingService.checkBillingSupported(Consts.ITEM_TYPE_INAPP)){
            // TODO: handle no connectivity
        }

        mSoomlaJS =    new SoomlaJS();         // The Native<->JS implementation

        Bundle bundle = getIntent().getExtras();
        String layout = bundle.getString("layout");

        /* Setting up the store WebView */
        mWebView = new WebView(this);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(mSoomlaJS, "Soomla");

        mWebView.setWebChromeClient(new WebChromeClient() {
//            public boolean onConsoleMessage(ConsoleMessage cm) {
//                Log.d("MyApplication", cm.message() + " -- From line "
//                        + cm.lineNumber() + " of "
//                        + cm.sourceId() );
//                return true;
//            }
        });

        mWebView.loadUrl("file:///android_asset/" + layout + ".html");

        setContentView(mWebView);

        //TODO: call initialization script here
//        mWebView.loadUrl("javascript:[init_fucntion_name]");
    }

    class SoomlaJS {
        public void purchased(String itemData){
            Toast toast = Toast.makeText(mContext, itemData, Toast.LENGTH_LONG);
            toast.show();

            // TODO: ALERT
        }

        public void back(){
            // TODO: ALERT
        }

        public void storeInitialized(){
            // TODO: ALERT
        }

    }


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
        super.onDestroy();
        // TODO: think what to do with persistence mechanism (Server / File)
        mBillingService.unbind();
    }


    /**
     * A {@link PurchaseObserver} is used to get callbacks when Android Market sends
     * messages to this application so that we can update the UI.
     */
    private class SoomlaPurchaseObserver extends PurchaseObserver {
        public SoomlaPurchaseObserver(Handler handler) {
            super(SoomlaStoreActivity.this, handler);
        }

        @Override
        public void onBillingSupported(boolean supported, String type) {
            if (type == null || type.equals(Consts.ITEM_TYPE_INAPP)) {
                if (supported) {
                    // TODO: handle purchase enabled. Initialize view and datastore (FIle Or Database).
                } else {
                    // TODO: see if needs a change. Maybe close the store when billing is not supported ?!
                    //       and maybe not b/c the user may still buy goods with coins.
                    //       best solution is to just tell webview to disable "buy more coins" button
                    showDialog(SoomlaConsts.DIALOG_BILLING_NOT_SUPPORTED_ID);
                }
            } else if (type.equals(Consts.ITEM_TYPE_SUBSCRIPTION)) {
                // TODO: subscription is supported. Do we want to notify someone ?
            } else {
                showDialog(SoomlaConsts.DIALOG_SUBSCRIPTIONS_NOT_SUPPORTED_ID);
            }
        }

        @Override
        public void onPurchaseStateChange(PurchaseState purchaseState, String itemId,
                                          int quantity, long purchaseTime, String developerPayload) {

            if (purchaseState == PurchaseState.PURCHASED) {
                // TODO: the item was purchased, add it to the store according to itemId
            }

            // TODO: refresh UI here
        }

        @Override
        public void onRequestPurchaseResponse(RequestPurchase request,
                                              Consts.ResponseCode responseCode) {
            if (responseCode == Consts.ResponseCode.RESULT_OK) {
                // purchase was sent to server
            } else if (responseCode == Consts.ResponseCode.RESULT_USER_CANCELED) {
                // purchase canceled by user
                // TODO: tell webview
            } else {
                // purchase failed !
                // TODO: tell webview
            }
        }

        @Override
        public void onRestoreTransactionsResponse(RestoreTransactions request,
                                                  Consts.ResponseCode responseCode) {
            if (responseCode == ResponseCode.RESULT_OK) {
                // RestoreTransaction succeeded !
            } else {
                // RestoreTransaction error !
            }
        }
    }




    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case SoomlaConsts.DIALOG_CANNOT_CONNECT_ID:
                return createDialog(R.string.cannot_connect_title,
                        R.string.cannot_connect_message);
            case SoomlaConsts.DIALOG_BILLING_NOT_SUPPORTED_ID:
                return createDialog(R.string.billing_not_supported_title,
                        R.string.billing_not_supported_message);
            case SoomlaConsts.DIALOG_SUBSCRIPTIONS_NOT_SUPPORTED_ID:
                return createDialog(R.string.subscriptions_not_supported_title,
                        R.string.subscriptions_not_supported_message);
            default:
                return null;
        }
    }

    private Dialog createDialog(int titleId, int messageId) {
        String helpUrl = replaceLanguageAndRegion(getString(R.string.help_url));
        final Uri helpUri = Uri.parse(helpUrl);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titleId)
                .setIcon(android.R.drawable.stat_sys_warning)
                .setMessage(messageId)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(R.string.learn_more, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, helpUri);
                        startActivity(intent);
                    }
                });
        return builder.create();
    }

    /**
     * Replaces the language and/or country of the device into the given string.
     * The pattern "%lang%" will be replaced by the device's language code and
     * the pattern "%region%" will be replaced with the device's country code.
     *
     * @param str the string to replace the language/country within
     * @return a string containing the local language and region codes
     */
    private String replaceLanguageAndRegion(String str) {
        // Substitute language and or region if present in string
        if (str.contains("%lang%") || str.contains("%region%")) {
            Locale locale = Locale.getDefault();
            str = str.replace("%lang%", locale.getLanguage().toLowerCase());
            str = str.replace("%region%", locale.getCountry().toLowerCase());
        }
        return str;
    }

}