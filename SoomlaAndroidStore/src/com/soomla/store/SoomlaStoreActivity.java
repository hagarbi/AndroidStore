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
import android.webkit.WebViewClient;
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

import java.util.Locale;

public class SoomlaStoreActivity extends Activity {

    // Billing
    private SoomlaPurchaseObserver  mSoomlaPurchaseObserver;
    private BillingService          mBillingService;

    private WebView     mWebView;
    private Context     mContext;
    private SoomlaJS    mSoomlaJS;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        /* Billing */
        Handler handler = new Handler();
        mSoomlaPurchaseObserver = new SoomlaPurchaseObserver(handler);
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
        mWebView.addJavascriptInterface(mSoomlaJS, "SoomlaNative");

        mWebView.setWebChromeClient(new WebChromeClient() {
            public boolean onConsoleMessage(ConsoleMessage cm) {
                Log.d("Soomla Android", cm.message() + " -- From line "
                        + cm.lineNumber() + " of "
                        + cm.sourceId());
                return true;
            }
        });

        mWebView.loadUrl("file:///android_asset/" + layout + ".html");

        setContentView(mWebView);


    }

    class SoomlaJS {

        public void wantsToBuyCurrencyPacks(String soomlaId){
            Toast toast = Toast.makeText(mContext, soomlaId, Toast.LENGTH_LONG);
            toast.show();

            mBillingService.requestPurchase("android.test.purchased", Consts.ITEM_TYPE_INAPP, "");

            // TODO: implement
        }

        public void wantsToBuyVirtualGoods(String soomlaId){
            VirtualGood good = StoreInfo.getInstance().getVirtualGoodBySoomlaId(soomlaId);

            int balance = StorageManager.getInstance().getVirtualGoodsStorage().add(good, 1);
            StorageManager.getInstance().getVirtualCurrencyStorage().remove(good.getmCurrencyValue());

            sendSoomlaJS("vGoodsPurchaseEnded", "true," + soomlaId + "," + balance + ",''");
        }

        public void wantsToLeaveStore(){
            Log.v("Soomla Android", "wantsToLeaveStore");
        }

        public void pageInitialized(){

            String json = "{" +
                    " template :  {" +
                    "     name :  \"basic\"," +
                    "     elements :  {" +
                    "         title :  {" +
                    "             name :  \"The Surf Store\"" +
                    "            }," +
                    "         buyMore :  {" +
                    "             text :  \"Buy more clams\"," +
                    "             image :  \"img/examples/surf/clam.png\"" +
                    "            }" +
                    "        }" +
                    "    }," +
                    " background :  \"img/theme-lime-bubbles.jpg\"," +
                    " currency :  {" +
                    "     name :  \"clams\"," +
                    "     image :  \"img/examples/surf/clam.png\"," +
                    "     balance\": \"0\"" +
                    "    }," +
                    " virtualGoods :  [" +
                    "        {" +
                    "         name :  \"Rip Curl Shortboard\"," +
                    "         description :  \"Shred the small waves with this super-fast board\"," +
                    "         src :  \"img/examples/surf/blue-surfboard.png\"," +
                    "         price :  \"100\"," +
                    "         productId :  \"2988822\"" +
                    "        }," +
                    "        {" +
                    "         name :  \"Billanbog Vintage Longboard\"," +
                    "         description :  \"Slowly glide through low power surf and hang five\"," +
                    "         src :  \"img/examples/surf/girl-surfboard-th.png\"," +
                    "         price :  \"150\"," +
                    "         productId :  \"2988823\"" +
                    "        }" +
                    "    ]," +
                    " currencyPacks :  [" +
                    "        {" +
                    "         name :  \"Super Saver Pack\"," +
                    "         description :  \"For you cheap skates...\"," +
                    "         image :  \"coin.jpg\"," +
                    "         itemId :  \"super_saver_pack\"," +
                    "         marketItem :  \"super_saver_pack\"," +
                    "         price :  \"0.99\"," +
                    "         amount :  \"200\"" +
                    "        }," +
                    "        {" +
                    "         name :  \"Malibu Medium Pack\"," +
                    "         description :  \"For you cheap skates...\"," +
                    "         image :  \"coin.jpg\"," +
                    "         itemId :  \"super_saver_pack\"," +
                    "         marketItem :  \"super_saver_pack\"," +
                    "         price :  \"1.99\"," +
                    "         amount :  \"500\"" +
                    "        }," +
                    "        {" +
                    "         name :  \"Pipeline Pumpin\\' Pack\"," +
                    "         description :  \"The holy grail for ya spendin\\' surfers\"," +
                    "         image :  \"coin.jpg\"," +
                    "         itemId :  \"pipeline_pumpin_pack\"," +
                    "         marketItem :  \"pipeline_pumpin_pack\"," +
                    "         price :  \"5.99\"," +
                    "         amount :  \"1500\"" +
                    "        }" +
                    "    ]" +
                    "}";
            Log.v("Soomla Android", json);
            sendSoomlaJS("initialize", "'" + json + "'");

//            ("javascript:Soomla.newStoreFromJSON({" +
//                    "    template : {" +
//                    "        name : \"basic\"," +
//                    "        elements : {" +
//                    "            title : {" +
//                    "                text : \"The surfboard store\"" +
//                    "            }" +
//                    "        }" +
//                    "    }})");
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

                    sendSoomlaJS("disableCoinsStore", "");
                }
            } else if (type.equals(Consts.ITEM_TYPE_SUBSCRIPTION)) {
                // TODO: subscription is supported. Do we want to notify someone ?
            } else {
                showDialog(SoomlaConsts.DIALOG_SUBSCRIPTIONS_NOT_SUPPORTED_ID);
            }
        }

        @Override
        public void onPurchaseStateChange(PurchaseState purchaseState, String itemId,
                                          long purchaseTime, String developerPayload) {

            VirtualCurrencyPack pack = StoreInfo.getInstance().getPackByGoogleProductId(itemId);
            int balance = StorageManager.getInstance().getVirtualCurrencyStorage().getBalance();

            // TODO: check if we need to handle PurchaseState.REFUNDED here

            if (purchaseState == PurchaseState.PURCHASED) {
                // TODO: the item was purchased, add it to the store according to itemId
                sendSoomlaJS("vGoodsPurchaseEnded", "true," + pack.getItemId() + "," + balance + ",''");
            }
            else if(purchaseState == PurchaseState.CANCELED){
                sendSoomlaJS("vGoodsPurchaseEnded", "false," + pack.getItemId() + "," + balance + ",'You canceled the purchase'");
            }

            // TODO: refresh UI here
        }

        @Override
        public void onRequestPurchaseResponse(RequestPurchase request,
                                              Consts.ResponseCode responseCode) {
            VirtualCurrencyPack pack = StoreInfo.getInstance().getPackByGoogleProductId(request.mProductId);
            int balance = StorageManager.getInstance().getVirtualCurrencyStorage().getBalance();

            if (responseCode == Consts.ResponseCode.RESULT_OK) {
                // purchase was sent to server
            } else if (responseCode == Consts.ResponseCode.RESULT_USER_CANCELED) {
                // purchase canceled by user
                // TODO: tell webview

                sendSoomlaJS("vGoodsPurchaseEnded", "false," + pack.getItemId() + "," + balance + ",'You canceled the purchase.'");
            } else {
                // purchase failed !
                // TODO: tell webview
                sendSoomlaJS("vGoodsPurchaseEnded", "false," + pack.getItemId() + "," + balance + ",'Unexpected error occured! Your purchase is canceled.'");
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

    private void sendSoomlaJS(String action, String data){
        mWebView.loadUrl("javascript:SoomlaJS." + action + "(" + data + ")");
    }
}