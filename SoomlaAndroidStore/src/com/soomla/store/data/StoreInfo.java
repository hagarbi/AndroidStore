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

import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;
import com.soomla.billing.util.AESObfuscator;
import com.soomla.store.IStoreAssets;
import com.soomla.store.StoreConfig;
import com.soomla.store.domain.data.VirtualCurrency;
import com.soomla.store.domain.data.VirtualCurrencyPack;
import com.soomla.store.domain.data.VirtualGood;
import com.soomla.store.exceptions.VirtualItemNotFoundException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * This class holds the store's meta data including:
 * - Virtual Currencies definitions
 * - Virtual Currency Packs definitions
 * - Virtual Goods definitions
 */
public class StoreInfo {

    public static StoreInfo getInstance(){
        if (sInstance == null){
            sInstance = new StoreInfo();
        }

        return sInstance;
    }

    /**
     * This function initializes StoreInfo. On first initialization, when the
     * database doesn't have any previous version of the store metadata, StoreInfo
     * is being loaded from the given {@link IStoreAssets}. After the first initialization,
     * StoreInfo will be initialized from the database.
     * NOTE: If you want to override the current StoreInfo, you'll have to bump the
     * database version (the old database will be destroyed).
     */
    public void initialize(IStoreAssets storeAssets){
        if (storeAssets == null){
            Log.e(TAG, "The given store assets can't be null !");
            return;
        }

        if (!initializeFromDB()){
            /// fall-back here if the json parsing fails or doesn't exist, we load the store from the given
            // {@link IStoreAssets}.
            mVirtualCurrencies    = Arrays.asList(storeAssets.getVirtualCurrencies());
            mVirtualCurrencyPacks = Arrays.asList(storeAssets.getVirtualCurrencyPacks());
            mVirtualGoods         = Arrays.asList(storeAssets.getVirtualGoods());

            // put StoreInfo in the database as JSON
            String store_json = toJSONObject().toString();
            if (StorageManager.getObfuscator() != null){
                store_json = StorageManager.getObfuscator().obfuscateString(store_json);
            }
            StorageManager.getDatabase().setStoreInfo(store_json);
        }
    }

