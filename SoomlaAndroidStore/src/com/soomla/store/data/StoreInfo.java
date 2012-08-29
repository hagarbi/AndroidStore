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
package com.soomla.store.data;

import android.util.Log;
import com.soomla.store.IStoreAssets;
import com.soomla.store.StoreConfig;
import com.soomla.store.domain.data.VirtualCurrency;
import com.soomla.store.domain.data.VirtualCurrencyPack;
import com.soomla.store.domain.data.VirtualGood;
import com.soomla.store.domain.ui.StoreTemplate;
import com.soomla.store.exceptions.VirtualItemNotFoundException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * This class holds the store's meta data including:
 * - Virtual Currency definition
 * - Virtual Currency Packs definitions
 * - Virtual Goods definitions
 * - Store UI Template
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

        mVirtualCurrency      = storeAssets.getVirtualCurrency();
        mVirtualCurrencyPacks = Arrays.asList(storeAssets.getVirtualCurrencyPacks());
        mVirtualGoods         = Arrays.asList(storeAssets.getVirtualGoods());
        mStoreBackground      = storeAssets.getStoreBackground();
        mTemplate             = storeAssets.getStoreTemplate();
    }

    /**
     * Use this function if you need to know the definition of a specific virtual currency pack.
     * @param productId is the requested pack's product id.
     * @return the definition of the virtual pack requested.
     * @throws VirtualItemNotFoundException
     */
    public VirtualCurrencyPack getPackByGoogleProductId(String productId) throws VirtualItemNotFoundException {
        VirtualCurrencyPack pack = null;
        for(VirtualCurrencyPack p : mVirtualCurrencyPacks){
            if (p.getmGoogleItem().getMarketId().equals(productId)){
                pack = p;
                break;
            }
        }

        if (pack == null){
            throw new VirtualItemNotFoundException("productId", productId);
        }

        return pack;
    }

    /**
     * Use this function if you need to know the definition of a specific virtual good.
     * @param itemId is the requested good's item id.
     * @return the definition of the virtual good requested.
     * @throws VirtualItemNotFoundException
     */
    public VirtualGood getVirtualGoodByItemId(String itemId) throws VirtualItemNotFoundException {
        VirtualGood good = null;
        for(VirtualGood g : mVirtualGoods){
            if (g.getItemId().equals(itemId)){
                good = g;
                break;
            }
        }

        if (good == null){
            throw new VirtualItemNotFoundException("itemId", itemId);
        }

        return good;
    }

    /** Getters **/

    public VirtualCurrency getVirtualCurrency(){
        return mVirtualCurrency;
    }

    public List<VirtualCurrencyPack> getCurrencyPacks() {
        return mVirtualCurrencyPacks;
    }

    public List<VirtualGood> getVirtualGoods() {
        return mVirtualGoods;
    }

    public StoreTemplate getTemplate() {
        return mTemplate;
    }

    public String getStoreBackground() {
        return mStoreBackground;
    }

    public boolean isIsCurrencyStoreDisabled() {
        return mIsCurrencyStoreDisabled;
    }

    /**
     * Use this function to get a json representation of StoreInfo.
     * @return a json representation of StoreInfo.
     */
    public String getJsonString(){
        return toJSONObject().toString();
    }


    /** Private functions **/

    private StoreInfo() { }

    private JSONObject toJSONObject(){
        JSONObject currency = mVirtualCurrency.toJSONObject();

        JSONArray currencyPacks = new JSONArray();
        for(VirtualCurrencyPack pack : mVirtualCurrencyPacks){
            currencyPacks.put(pack.toJSONObject());
        }

        JSONArray virtualGoods = new JSONArray();
        for(VirtualGood good : mVirtualGoods){
            virtualGoods.put(good.toJSONObject());
        }

        JSONObject template = mTemplate.toJSONObject();


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("currency", currency);
            jsonObject.put("currencyPacks", currencyPacks);
            jsonObject.put("virtualGoods", virtualGoods);
            jsonObject.put("template", template);
            jsonObject.put("background", mStoreBackground);
            jsonObject.put("isCurrencyStoreDisabled", mIsCurrencyStoreDisabled);
        } catch (JSONException e) {
            if (StoreConfig.debug){
                Log.d(TAG, "An error occured while generating JSON object.");
            }
        }

        return jsonObject;
    }

    /** Private members **/

    private static final String TAG = "SOOMLA StoreInfo";
    private static StoreInfo                        sInstance = null;

    private VirtualCurrency                         mVirtualCurrency;
    private List<VirtualCurrencyPack>               mVirtualCurrencyPacks;
    private List<VirtualGood>                       mVirtualGoods;
    private StoreTemplate                           mTemplate;
    private String                                  mStoreBackground;
    private boolean                                 mIsCurrencyStoreDisabled;
}