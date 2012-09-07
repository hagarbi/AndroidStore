package com.soomla.store.example;

import android.content.Context;
import android.widget.Toast;
import com.soomla.store.IStoreEventHandler;
import com.soomla.store.StoreConfig;
import com.soomla.store.domain.data.VirtualCurrencyPack;
import com.soomla.store.domain.data.VirtualGood;

public class ExampleEventHandler implements IStoreEventHandler {

    private Context mContext;
    private StoreExampleActivity mActivity;
    public ExampleEventHandler(Context context, StoreExampleActivity activity){
        mContext = context;
        mActivity = activity;
    }

    @Override
    public void onVirtualCurrencyPackPurchased(VirtualCurrencyPack pack) {
        showToastIfDebug(pack.getName() + " was just purchased");
    }

    @Override
    public void onVirtualGoodPurchased(VirtualGood good) {
        showToastIfDebug(good.getName() + " was just purchased");
    }

    @Override
    public void onBillingSupported() {
        showToastIfDebug("Billing is supported");
    }

    @Override
    public void onBillingNotSupported() {
        showToastIfDebug("Billing is not supported");
    }

    @Override
    public void onMarketPurchaseProcessStarted() {
        showToastIfDebug("Market purchase started");
    }

    @Override
    public void onGoodsPurchaseProcessStarted() {
        showToastIfDebug("Goods purchase started");
    }

    @Override
    public void onClosingStore() {
        mActivity.robotBackHome();

        showToastIfDebug("Going to close store");
    }

    private void showToastIfDebug(String msg) {
        if (StoreConfig.debug){
            Toast toast = Toast.makeText(mContext, msg, 5000);
            toast.show();
        }
    }
}
