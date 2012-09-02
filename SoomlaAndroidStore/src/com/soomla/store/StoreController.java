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
import android.text.TextUtils;
import android.util.Log;
import com.soomla.billing.BillingService;
import com.soomla.billing.Consts;
import com.soomla.store.data.StorageManager;
import com.soomla.store.data.StoreInfo;
import com.soomla.store.domain.data.VirtualCurrency;
import com.soomla.store.domain.data.VirtualGood;
import com.soomla.store.exceptions.VirtualItemNotFoundException;
import org.json.JSONException;
import org.json.JSONObject;

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

            HashMap<String, Integer> currencyValues = good.getCurrencyValues();
            String insufficientItemId = "";
            for (String currencyItemId : currencyValues.keySet()){
                int currencyBalance = StorageManager.getInstance().getVirtualCurrencyStorage().getBalance
                        (currencyItemId);
                if (currencyBalance < currencyValues.get(currencyItemId)){
                    insufficientItemId = currencyItemId;
                    break;
                }
            }
            if (TextUtils.isEmpty(insufficientItemId)){
                StorageManager.getInstance().getVirtualGoodsStorage().add(good, 1);
                for (String currencyItemId : currencyValues.keySet()){
                    StorageManager.getInstance().getVirtualCurrencyStorage().remove(currencyItemId,
                            currencyValues.get(currencyItemId));
                }

                updateContentInJS();

                StoreEventHandlers.getInstance().onVirtualGoodPurchased(good);
            }
            else {
                mActivity.sendToJS("insufficientFunds", "" + insufficientItemId);
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
        if (StoreConfig.debug){
            Log.d(TAG, "uiReady");
        }
        mActivity.JSuiReady();
        mActivity.sendToJS("initialize", StoreInfo.getInstance().getJsonString());

        updateContentInJS();
    }

    /**
     * The store is initialized.
     */
    public void storeInitialized(){
        if (StoreConfig.debug){
            Log.d(TAG, "storeInitialized");
        }
        mActivity.loadWebView();
    }

    /**
     * Sends the virtual currency and virtual goods updated data to the webview's JS.
     */
    private void updateContentInJS(){
        try {
            JSONObject jsonObject = new JSONObject();
            for(VirtualCurrency virtualCurrency : StoreInfo.getInstance().getVirtualCurrencies()){
                jsonObject.put(virtualCurrency.getItemId(),
                        StorageManager.getInstance().getVirtualCurrencyStorage().getBalance(virtualCurrency.getItemId()));
            }

            mActivity.sendToJS("currencyBalanceChanged", jsonObject.toString());

            jsonObject = new JSONObject();
            for (VirtualGood good : StoreInfo.getInstance().getVirtualGoods()){
                JSONObject updatedValues = new JSONObject();
                updatedValues.put("balance", StorageManager.getInstance().getVirtualGoodsStorage().getBalance(good));
                updatedValues.put("price", good.getCurrencyValuesAsJSONObject());

                jsonObject.put(good.getItemId(), updatedValues);
            }

            mActivity.sendToJS("goodsUpdated", jsonObject.toString());

        } catch (JSONException e) {
            if (StoreConfig.debug){
                Log.d(TAG, "couldn't generate json to send balances");
            }
        }
    }

    /** Private members **/

    private static final String TAG = "SOOMLA StoreController";

    private BillingService mBillingService;
    private Handler        mHandler;
    private StoreActivity  mActivity;
}
