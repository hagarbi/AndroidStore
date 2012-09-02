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

import com.soomla.store.data.JSONConsts;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * This price model is a model that gives the associated virtual good a static price that's not affected by anything.
 */
public class StaticPriceModel extends AbstractPriceModel {

    private StaticPriceModel(){
        super();

        mType = "static";
    }

    /** Constructor
     * Creates an instance of {@link StaticPriceModel} with the given price.
     * @param mCurrencyValue is a given price as a HashMap.
     */
    public StaticPriceModel(HashMap<String, Integer> mCurrencyValue){
        this.mCurrencyValue = mCurrencyValue;
        mType = "static";
    }

    /**
     * docs in {@link AbstractPriceModel#getCurrentPrice(VirtualGood)}.
     */
    @Override
    public HashMap<String, Integer> getCurrentPrice(VirtualGood good) {
        return mCurrencyValue;
    }

    /**
     * docs in {@link AbstractPriceModel#toJSONObject()}
     */
    @Override
    public JSONObject toJSONObject() throws JSONException {
        JSONObject parentJsonObject = super.toJSONObject();
        JSONObject jsonObject = new JSONObject();

        Iterator<?> keys = parentJsonObject.keys();
        while(keys.hasNext())
        {
            String key = (String)keys.next();
            jsonObject.put(key, parentJsonObject.get(key));
        }

        JSONObject currencyValues = new JSONObject();
        for(String key : mCurrencyValue.keySet()){
            currencyValues.put(key, mCurrencyValue.get(key));
        }
        jsonObject.put(JSONConsts.GOOD_PRICE_MODEL_VALUES, currencyValues);

        return jsonObject;
    }

    /**
     * Creates a {@link StaticPriceModel} with the given JSONObject.
     * @param jsonObject is a JSONObject representation of the required {@link StaticPriceModel}.
     * @return an instance of {@link StaticPriceModel}.
     * @throws JSONException
     */
    public static StaticPriceModel fromJSONObject(JSONObject jsonObject) throws JSONException {
        JSONObject valuesJSONObject = jsonObject.getJSONObject(JSONConsts.GOOD_PRICE_MODEL_VALUES);
        Iterator<?> keys = valuesJSONObject.keys();
        HashMap<String, Integer> values = new HashMap<String, Integer>();
        while(keys.hasNext()){
            String key = (String)keys.next();
            values.put(key, valuesJSONObject.getInt(key));
        }

        return new StaticPriceModel(values);
    }


    /** Private Members **/

    private HashMap<String, Integer> mCurrencyValue;
}
