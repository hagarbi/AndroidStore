package com.soomla.store;

import com.soomla.store.domain.VirtualCurrency;
import com.soomla.store.domain.VirtualCurrencyPack;

import java.util.HashMap;

/**
 * This class also holds and serves the pointer to the single {@link com.soomla.store.domain.VirtualCurrency}
 * in the entire application.
 */
public class StoreInfo {

    public static StoreInfo getInstance(){
        if (mInstance == null){
            mInstance = new StoreInfo();
        }

        return mInstance;
    }

    public void initialize(VirtualCurrency mVirtualCurrency, HashMap<String, VirtualCurrencyPack> mPacksOptions){
        this.mVirtualCurrency = mVirtualCurrency;
        this.mPacksOptions = mPacksOptions;
    }

    public VirtualCurrency getVirtualCurrency(){
        return mVirtualCurrency;
    }

    public VirtualCurrencyPack getPackByGoogleProductId(String productId){
        return mPacksOptions.get(productId);
    }

    private StoreInfo() { }

    private static StoreInfo                        mInstance = null;
    private VirtualCurrency                         mVirtualCurrency;
    private HashMap<String, VirtualCurrencyPack>    mPacksOptions;
}