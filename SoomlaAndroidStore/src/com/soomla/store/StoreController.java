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

import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import com.soomla.billing.BillingService;
import com.soomla.billing.Consts;
import com.soomla.store.data.StorageManager;
import com.soomla.store.data.StoreInfo;
import com.soomla.store.domain.data.VirtualGood;
import com.soomla.store.exceptions.VirtualItemNotFoundException;

import java.util.HashMap;

/**
 * This class is the main place to invoke store actions.
 * SOOMLA's android sdk uses this class as an interface between the
 * webview's JS and the native code.
 */
public class StoreController {

    /** Constructor
     *
     * @param mHandler is a Handler used to post messages to the UI thread.
     * @param mActivity is the main {@link StoreActivity}.
     */
    public StoreController(Handler mHandler,
                           StoreActivity mActivity) {
        this.mHandler = mHandler;
        this.mActivity = mActivity;


        /* Billing */

        mBillingService = new BillingService();
        mBillingService.setContext(mActivity.getApplicationContext());

        if (!mBillingService.checkBillingSupported(Consts.ITEM_TYPE_INAPP)){
            if (StoreConfig.debug){
                Log.d(TAG, "There's no connectivity with the billing service.");
            }
        }


        /* Storage */

        HashMap<String, String> secureData;
        if (StoreConfig.DB_SECURE){
            secureData = new HashMap<String, String>();
            String deviceId = Settings.Secure.getString(mActivity.getContentResolver(), Settings.Secure.ANDROID_ID);
            secureData.put("applicationId", mActivity.getPackageName());
            secureData.put("deviceId", deviceId);
        }
        else {
            Log.w(TAG,
                    "Your database data will not be encrypted. " +
                            "Don't ever release your application this way! " +
                            "Change StoreConfig.DB_SECURE to true.");
        }
        StorageManager.getInstance().initialize(mActivity.getApplicationContext(), secureData);
    }

    /**
     * The user wants to buy a virtual currency pack.
     * @param productId is the product id of the pack.
     */
    public void wantsToBuyCurrencyPacks(String productId){
        Log.d(TAG, "wantsToBuyCurrencyPacks " + productId);

        StoreEventHandlers.getInstance().onMarketPurchaseProcessStarted();
        mBillingService.requestPurchase(productId, Consts.ITEM_TYPE_INAPP, "");
    }

    /**
     * The user wants to buy a virtual good.
     * @param itemId is the item id of the virtual good.
     */
    public void wantsToBuyVirtualGoods(String itemId) {
        Log.d(TAG, "wantsToBuyVirtualGoods " + itemId);
        StoreEventHandlers.getInstance().onGoodsPurchaseProcessStarted();
        try {
            VirtualGood good = StoreInfo.getInstance().getVirtualGoodByItemId(itemId);

            if (StorageManager.getInstance().getVirtualCurrencyStorage().getBalance() >= good.getmCurrencyValue()){
                StorageManager.getInstance().getVirtualGoodsStorage().add(good, 1);
                StorageManager.getInstance().getVirtualCurrencyStorage().remove(good.getmCurrencyValue());

                updateJSBalances();

                StoreEventHandlers.getInstance().onVirtualGoodPurchased(good);
            }
            else {
                int balance = StorageManager.getInstance().getVirtualGoodsStorage().getBalance(good);
                mActivity.sendToJS("insufficientFunds", "" + balance);
            }
        } catch (VirtualItemNotFoundException e) {
            mActivity.sendToJS("unexpectedError", "");
            Log.e(TAG, "Couldn't find a VirtualGood with itemId: " + itemId + ". Purchase is cancelled.");
        }
    }

    /**
     * The user wants to leave the store.
     * Clicked on "close" button.
     */
    public void wantsToLeaveStore(){
        Log.d(TAG, "wantsToLeaveStore");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mActivity.finish();
            }
        });
    }

    public void onDestroy(){
        StoreEventHandlers.getInstance().onClosingStore();
        mBillingService.unbind();
    }

    /**
     * The store's ui is ready to receive calls.
     */
    public void uiReady(){
        Log.d(TAG, "pageInitialized");
        mActivity.storeJSInitialized();
        mActivity.sendToJS("initialize", StoreInfo.getInstance().getJsonString());

        updateJSBalances();
    }

    /**
     * The store is initialized.
     */
    public void storeInitialized(){
        mActivity.loadWebView();
    }

    /**
     * Sends the virtual currency and virtual goods balances to the webview's JS.
     */
    private void updateJSBalances(){
        int currencyBalance = StorageManager.getInstance().getVirtualCurrencyStorage().getBalance();
        mActivity.sendToJS("currencyBalanceChanged", "'" + StoreConfig.CURRENCY_ITEM_ID + "'," + currencyBalance);

        for (VirtualGood good : StoreInfo.getInstance().getVirtualGoodsList()){
            int goodBalance = StorageManager.getInstance().getVirtualGoodsStorage().getBalance(good);

            mActivity.sendToJS("goodsBalanceChanged", "'" + good.getItemId() + "'," + goodBalance);
        }
    }

    /** Private members **/

    private static final String TAG = "SOOMLA StoreController";

    private BillingService mBillingService;
    private Handler        mHandler;
    private StoreActivity  mActivity;
}
