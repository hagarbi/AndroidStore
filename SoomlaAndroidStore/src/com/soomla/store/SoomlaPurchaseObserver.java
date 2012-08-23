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
import com.soomla.store.domain.data.VirtualCurrencyPack;
import com.soomla.store.exceptions.VirtualItemNotFoundException;

/**
 * A {@link SoomlaPurchaseObserver} is used to get callbacks when Android Market sends
 * messages to this application so that we can update the UI.
 */
public class SoomlaPurchaseObserver extends PurchaseObserver {

    public SoomlaPurchaseObserver(Handler handler, SoomlaStoreActivity activity, ISoomlaStoreEventHandler eventHandler) {
        super(activity, handler);

        mActivity = activity;
        mEventHandler = eventHandler;
    }

    @Override
    public void onBillingSupported(boolean supported, String type) {
        if (type == null || type.equals(Consts.ITEM_TYPE_INAPP)) {
            if (supported) {
                if (SoomlaPrefs.debug){
                    Log.d(TAG, "billing is supported !");
                }
            } else {
                // purchase is not supported. just send a message to JS to disable the "get more ..." button.

                if (SoomlaPrefs.debug){
                    Log.d(TAG, "billing is not supported !");
                }

                mActivity.sendSoomlaJS("disableCurrencyStore", "");
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

        int balance = StorageManager.getInstance().getVirtualCurrencyStorage().getBalance();

        try {
            VirtualCurrencyPack pack = StoreInfo.getInstance().getPackByGoogleProductId(productId);

            if (purchaseState == Consts.PurchaseState.PURCHASED ||
                    purchaseState == Consts.PurchaseState.REFUNDED) {

                // we're throwing this event when on PURCHASE or REFUND !

                mActivity.sendSoomlaJS("currencyBalanceChanged", "'" + SoomlaPrefs.CURRENCY_ITEM_ID + "'," + balance);
                if (mEventHandler != null){
                    mEventHandler.onVirtualCurrencyPackPurchased(pack);
                }
            }

        } catch (VirtualItemNotFoundException e) {
            mActivity.sendSoomlaJS("showUnexpectedErrorDialog", "");
            Log.e(TAG, "ERROR : Couldn't find VirtualCurrencyPack with productId: " + productId);
        }
    }

    @Override
    public void onRequestPurchaseResponse(BillingService.RequestPurchase request,
                                          Consts.ResponseCode responseCode) {
        int balance = StorageManager.getInstance().getVirtualCurrencyStorage().getBalance();

        if (responseCode == Consts.ResponseCode.RESULT_OK) {
            // purchase was sent to server
        } else if (responseCode == Consts.ResponseCode.RESULT_USER_CANCELED) {

            // purchase canceled by user... doing nothing for now.

        } else {
           // purchase failed !

           mActivity.sendSoomlaJS("showUnexpectedErrorDialog", "");
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

    private static final String TAG = "SOOMLA SoomlaPurchaseObserver";

    private SoomlaStoreActivity      mActivity;
    private ISoomlaStoreEventHandler mEventHandler;
}
