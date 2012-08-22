package com.soomla.store;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import com.soomla.billing.BillingService;
import com.soomla.billing.Consts;
import com.soomla.store.data.StorageManager;
import com.soomla.store.domain.VirtualGood;
import com.soomla.store.exceptions.VirtualItemNotFoundException;


public class SoomlaStore {

    public SoomlaStore(Context mContext,
                       BillingService mBillingService,
                       Handler mHandler,
                       SoomlaStoreActivity mActivity,
                       ISoomlaStoreEventHandler eventHandler) {
        this.mContext = mContext;
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
                int balance = StorageManager.getInstance().getVirtualGoodsStorage().add(good, 1);
                StorageManager.getInstance().getVirtualCurrencyStorage().remove(good.getmCurrencyValue());

                mActivity.sendSoomlaJS("goodsBalanceChanged", "'" + itemId + "'," + balance);
                if (mEventHandler != null){
                    mEventHandler.onVirtualGoodPurchased(good);
                }
            }
            else {
                int balance = StorageManager.getInstance().getVirtualGoodsStorage().getBalance(good);
                String failureMsg = "You don\'t have enough " + StoreInfo.getInstance().getVirtualCurrency().getName() + " to buy a " + good.getName() + ".";
                mActivity.sendSoomlaJS("showInsufficientFundsDialog", "" + balance);
            }
        } catch (VirtualItemNotFoundException e) {
            mActivity.sendSoomlaJS("showUnexpectedErrorDialog", "");
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
    }

    private static final String TAG = "SOOMLA SoomlaStore";

    private Context mContext;
    private BillingService mBillingService;
    private Handler mHandler;
    private SoomlaStoreActivity mActivity;
    private ISoomlaStoreEventHandler mEventHandler;
}
