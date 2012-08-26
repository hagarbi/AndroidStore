package com.soomla.store;

import android.os.Handler;
import android.util.Log;
import com.soomla.billing.BillingService;
import com.soomla.billing.Consts;
import com.soomla.store.data.StorageManager;
import com.soomla.store.domain.data.VirtualGood;
import com.soomla.store.exceptions.VirtualItemNotFoundException;


public class StoreController {

    public StoreController(BillingService mBillingService,
                           Handler mHandler,
                           StoreActivity mActivity) {
        this.mBillingService = mBillingService;
        this.mHandler = mHandler;
        this.mActivity = mActivity;
    }

    public void wantsToBuyCurrencyPacks(String productId){
        Log.d(TAG, "wantsToBuyCurrencyPacks " + productId);

        mBillingService.requestPurchase(productId, Consts.ITEM_TYPE_INAPP, "");
    }

    public void wantsToBuyVirtualGoods(String itemId) {
        Log.d(TAG, "wantsToBuyVirtualGoods " + itemId);
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
                String failureMsg = "You don\'t have enough " + StoreInfo.getInstance().getVirtualCurrency().getName() + " to buy a " + good.getName() + ".";
                mActivity.sendToJS("insufficientFunds", "" + balance);
            }
        } catch (VirtualItemNotFoundException e) {
            mActivity.sendToJS("unexpectedError", "");
            Log.e(TAG, "Couldn't find a VirtualGood with itemId: " + itemId + ". Purchase is cancelled.");
        }
    }

    public void wantsToLeaveStore(){
        Log.d(TAG, "wantsToLeaveStore");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mActivity.finish();
            }
        });
    }

    public void pageInitialized(){
        Log.d(TAG, "pageInitialized");
        mActivity.storeJSInitialized();
        mActivity.sendToJS("initialize", StoreInfo.getInstance().getJsonString());

        updateJSBalances();

        mActivity.loadWebView();
    }

    private void updateJSBalances(){
        int currencyBalance = StorageManager.getInstance().getVirtualCurrencyStorage().getBalance();
        mActivity.sendToJS("currencyBalanceChanged", "'" + StoreConfig.CURRENCY_ITEM_ID + "'," + currencyBalance);

        for (VirtualGood good : StoreInfo.getInstance().getVirtualGoodsList()){
            int goodBalance = StorageManager.getInstance().getVirtualGoodsStorage().getBalance(good);

            mActivity.sendToJS("goodsBalanceChanged", "'" + good.getItemId() + "'," + goodBalance);
        }
    }

    private static final String TAG = "SOOMLA StoreController";

    private BillingService mBillingService;
    private Handler mHandler;
    private StoreActivity mActivity;
}
