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

/**
 * This abstract class represents a price-model used in every {@link VirtualGood}.
 */
public abstract class AbstractPriceModel {

    /**
     * Fetch the price of the given {@link VirtualGood} (Usually, the given virtual good will be the same as the
     * virtual good that holds this price-model).
     *
     * The price of a {@link VirtualGood} is a hash that contains multiple currencies' itemIds and their needed
     * values. If the user doesn't own at least one of the needed currency balances,
     * he can't purchase the required {@link VirtualGood}.
     * @param good is the virtual good to fetch the price for.
     * @return the price of the given virtual good.
     */
    public abstract HashMap<String, Integer> getCurrentPrice(VirtualGood good);

    /**
     * Converts the current {@link AbstractPriceModel} to a JSONObject.
     * @return a JSONObject representation of the current {@link AbstractPriceModel}.
     */
    public JSONObject toJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSONConsts.GOOD_PRICE_MODEL_TYPE, mType);

        return jsonObject;
    }

    /**
     * Creates the appropriate {@link AbstractPriceModel} with the given JSONObject.
     * The appropriate {@link AbstractPriceModel} is determined by the "type" element inside the JSONObject.
     * @param jsonObject is a JSONObject representation of the required {@link AbstractPriceModel}.
     * @return an implementation of {@link AbstractPriceModel}.
     * @throws JSONException
     */
    public static AbstractPriceModel fromJSONObject(JSONObject jsonObject) throws JSONException {
        String type = jsonObject.getString(JSONConsts.GOOD_PRICE_MODEL_TYPE);
        if (type.equals("static")){
            return StaticPriceModel.fromJSONObject(jsonObject);
        } else if (type.equals("balance")){
            return BalanceDrivenPriceModel.fromJSONObject(jsonObject);
        }

        return null;
    }

    /**
     * Generates a JSONObject out of a given price model.
     * @param priceModel is the required price model.
     * @return a JSONObject representation of the given price model.
     * @throws JSONException
     */
    public static JSONObject priceModelToJSONObject(AbstractPriceModel priceModel) throws JSONException {
        return priceModel.toJSONObject();
    }


    protected AbstractPriceModel(){}

    protected String mType = "abstract";
}