    public boolean initializeFromDB() {
        // first, trying to load StoreInfo from the local DB.
        Cursor cursor = StorageManager.getDatabase().getMetaData();
        if (cursor != null) {
            String storejson = "";
            try {
                int storeVal = cursor.getColumnIndexOrThrow(
                        StoreDatabase.METADATA_COLUMN_STOREINFO);
                if (cursor.moveToNext()) {
                    storejson = cursor.getString(storeVal);
                    if (StorageManager.getObfuscator() != null){
                        storejson = StorageManager.getObfuscator().unobfuscateToString(storejson);
                    }

                    if (StoreConfig.debug){
                        Log.d(TAG, "the metadata json (from DB) is " + storejson);
                    }
                }
            } catch (AESObfuscator.ValidationException e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }

            if (!TextUtils.isEmpty(storejson)){
                try {
                    fromJSONObject(new JSONObject(storejson));

                    // everything went well... StoreInfo is initialized from the local DB.
                    // it's ok to return now.
                    return true;
                } catch (JSONException e) {
                    if (StoreConfig.debug){
                        Log.d(TAG, "Can't parse metadata json: " + storejson);
                    }
                }
            }
        }
        return false;
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
     * Use this function if you need to know the definition of a specific virtual currency pack.
     * @param itemId is the requested pack's item id.
     * @return the definition of the virtual pack requested.
     * @throws VirtualItemNotFoundException
     */
    public VirtualCurrencyPack getPackByItemId(String itemId) throws VirtualItemNotFoundException {
        VirtualCurrencyPack pack = null;
        for(VirtualCurrencyPack p : mVirtualCurrencyPacks){
            if (p.getItemId().equals(itemId)){
                pack = p;
                break;
            }
        }

        if (pack == null){
            throw new VirtualItemNotFoundException("itemId", itemId);
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

    /**
     * Use this function if you need to know the definition of a specific virtual currency.
     * @param itemId is the requested currency's item id.
     * @return the definition of the virtual currency requested.
     * @throws VirtualItemNotFoundException
     */
    public VirtualCurrency getVirtualCurrencyByItemId(String itemId) throws VirtualItemNotFoundException {
        VirtualCurrency currency = null;
        for(VirtualCurrency c : mVirtualCurrencies){
            if (c.getItemId().equals(itemId)){
                currency = c;
                break;
            }
        }

        if (currency == null){
            throw new VirtualItemNotFoundException("itemId", itemId);
        }

        return currency;
    }

    /** Getters **/

    public List<VirtualCurrency> getVirtualCurrencies(){
        return mVirtualCurrencies;
    }

    public List<VirtualCurrencyPack> getCurrencyPacks() {
        return mVirtualCurrencyPacks;
    }

    public List<VirtualGood> getVirtualGoods() {
        return mVirtualGoods;
    }

    /** Private functions **/

    private StoreInfo() { }

    private void fromJSONObject(JSONObject jsonObject){
        try {
            JSONArray virtualCurrencies = jsonObject.getJSONArray(JSONConsts.STORE_VIRTUALCURRENCIES);
            mVirtualCurrencies = new LinkedList<VirtualCurrency>();
            for (int i=0; i<virtualCurrencies.length(); i++){
                JSONObject o = virtualCurrencies.getJSONObject(i);
                mVirtualCurrencies.add(new VirtualCurrency(o));
            }

            JSONArray currencyPacks = jsonObject.getJSONArray(JSONConsts.STORE_CURRENCYPACKS);
            mVirtualCurrencyPacks = new LinkedList<VirtualCurrencyPack>();
            for (int i=0; i<currencyPacks.length(); i++){
                JSONObject o = currencyPacks.getJSONObject(i);
                mVirtualCurrencyPacks.add(new VirtualCurrencyPack(o));
            }

            JSONArray virtualGoods = jsonObject.getJSONArray(JSONConsts.STORE_VIRTUALGOODS);
            mVirtualGoods = new LinkedList<VirtualGood>();
            for (int i=0; i<virtualGoods.length(); i++){
                JSONObject o = virtualGoods.getJSONObject(i);
                mVirtualGoods.add(new VirtualGood(o));
            }
        } catch (JSONException e) {
            if (StoreConfig.debug){
                Log.d(TAG, "An error occurred while parsing JSON object.");
            }
        }
    }

    /**
     * Converts StoreInfo to a JSONObject.
     * @return a JSONObject representation of the StoreInfo.
     */
    public JSONObject toJSONObject(){

        JSONArray virtualCurrencies = new JSONArray();
        for(VirtualCurrency c : mVirtualCurrencies){
            virtualCurrencies.put(c.toJSONObject());
        }

        JSONArray currencyPacks = new JSONArray();
        for(VirtualCurrencyPack pack : mVirtualCurrencyPacks){
            currencyPacks.put(pack.toJSONObject());
        }

        JSONArray virtualGoods = new JSONArray();
        for(VirtualGood good : mVirtualGoods){
            virtualGoods.put(good.toJSONObject());
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(JSONConsts.STORE_VIRTUALCURRENCIES, virtualCurrencies);
            jsonObject.put(JSONConsts.STORE_VIRTUALGOODS, virtualGoods);
            jsonObject.put(JSONConsts.STORE_CURRENCYPACKS, currencyPacks);
        } catch (JSONException e) {
            if (StoreConfig.debug){
                Log.d(TAG, "An error occurred while generating JSON object.");
            }
        }

        return jsonObject;
    }

    /** Private members **/

    private static final String TAG = "SOOMLA StoreInfo";
    private static StoreInfo                        sInstance = null;

    private List<VirtualCurrency>                   mVirtualCurrencies;
    private List<VirtualCurrencyPack>               mVirtualCurrencyPacks;
    private List<VirtualGood>                       mVirtualGoods;
}