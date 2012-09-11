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
import android.os.Handler;
import android.util.Log;
import com.soomla.billing.BillingService;
import com.soomla.billing.Consts;
import com.soomla.billing.PurchaseObserver;
import com.soomla.billing.ResponseHandler;
import com.soomla.store.data.StorageManager;
import com.soomla.store.data.StoreInfo;
import com.soomla.store.domain.data.VirtualCurrency;
import com.soomla.store.domain.data.VirtualCurrencyPack;
import com.soomla.store.domain.data.VirtualGood;
import com.soomla.store.exceptions.InsufficientFundsException;
import com.soomla.store.exceptions.VirtualItemNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class is where all the important stuff happens. You can use it to purchase products from Google Play,
 * buy virtual goods, and get events on whatever happens.
 *
 * This is the only class you need to initialize in order to use the SOOMLA SDK. If you use the UI,
 * you'll need to also use {@link com.soomla.store.storefront.StorefrontActivity}.
 *
 * In addition to initializing this class, you'll also have to call
 * {@link StoreController#storeOpening(android.app.Activity, android.os.Handler)} and
 * {@link com.soomla.store.StoreController#storeClosing()} when you open the store window or close it. These two
 * calls initializes important components that support billing and storage information (see implementation below).
 * IMPORTANT: if you use the SOOMLA storefront (SOOMLA Storefront), than DON'T call these 2 functions.
 *
 */
public class StoreController extends PurchaseObserver {

    /**
     * If you're using SOOMLA's UI, You have to initialize the {@link StoreController} before you
     * open the {@link com.soomla.store.storefront.StorefrontActivity}.
     * This initializer also initializes {@link StorageManager} and {@link StoreInfo}.
     * @param context is used to initialize {@link StorageManager}
     * @param storeAssets is the definition of your application specific assets.
     * @param publicKey is your public key from Google Play.
     * @param debugMode is determining weather you're on debug mode or not (duh !!!).
     */
    public void initialize(Context context,
                           IStoreAssets storeAssets,
                           String publicKey,
                           boolean debugMode){

        StoreConfig.publicKey = publicKey;
        StoreConfig.debug = debugMode;

        StorageManager.getInstance().initialize(context);
        StoreInfo.getInstance().initialize(storeAssets);
    }

    /**
     * Start a currency pack purchase process (with Google Play)
     * @param productId is the product id of the required currency pack.
     */
    public void buyCurrencyPack(String productId){
        StoreEventHandlers.getInstance().onMarketPurchaseProcessStarted();
        mBillingService.requestPurchase(productId, Consts.ITEM_TYPE_INAPP, "");
    }

    /**
     * Start a virtual goods purchase process.
     * @param itemId is the item id of the required virtual good.
     * @throws InsufficientFundsException
     * @throws VirtualItemNotFoundException
     */
    public void buyVirtualGood(String itemId) throws InsufficientFundsException, VirtualItemNotFoundException{
        StoreEventHandlers.getInstance().onGoodsPurchaseProcessStarted();
        VirtualGood good = StoreInfo.getInstance().getVirtualGoodByItemId(itemId);

        // fetching currencies and amounts that the user needs in order to purchase the current
        // {@link VirtualGood}.
        HashMap<String, Integer> currencyValues = good.getCurrencyValues();

        // preparing list of {@link VirtualCurrency} objects.
        List<VirtualCurrency> virtualCurrencies = new ArrayList<VirtualCurrency>();
        for (String currencyItemId : currencyValues.keySet()){
            virtualCurrencies.add(StoreInfo.getInstance().getVirtualCurrencyByItemId(currencyItemId));
        }

        // checking if the user has enough of each of the virtual currencies in order to purchase this virtual
        // good.
        VirtualCurrency needMore = null;
        for (VirtualCurrency virtualCurrency : virtualCurrencies){
            int currencyBalance = StorageManager.getInstance().getVirtualCurrencyStorage().getBalance
                    (virtualCurrency);
            int currencyBalanceNeeded = currencyValues.get(virtualCurrency.getItemId());
            if (currencyBalance < currencyBalanceNeeded){
                needMore = virtualCurrency;
                break;
            }
        }

        // if the user has enough, the virtual good is purchased.
        if (needMore == null){
            StorageManager.getInstance().getVirtualGoodsStorage().add(good, 1);
            for (VirtualCurrency virtualCurrency : virtualCurrencies){
                int currencyBalanceNeeded = currencyValues.get(virtualCurrency.getItemId());
                StorageManager.getInstance().getVirtualCurrencyStorage().remove(virtualCurrency,
                        currencyBalanceNeeded);
            }

            StoreEventHandlers.getInstance().onVirtualGoodPurchased(good);
        }
        else {
            throw new InsufficientFundsException(needMore.getItemId());
        }
    }

    /**
     * Call this function when you open the actual store window
     * @param activity is the activity being opened (or the activity that contains the store)/
     * @param handler is a handler to post UI thread messages on.
     */
    public void storeOpening(Activity activity, Handler handler){
        initialize(activity, handler);

        StoreInfo.getInstance().initializeFromDB();

        /* Billing */

        mBillingService = new BillingService();
        mBillingService.setContext(activity.getApplicationContext());

        if (!mBillingService.checkBillingSupported(Consts.ITEM_TYPE_INAPP)){
            if (StoreConfig.debug){
                Log.d(TAG, "There's no connectivity with the billing service.");
            }
        }

        ResponseHandler.register(this);

        StoreEventHandlers.getInstance().onOpeningStore();
    }

    /**
     * Call this function when you close the actual store window.
     */
    public void storeClosing(){
        StoreEventHandlers.getInstance().onClosingStore();

        mBillingService.unbind();
        ResponseHandler.unregister(this);
    }


    /** PurchaseObserver overridden functions**/

    /**
     * docs in {@link PurchaseObserver#onBillingSupported(boolean supported, String type)}.
     */
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

    /**
     * docs in {@link PurchaseObserver#onPurchaseStateChange(com.soomla.billing.Consts.PurchaseState, String, long, String)}.
     */
    @Override
    public void onPurchaseStateChange(Consts.PurchaseState purchaseState, String productId, long purchaseTime, String developerPayload) {
        try {

            if (purchaseState == Consts.PurchaseState.PURCHASED ||
                    purchaseState == Consts.PurchaseState.REFUNDED) {

                // we're throwing this event when on PURCHASE or REFUND !

                VirtualCurrencyPack pack = StoreInfo.getInstance().getPackByGoogleProductId(productId);
                StoreEventHandlers.getInstance().onVirtualCurrencyPackPurchased(pack, purchaseState);
            }

        } catch (VirtualItemNotFoundException e) {
            StoreEventHandlers.getInstance().onUnexpectedErrorInStore();
            Log.e(TAG, "ERROR : Couldn't find VirtualCurrencyPack with productId: " + productId);
        }
    }

    /**
     * docs in {@link PurchaseObserver#onRequestPurchaseResponse(com.soomla.billing.BillingService.RequestPurchase, com.soomla.billing.Consts.ResponseCode)}.
     */
    @Override
    public void onRequestPurchaseResponse(BillingService.RequestPurchase request, Consts.ResponseCode responseCode) {
        if (responseCode == Consts.ResponseCode.RESULT_OK) {
            // purchase was sent to server
        } else if (responseCode == Consts.ResponseCode.RESULT_USER_CANCELED) {

            // purchase canceled by user... doing nothing for now.

        } else {
            // purchase failed !

            StoreEventHandlers.getInstance().onUnexpectedErrorInStore();
            Log.e(TAG, "ERROR : Purchase failed for productId: " + request.mProductId);
        }
    }

    /**
     * docs in {@link PurchaseObserver#onRestoreTransactionsResponse(com.soomla.billing.BillingService.RestoreTransactions, com.soomla.billing.Consts.ResponseCode)}.
     */
    @Override
    public void onRestoreTransactionsResponse(BillingService.RestoreTransactions request, Consts.ResponseCode responseCode) {
        // THIS IS FOR MANAGED ITEMS. SOOMLA DOESN'T SUPPORT MANAGED ITEMS.

        if (responseCode == Consts.ResponseCode.RESULT_OK) {
            // RestoreTransaction succeeded !
        } else {
            // RestoreTransaction error !
        }
    }

    /** Singleton **/

    private static StoreController sInstance = null;

    public static StoreController getInstance(){
        if (sInstance == null){
            sInstance = new StoreController();
        }

        return sInstance;
    }

    private StoreController() {
    }


    /** Private Members**/

    private static final String TAG = "SOOMLA StoreController";

    private BillingService mBillingService;
}
