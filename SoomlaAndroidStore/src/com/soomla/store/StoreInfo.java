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

import android.util.Log;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soomla.store.domain.data.VirtualCurrency;
import com.soomla.store.domain.data.VirtualCurrencyPack;
import com.soomla.store.domain.data.VirtualGood;
import com.soomla.store.domain.ui.StoreTemplate;
import com.soomla.store.exceptions.VirtualItemNotFoundException;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * This class also holds and serves the pointer to the single {@link com.soomla.store.domain.data.VirtualCurrency}
 * in the entire application.
 */
public class StoreInfo {

    public static StoreInfo getInstance(){
        if (sInstance == null){
            sInstance = new StoreInfo();
        }

        return sInstance;
    }

    /**
     * This function initializes StoreInfo using the given store assets.
     */
    public void initialize(IStoreAssets storeAssets){
        if (storeAssets == null){
            Log.e(TAG, "The given store assets can't be null !");
            return;
        }

        mVirtualCurrency = storeAssets.getVirtualCurrency();
        mPacksOptions = new HashMap<String, VirtualCurrencyPack>();
        for(VirtualCurrencyPack pack : storeAssets.getVirtualCurrencyPacks()){
            mPacksOptions.put(pack.getmGoogleItem().getMarketId(), pack);
        }

        mVirtualGoodOptions = new HashMap<String, VirtualGood>();
        for(VirtualGood good : storeAssets.getVirtualGoods()){
            mVirtualGoodOptions.put(good.getItemId(), good);
        }

        mStoreBackground = storeAssets.getStoreBackground();
        mTemplate = storeAssets.getStoreTemplate();
    }

    /**
     * Use this function if you need to know the definition of a specific virtual currency pack.
     * @param productId is the requested pack's product id.
     * @return the definition of the virtual pack requested.
     * @throws VirtualItemNotFoundException
     */
    public VirtualCurrencyPack getPackByGoogleProductId(String productId) throws VirtualItemNotFoundException {{
        if (!mPacksOptions.containsKey(productId)){
            throw new VirtualItemNotFoundException("productId", productId);
        }
    }
        return mPacksOptions.get(productId);
    }

    /**
     * Use this function if you need to know the definition of a specific virtual good.
     * @param itemId is the requested good's item id.
     * @return the definition of the virtual good requested.
     * @throws VirtualItemNotFoundException
     */
    public VirtualGood getVirtualGoodByItemId(String itemId) throws VirtualItemNotFoundException {
        if (!mVirtualGoodOptions.containsKey(itemId)){
            throw new VirtualItemNotFoundException("itemId", itemId);
        }

        return mVirtualGoodOptions.get(itemId);
    }


    /** Getters **/

    @JsonProperty("currency")
    public VirtualCurrency getVirtualCurrency(){
        return mVirtualCurrency;
    }

    @JsonProperty("currencyPacks")
    public List<VirtualCurrencyPack> getCurrencyPacksList() {
        return new LinkedList<VirtualCurrencyPack>(mPacksOptions.values());
    }

    @JsonProperty("virtualGoods")
    public List<VirtualGood> getVirtualGoodsList() {
        return new LinkedList<VirtualGood>(mVirtualGoodOptions.values());
    }

    public StoreTemplate getTemplate() {
        return mTemplate;
    }

    @JsonProperty("background")
    public String getStoreBackground() {
        return mStoreBackground;
    }

    @JsonProperty("isCurrencyStoreDisabled")
    public boolean isIsCurrencyStoreDisabled() {
        return mIsCurrencyStoreDisabled;
    }

    /**
     * Use this function to get a json representation of StoreInfo.
     * @return a json representation of StoreInfo.
     */
    @JsonIgnore
    public String getJsonString(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    /** Private functions **/

    private StoreInfo() { }


    /** Private members **/

    private static final String TAG = "SOOMLA StoreInfo";
    private static StoreInfo                        sInstance = null;

    private VirtualCurrency                         mVirtualCurrency;
    private HashMap<String, VirtualCurrencyPack>    mPacksOptions;
    private HashMap<String, VirtualGood>            mVirtualGoodOptions;
    private StoreTemplate                           mTemplate;
    private String                                  mStoreBackground;
    private boolean                                 mIsCurrencyStoreDisabled;
}