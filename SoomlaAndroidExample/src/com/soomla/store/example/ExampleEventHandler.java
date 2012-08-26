package com.soomla.store.example;

import android.content.Context;
import android.widget.Toast;
import com.soomla.store.IStoreEventHandler;
import com.soomla.store.domain.data.VirtualCurrencyPack;
import com.soomla.store.domain.data.VirtualGood;

public class ExampleEventHandler implements IStoreEventHandler {

    private Context mContext;
    public ExampleEventHandler(Context context){
        mContext = context;
    }

    @Override
    public void onVirtualCurrencyPackPurchased(VirtualCurrencyPack pack) {
        Toast toast = Toast.makeText(mContext, pack.getName() + " was just purchased", 5000);
        toast.show();
    }

    @Override
    public void onVirtualGoodPurchased(VirtualGood good) {
        Toast toast = Toast.makeText(mContext, good.getName() + " was just purchased", 5000);
        toast.show();
    }

    @Override
    public void onBillingSupported() {
        Toast toast = Toast.makeText(mContext, "Billing is supported", 5000);
        toast.show();
    }

    @Override
    public void onBillingNotSupported() {
        Toast toast = Toast.makeText(mContext, "Billing is not supported", 5000);
        toast.show();
    }

    @Override
    public void onMarketPurchaseProcessStarted() {
        Toast toast = Toast.makeText(mContext, "Market purchase started", 5000);
        toast.show();
    }

    @Override
    public void onGoodsPurchaseProcessStarted() {
        Toast toast = Toast.makeText(mContext, "Goods purchase started", 5000);
        toast.show();
    }

    @Override
    public void onClosingStore() {
        Toast toast = Toast.makeText(mContext, "Going to close store", 5000);
        toast.show();
    }
}
