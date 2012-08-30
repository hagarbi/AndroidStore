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

        // first, trying to load StoreInfo from the local DB.
        Cursor cursor = StorageManager.getDatabase().getMetaData();
        if (cursor != null) {
            String metadata_json = "";
            try {
                int metadateVal = cursor.getColumnIndexOrThrow(
                        StoreDatabase.METADATA_COLUMN_VALUE);
                if (cursor.moveToNext()) {
                    metadata_json = cursor.getString(metadateVal);
                    if (StorageManager.getObfuscator() != null){
                        metadata_json = StorageManager.getObfuscator().unobfuscateToString(metadata_json);
                    }

                    if (StoreConfig.debug){
                        Log.d(TAG, "the metadata json (from DB) is " + metadata_json);
                    }
                }
            } catch (AESObfuscator.ValidationException e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }

            if (!TextUtils.isEmpty(metadata_json)){
                try {
                    fromJSONObject(new JSONObject(metadata_json));

                    // everything went well... StoreInfo is initialized from the local DB.
                    // it's ok to return now.
                    return;
                } catch (JSONException e) {
                    if (StoreConfig.debug){
                        Log.d(TAG, "Can't parse metadata json: " + metadata_json);
                    }
                }
            }
        }


        // fall-back here if the json parsing fails or doesn't exist
        mVirtualCurrency      = storeAssets.getVirtualCurrency();
        mVirtualCurrencyPacks = Arrays.asList(storeAssets.getVirtualCurrencyPacks());
        mVirtualGoods         = Arrays.asList(storeAssets.getVirtualGoods());
        mStoreBackground      = storeAssets.getStoreBackground();
        mTemplate             = storeAssets.getStoreTemplate();

        // put StoreInfo in the database as JSON
        String obf_json = StorageManager.getObfuscator().obfuscateString(getJsonString());
        StorageManager.getDatabase().setMetaData(obf_json);
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
        String json = toJSONObject().toString();
        if (StoreConfig.debug){
            Log.d(TAG, "generated json: " + json);
        }
        return json;
    }


    /** Private functions **/

    private StoreInfo() { }

    private void fromJSONObject(JSONObject jsonObject){
        try {
            mVirtualCurrency = new VirtualCurrency(jsonObject.getJSONObject("currency"));
            mTemplate = new StoreTemplate(jsonObject.getJSONObject("template"));
            mStoreBackground = jsonObject.getString("background");
            mIsCurrencyStoreDisabled = jsonObject.getBoolean("isCurrencyStoreDisabled");

            JSONArray currencyPacks = jsonObject.getJSONArray("currencyPacks");
            mVirtualCurrencyPacks = new LinkedList<VirtualCurrencyPack>();
            for (int i=0; i<currencyPacks.length(); i++){
                JSONObject o = currencyPacks.getJSONObject(i);
                mVirtualCurrencyPacks.add(new VirtualCurrencyPack(o));
            }

            JSONArray virtualGoods = jsonObject.getJSONArray("virtualGoods");
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
                Log.d(TAG, "An error occurred while generating JSON object.");
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