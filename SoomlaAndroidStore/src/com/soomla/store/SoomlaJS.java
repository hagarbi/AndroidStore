package com.soomla.store;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import com.soomla.billing.BillingService;
import com.soomla.billing.Consts;
import com.soomla.store.data.StorageManager;
import com.soomla.store.domain.VirtualGood;
import com.soomla.store.exceptions.VirtualItemNotFoundException;


public class SoomlaJS {

    public SoomlaJS(Context mContext, BillingService mBillingService, Handler mHandler, SoomlaStoreActivity mActivity) {
        this.mContext = mContext;
        this.mBillingService = mBillingService;
        this.mHandler = mHandler;
        this.mActivity = mActivity;
    }

    public void wantsToBuyCurrencyPacks(String productId){
        Log.v(TAG, "wantsToBuyCurrencyPacks " + productId);

        mBillingService.requestPurchase("android.test.purchased", Consts.ITEM_TYPE_INAPP, "");

        // TODO: implement according to productId
    }

    public void wantsToBuyVirtualGoods(String itemId) {
        Log.v(TAG, "wantsToBuyVirtualGoods " + itemId);
        try {
            VirtualGood good = StoreInfo.getInstance().getVirtualGoodBySoomlaId(itemId);

            int balance = StorageManager.getInstance().getVirtualGoodsStorage().add(good, 1);
            StorageManager.getInstance().getVirtualCurrencyStorage().remove(good.getmCurrencyValue());

            mActivity.sendSoomlaJS("goodsPurchase", "true," + itemId + "," + balance + ",''");
        } catch (VirtualItemNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void wantsToLeaveStore(){
        Log.v(TAG, "wantsToLeaveStore");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mActivity.finish();
            }
        });
    }

    public void pageInitialized(){
        Log.v(TAG, "pageInitialized");
        mActivity.storeJSInitialized();
        mActivity.sendSoomlaJS("initialize", StoreInfo.getInstance().getJsonString());
    }

    private static final String TAG = "SoomlaJS";

    private Context mContext;
    private BillingService mBillingService;
    private Handler mHandler;
    private SoomlaStoreActivity mActivity;
}
