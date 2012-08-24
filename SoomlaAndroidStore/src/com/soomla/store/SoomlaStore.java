package com.soomla.store;

import android.os.Handler;
import android.util.Log;
import com.soomla.billing.BillingService;
import com.soomla.billing.Consts;
import com.soomla.store.data.StorageManager;
import com.soomla.store.domain.data.VirtualGood;
import com.soomla.store.exceptions.VirtualItemNotFoundException;


public class SoomlaStore {

    public SoomlaStore(BillingService mBillingService,
                       Handler mHandler,
                       SoomlaStoreActivity mActivity,
                       ISoomlaStoreEventHandler eventHandler) {
        this.mBillingService = mBillingService;
        this.mHandler = mHandler;
        this.mActivity = mActivity;
        this.mEventHandler = eventHandler;
    }

    public void wantsToBuyCurrencyPacks(String productId){
        Log.d(TAG, "wantsToBuyCurrencyPacks " + productId);

        mBillingService.requestPurchase(productId, Consts.ITEM_TYPE_INAPP, "");
    }

    public void wantsToBuyVirtualGoods(String itemId) {
        Log.d(TAG, "wantsToBuyVirtualGoods " + itemId);
        try {
            VirtualGood good = StoreInfo.getInstance().getVirtualGoodBySoomlaId(itemId);

            if (StorageManager.getInstance().getVirtualCurrencyStorage().getBalance() >= good.getmCurrencyValue()){
                StorageManager.getInstance().getVirtualGoodsStorage().add(good, 1);
                StorageManager.getInstance().getVirtualCurrencyStorage().remove(good.getmCurrencyValue());

                updateJSBalances();

                if (mEventHandler != null){
                    mEventHandler.onVirtualGoodPurchased(good);
                }
            }
            else {
                int balance = StorageManager.getInstance().getVirtualGoodsStorage().getBalance(good);
                String failureMsg = "You don\'t have enough " + StoreInfo.getInstance().getVirtualCurrency().getName() + " to buy a " + good.getName() + ".";
                mActivity.sendSoomlaJS("insufficientFunds", "" + balance);
            }
        } catch (VirtualItemNotFoundException e) {
            mActivity.sendSoomlaJS("unexpectedError", "");
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
        mActivity.sendSoomlaJS("initialize", StoreInfo.getInstance().getJsonString());

        updateJSBalances();

        mActivity.loadWebView();
    }

    private void updateJSBalances(){
        int currencyBalance = StorageManager.getInstance().getVirtualCurrencyStorage().getBalance();
        mActivity.sendSoomlaJS("currencyBalanceChanged", "'" + SoomlaPrefs.CURRENCY_ITEM_ID + "'," + currencyBalance);

        for (VirtualGood good : StoreInfo.getInstance().getVirtualGoodsList()){
            int goodBalance = StorageManager.getInstance().getVirtualGoodsStorage().getBalance(good);

            mActivity.sendSoomlaJS("goodsBalanceChanged", "'" + good.getItemId() + "'," + goodBalance);
        }
    }

    private static final String TAG = "SOOMLA SoomlaStore";

    private BillingService mBillingService;
    private Handler mHandler;
    private SoomlaStoreActivity mActivity;
    private ISoomlaStoreEventHandler mEventHandler;
}
