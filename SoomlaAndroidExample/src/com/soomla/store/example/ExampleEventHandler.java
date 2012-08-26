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
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onVirtualGoodPurchased(VirtualGood good) {
        Toast toast = Toast.makeText(mContext, "Hey Hey Hey", 5000);
        toast.show();
    }
}
