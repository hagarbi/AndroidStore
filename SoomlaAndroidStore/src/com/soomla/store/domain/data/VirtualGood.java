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
package com.soomla.store.domain.data;

import android.util.Log;
import com.soomla.store.StoreConfig;
import com.soomla.store.data.JSONConsts;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * This is a representation of the application's virtual good.
 * Virtual goods are bought with {@link VirtualCurrency} hence the {@link VirtualGood#mCurrencyValue}
 */
public class VirtualGood extends VirtualItem {

    /** Constructor
     *
     * @param mName is the name of the virtual good.
     * @param mDescription is the description of the virtual good. This will show up
     *                       in the store in the description section.
     * @param mImgFilePath is the path to the image that corresponds to the virtual good.
     * @param mItemId is the id of the virtual good.
     * @param mCurrencyValue is a hash with the a currency's itemId and the amount needed from it to purchase this
     *                       virtual good.
     */
    public VirtualGood(String mName, String mDescription, String mImgFilePath, HashMap<String, Integer> mCurrencyValue,
            String mItemId) {
        super(mName, mDescription, mImgFilePath, mItemId);
        this.mCurrencyValue = mCurrencyValue;
    }

    public VirtualGood(JSONObject jsonObject) {
        super(jsonObject);
        try {
            JSONObject currencyValues = jsonObject.getJSONObject(JSONConsts.GOOD_CURRENCY_VALUE);
            Iterator<?> keys = currencyValues.keys();
            this.mCurrencyValue = new HashMap<String, Integer>();
            while(keys.hasNext())
            {
                String key = (String)keys.next();
                this.mCurrencyValue.put(key, currencyValues.getInt(key));
            }
        } catch (JSONException e) {
            if (StoreConfig.debug){
                Log.d(TAG, "An error occured while parsing JSON object.");
            }
        }
    }

    public JSONObject toJSONObject(){
        JSONObject parentJsonObject = super.toJSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            JSONObject currencyValues = new JSONObject();
            for(String key : mCurrencyValue.keySet()){
                currencyValues.put(key, mCurrencyValue.get(key));
            }
            jsonObject.put(JSONConsts.GOOD_CURRENCY_VALUE, currencyValues);

            Iterator<?> keys = parentJsonObject.keys();
            while(keys.hasNext())
            {
                String key = (String)keys.next();
                jsonObject.put(key, parentJsonObject.get(key));
            }
        } catch (JSONException e) {
            if (StoreConfig.debug){
                Log.d(TAG, "An error occured while generating JSON object.");
            }
        }

        return jsonObject;
    }

    /** Getters **/

    /**
     * Get the amount of currency need to purchase this virtual good by the currency itemId.
     * @param currencyItemId is the itemId of the currency in question.
     * @return the amount of currency needed to purchase this virtual good.
     */
    public int getCurrencyValue(String currencyItemId){
        if (!mCurrencyValue.containsKey(currencyItemId)){
            return 0;
        }
        return mCurrencyValue.get(currencyItemId);
    }

    /**
     * Get the entire hash that represents the price of this virtual good.
     * @return the entire hash that represents the price of this virtual good.
     */
    public HashMap<String, Integer> getCurrencyValues(){
        return mCurrencyValue;
    }

    /** Private members **/

    private static final String TAG = "SOOMLA VirtualGood";

    private HashMap<String, Integer> mCurrencyValue;
}
