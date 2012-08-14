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
import com.soomla.store.domain.VirtualCurrencyPack;
import com.soomla.store.exceptions.VirtualItemNotFoundException;

/**
 * A {@link SoomlaPurchaseObserver} is used to get callbacks when Android Market sends
 * messages to this application so that we can update the UI.
 */
public class SoomlaPurchaseObserver extends PurchaseObserver {

    public SoomlaPurchaseObserver(Handler handler, SoomlaStoreActivity activity) {
        super(activity, handler);

        mActivity = activity;
    }

    @Override
    public void onBillingSupported(boolean supported, String type) {
        if (type == null || type.equals(Consts.ITEM_TYPE_INAPP)) {
            if (supported) {
                // TODO: handle purchase enabled. Initialize view and datastore (FIle Or Database).
                Log.v(TAG, "billing is supported !");
            } else {
                // TODO: see if needs a change. Maybe close the store when billing is not supported ?!
                //       and maybe not b/c the user may still buy goods with coins.
                //       best solution is to just tell webview to disable "buy more coins" button
                mActivity.showDialog(SoomlaConsts.DIALOG_BILLING_NOT_SUPPORTED_ID);

                mActivity.sendSoomlaJS("disableCoinsStore", "");
            }
        } else if (type.equals(Consts.ITEM_TYPE_SUBSCRIPTION)) {
            // TODO: subscription is supported. Do we want to notify someone ?
        } else {
            mActivity.showDialog(SoomlaConsts.DIALOG_SUBSCRIPTIONS_NOT_SUPPORTED_ID);
        }
    }

    @Override
    public void onPurchaseStateChange(Consts.PurchaseState purchaseState, String productId,
                                      long purchaseTime, String developerPayload) {

        try {
            VirtualCurrencyPack pack = StoreInfo.getInstance().getPackByGoogleProductId(productId);

            int balance = StorageManager.getInstance().getVirtualCurrencyStorage().getBalance();

            // TODO: check if we need to handle PurchaseState.REFUNDED here

            if (purchaseState == Consts.PurchaseState.PURCHASED) {
                // TODO: the item was purchased, add it to the store according to itemId
                mActivity.sendSoomlaJS("vGoodsPurchaseEnded", "true," + pack.getItemId() + "," + balance + ",''");
            }
            else if(purchaseState == Consts.PurchaseState.CANCELED){
                mActivity.sendSoomlaJS("vGoodsPurchaseEnded", "false," + pack.getItemId() + "," + balance + ",'You canceled the purchase'");
            }

            // TODO: refresh UI here
        } catch (VirtualItemNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPurchaseResponse(BillingService.RequestPurchase request,
                                          Consts.ResponseCode responseCode) {
        VirtualCurrencyPack pack = null;
        try {
            pack = StoreInfo.getInstance().getPackByGoogleProductId(request.mProductId);
            int balance = StorageManager.getInstance().getVirtualCurrencyStorage().getBalance();

            if (responseCode == Consts.ResponseCode.RESULT_OK) {
                // purchase was sent to server
            } else if (responseCode == Consts.ResponseCode.RESULT_USER_CANCELED) {
                // purchase canceled by user
                // TODO: tell webview

                mActivity.sendSoomlaJS("vGoodsPurchaseEnded", "false," + pack.getItemId() + "," + balance + ",'You canceled the purchase.'");
            } else {
                // purchase failed !
                // TODO: tell webview
                mActivity.sendSoomlaJS("vGoodsPurchaseEnded", "false," + pack.getItemId() + "," + balance + ",'Unexpected error occured! Your purchase is canceled.'");
            }
        } catch (VirtualItemNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRestoreTransactionsResponse(BillingService.RestoreTransactions request,
                                              Consts.ResponseCode responseCode) {
        if (responseCode == Consts.ResponseCode.RESULT_OK) {
            // RestoreTransaction succeeded !
        } else {
            // RestoreTransaction error !
        }
    }

    private static final String TAG = "SoomlaPurchaseObserver";

    private SoomlaStoreActivity mActivity;
}
