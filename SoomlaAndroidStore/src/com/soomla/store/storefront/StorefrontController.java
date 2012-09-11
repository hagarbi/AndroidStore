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
package com.soomla.store.storefront;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import com.soomla.store.IStoreEventHandler;
import com.soomla.store.StoreEventHandlers;
import com.soomla.store.data.StorageManager;
import com.soomla.store.data.StorefrontInfo;
import com.soomla.store.domain.data.VirtualCurrency;
import com.soomla.store.domain.data.VirtualCurrencyPack;
import com.soomla.store.domain.data.VirtualGood;
import org.json.JSONException;
import org.json.JSONObject;

public class StorefrontController implements IStoreEventHandler {

    public void openStore(Activity activity, IStorefrontAssets storefrontAssets){
        StorefrontInfo.getInstance().initialize(storefrontAssets);

        Intent intent = new Intent(activity.getApplicationContext(), StorefrontActivity.class);
        activity.startActivity(intent);

        StoreEventHandlers.getInstance().addEventHandler(this);
    }

    public void registerStorefrontActivity(StorefrontActivity storefrontActivity){
        this.mActivity = storefrontActivity;
    }

    @Override
    public void onVirtualCurrencyPackPurchased(VirtualCurrencyPack pack) {
        try {
            JSONObject jsonObject = new JSONObject();
            VirtualCurrency virtualCurrency = pack.getVirtualCurrency();
            jsonObject.put(virtualCurrency.getItemId(), StorageManager.getInstance()
                    .getVirtualCurrencyStorage().getBalance(pack.getVirtualCurrency()));

            mActivity.sendToJS("currencyBalanceChanged", jsonObject.toString());
        } catch (JSONException e) {
            Log.e(TAG, "couldn't generate json to return balance.");
        }
    }

    @Override
    public void onVirtualGoodPurchased(VirtualGood good) {
        mActivity.getStoreJS().updateContentInJS();
    }

    @Override
    public void onBillingSupported() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onBillingNotSupported() {
        mActivity.sendToJS("disableCurrencyStore", "");
    }

    @Override
    public void onMarketPurchaseProcessStarted() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onGoodsPurchaseProcessStarted() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onClosingStore() {
        StoreEventHandlers.getInstance().removeEventHandler(this);
    }

    @Override
    public void onUnexpectedErrorInStore() {
        mActivity.sendToJS("unexpectedError", "");
    }

    @Override
    public void onOpeningStore() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /** Singleton **/

    private static StorefrontController sInstance = null;

    public static StorefrontController getInstance(){
        if (sInstance == null){
            sInstance = new StorefrontController();
        }

        return sInstance;
    }

    private StorefrontController() {
    }

    /** Private Members**/

    private static final String TAG = "SOOMLA StorefrontController";

    private StorefrontActivity mActivity;
}
