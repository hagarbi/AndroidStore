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

public class StaticPriceModel extends AbstractPriceModel {

    private StaticPriceModel(){
        super();

        mType = "static";
    }

    public StaticPriceModel(HashMap<String, Integer> mCurrencyValue){
        this.mCurrencyValue = mCurrencyValue;
        mType = "static";
    }

    @Override
    public HashMap<String, Integer> getCurrentPrice(VirtualGood good) {
        return mCurrencyValue;
    }

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

    private HashMap<String, Integer> mCurrencyValue;
}
