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
import android.util.Log;
import com.soomla.billing.BillingService;
import com.soomla.billing.Consts;
import com.soomla.billing.PurchaseObserver;
import com.soomla.store.data.StorageManager;
import com.soomla.store.data.StoreInfo;
import com.soomla.store.domain.data.VirtualCurrencyPack;
import com.soomla.store.exceptions.VirtualItemNotFoundException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A {@link StorePurchaseObserver} is used to get callbacks when Android Market sends
 * messages to this application so that we can update the UI.
 */
public class StorePurchaseObserver extends PurchaseObserver {

    public StorePurchaseObserver(Handler handler, StoreActivity activity) {
        super(activity, handler);

        mActivity = activity;
    }

    @Override
    public void onBillingSupported(boolean supported, String type) {
        if (type == null || type.equals(Consts.ITEM_TYPE_INAPP)) {
            if (supported) {
                if (StoreConfig.debug){
                    Log.d(TAG, "billing is supported !");
                }
                StoreEventHandlers.getInstance().onBillingSupported();
            } else {
                // purchase is not supported. just send a message to JS to disable the "get more ..." button.

                if (StoreConfig.debug){
                    Log.d(TAG, "billing is not supported !");
                }

                mActivity.sendToJS("disableCurrencyStore", "");
                StoreEventHandlers.getInstance().onBillingNotSupported();
            }
        } else if (type.equals(Consts.ITEM_TYPE_SUBSCRIPTION)) {
            // subscription is not supported
            // Soomla doesn't support subscriptions yet. doing nothing here ...
        } else {
            // subscription is not supported
            // Soomla doesn't support subscriptions yet. doing nothing here ...
        }
    }

    @Override
    public void onPurchaseStateChange(Consts.PurchaseState purchaseState, String productId,
                                      long purchaseTime, String developerPayload) {

        try {

            if (purchaseState == Consts.PurchaseState.PURCHASED ||
                    purchaseState == Consts.PurchaseState.REFUNDED) {

                // we're throwing this event when on PURCHASE or REFUND !

                VirtualCurrencyPack pack = StoreInfo.getInstance().getPackByGoogleProductId(productId);

                JSONObject jsonObject = new JSONObject();
                String currencyItemId = pack.getmCurrency().getItemId();
                jsonObject.put(currencyItemId, StorageManager.getInstance()
                        .getVirtualCurrencyStorage().getBalance(currencyItemId));

                mActivity.sendToJS("currencyBalanceChanged", jsonObject.toString());

                StoreEventHandlers.getInstance().onVirtualCurrencyPackPurchased(pack);
            }

        } catch (VirtualItemNotFoundException e) {
            mActivity.sendToJS("unexpectedError", "");
            Log.e(TAG, "ERROR : Couldn't find VirtualCurrencyPack with productId: " + productId);
        } catch (JSONException e) {
            Log.e(TAG, "couldn't generate json to return balance.");
        }
    }

    @Override
    public void onRequestPurchaseResponse(BillingService.RequestPurchase request,
                                          Consts.ResponseCode responseCode) {

        if (responseCode == Consts.ResponseCode.RESULT_OK) {
            // purchase was sent to server
        } else if (responseCode == Consts.ResponseCode.RESULT_USER_CANCELED) {

            // purchase canceled by user... doing nothing for now.

        } else {
           // purchase failed !

           mActivity.sendToJS("unexpectedError", "");
        }
    }

    @Override
    public void onRestoreTransactionsResponse(BillingService.RestoreTransactions request,
                                              Consts.ResponseCode responseCode) {

        // THIS IS FOR MANAGED ITEMS. SOOMLA DOESN'T SUPPORT MANAGED ITEMS.

        if (responseCode == Consts.ResponseCode.RESULT_OK) {
            // RestoreTransaction succeeded !
        } else {
            // RestoreTransaction error !
        }
    }

    private static final String TAG = "SOOMLA StorePurchaseObserver";

    private StoreActivity mActivity;
}
