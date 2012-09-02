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
import com.soomla.store.data.StorageManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class BalanceDrivenPriceModel extends AbstractPriceModel{

    private BalanceDrivenPriceModel() {
        super();
    }

    public BalanceDrivenPriceModel(ArrayList<HashMap<String, Integer>> mCurrencyValuePerBalance){
        this.mCurrencyValuePerBalance = mCurrencyValuePerBalance;
        mType = "balance";
    }

    @Override
    public HashMap<String, Integer> getCurrentPrice(VirtualGood good) {
        int balance = StorageManager.getInstance().getVirtualGoodsStorage().getBalance(good);

        // if the balance is bigger than the size of the array, return the last value in the array.
        if (balance >= mCurrencyValuePerBalance.size()){
            return mCurrencyValuePerBalance.get(mCurrencyValuePerBalance.size()-1);
        }

        return mCurrencyValuePerBalance.get(balance);
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

        JSONArray valuesPerBalance = new JSONArray();
        for (HashMap<String, Integer> currencyValue : mCurrencyValuePerBalance) {
            JSONObject currencyValues = new JSONObject();
            for (String key : currencyValue.keySet()) {
                currencyValues.put(key, currencyValue.get(key));
            }
            valuesPerBalance.put(currencyValues);
        }
        jsonObject.put(JSONConsts.GOOD_PRICE_MODEL_VALUES, valuesPerBalance);

        return jsonObject;
    }

    public static BalanceDrivenPriceModel fromJSONObject(JSONObject jsonObject) throws JSONException {
        JSONArray valuesPerBalanceJSON = jsonObject.getJSONArray(JSONConsts.GOOD_PRICE_MODEL_VALUES);
        ArrayList <HashMap<String, Integer>> valuesPerBalance = new ArrayList<HashMap<String,
                Integer>>(valuesPerBalanceJSON.length());
        for (int i=0; i<valuesPerBalanceJSON.length(); i++){
            JSONObject valuesJSON = valuesPerBalanceJSON.getJSONObject(i);
            Iterator<?> valuesKeys = valuesJSON.keys();
            HashMap<String, Integer> values = new HashMap<String, Integer>();
            while(valuesKeys.hasNext()){
                String key = (String)valuesKeys.next();
                values.put(key, valuesJSON.getInt(key));
            }

            valuesPerBalance.add(i, values);
        }

        return new BalanceDrivenPriceModel(valuesPerBalance);
    }

    private ArrayList<HashMap<String, Integer>> mCurrencyValuePerBalance;
}
