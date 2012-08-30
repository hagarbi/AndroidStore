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
import org.json.JSONException;
import org.json.JSONObject;

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
     * @param mCurrencyValue is the number of currencies needed to buy the current virtual good.
     */
    public VirtualGood(String mName, String mDescription, String mImgFilePath, int mCurrencyValue, String mItemId) {
        super(mName, mDescription, mImgFilePath, mItemId);
        this.mCurrencyValue = mCurrencyValue;
    }

    public VirtualGood(JSONObject jsonObject) {
        super(jsonObject);
        try {
            this.mCurrencyValue = jsonObject.getInt("price");
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
            jsonObject.put("price", new Integer(mCurrencyValue));

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

    public int getmCurrencyValue(){
        return mCurrencyValue;
    }

    /** Private members **/

    private static final String TAG = "SOOMLA VirtualGood";

    private int mCurrencyValue;
}
