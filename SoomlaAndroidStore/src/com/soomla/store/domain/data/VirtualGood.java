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
 * Virtual goods are bought with one or more {@link VirtualCurrency}. The price
 * is determined by the {@link VirtualGood#mPriceModel}
 */
public class VirtualGood extends VirtualItem {

    /** Constructor
     *
     * @param mName is the name of the virtual good.
     * @param mDescription is the description of the virtual good. This will show up
     *                       in the store in the description section.
     * @param mImgFilePath is the path to the image that corresponds to the virtual good.
     * @param mItemId is the id of the virtual good.
     * @param mPriceModel is the way the price of the current virtual good is calculated.
     */
    public VirtualGood(String mName, String mDescription, String mImgFilePath, AbstractPriceModel mPriceModel,
            String mItemId) {
        super(mName, mDescription, mImgFilePath, mItemId);
        this.mPriceModel = mPriceModel;
    }

    public VirtualGood(JSONObject jsonObject) {
        super(jsonObject);
        try {
            this.mPriceModel = AbstractPriceModel.fromJSONObject(jsonObject.getJSONObject(JSONConsts
                    .GOOD_PRICE_MODEL));
        } catch (JSONException e) {
            if (StoreConfig.debug){
                Log.d(TAG, "An error occurred while parsing JSON object.");
            }
        }
    }

    public JSONObject toJSONObject(){
        JSONObject parentJsonObject = super.toJSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            Iterator<?> keys = parentJsonObject.keys();
            while(keys.hasNext())
            {
                String key = (String)keys.next();
                jsonObject.put(key, parentJsonObject.get(key));
            }

            JSONObject priceModelObject = AbstractPriceModel.priceModelToJSONObject(mPriceModel);
            jsonObject.put(JSONConsts.GOOD_PRICE_MODEL, priceModelObject);
        } catch (JSONException e) {
            if (StoreConfig.debug){
                Log.d(TAG, "An error occurred while generating JSON object.");
            }
        }

        return jsonObject;
    }

    public HashMap<String, Integer> getCurrencyValues(){
        return mPriceModel.getCurrentPrice(this);
    }

    /** Private members **/

    private static final String TAG = "SOOMLA VirtualGood";

    private AbstractPriceModel mPriceModel;
}
