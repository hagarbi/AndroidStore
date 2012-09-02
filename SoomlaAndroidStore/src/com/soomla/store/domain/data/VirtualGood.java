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

    /** Constructor
     *
     * Generates an instance of {@link VirtualGood} from a JSONObject.
     * @param jsonObject is a JSONObject representation of the wanted {@link VirtualGood}.
     * @throws JSONException
     */
    public VirtualGood(JSONObject jsonObject) throws JSONException{
        super(jsonObject);
        this.mPriceModel = AbstractPriceModel.fromJSONObject(jsonObject.getJSONObject(JSONConsts
                .GOOD_PRICE_MODEL));
    }

    /**
     * Converts the current {@link VirtualGood} to a JSONObject.
     * @return a JSONObject representation of the current {@link VirtualGood}.
     */
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

    /**
     * The currency value is calculated in the price model so we return the current price of the
     * virtual good as defined in its price model.
     * @return the current price of the virtual good according to its price model.
     */
    public HashMap<String, Integer> getCurrencyValues(){
        return mPriceModel.getCurrentPrice(this);
    }

    /**
     * The same as the above {@link com.soomla.store.domain.data.VirtualGood#getCurrencyValues()}
     * only here the returned value is a representation of the HashMap as a JSONObject.
     * @return the current price of the virtual good according to its price model as a JSONObject.
     */
    public JSONObject getCurrencyValuesAsJSONObject(){

        HashMap<String, Integer> currencyValue = mPriceModel.getCurrentPrice(this);
        JSONObject jsonObject = new JSONObject();
        for(String key : currencyValue.keySet()){
            try {
                jsonObject.put(key, currencyValue.get(key));
            } catch (JSONException e) {
                if (StoreConfig.debug){
                    Log.d(TAG, "An error occurred while generating JSON object.");
                }
            }
        }

        return jsonObject;
    }

    /** Private members **/

    private static final String TAG = "SOOMLA VirtualGood";

    private AbstractPriceModel mPriceModel;
}
