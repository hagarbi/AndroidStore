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

public abstract class AbstractPriceModel {

    protected AbstractPriceModel(){}

    public abstract HashMap<String, Integer> getCurrentPrice(VirtualGood good);
    public JSONObject toJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSONConsts.GOOD_PRICE_MODEL_TYPE, getType());

        return jsonObject;
    }

    public String getType() {
        return mType;
    }

    public static AbstractPriceModel fromJSONObject(JSONObject jsonObject) throws JSONException {
        String type = jsonObject.getString(JSONConsts.GOOD_PRICE_MODEL_TYPE);
        if (type.equals("static")){
            return StaticPriceModel.fromJSONObject(jsonObject);
        } else if (type.equals("balance")){
            return BalanceDrivenPriceModel.fromJSONObject(jsonObject);
        }

        return null;
    }

    public static JSONObject priceModelToJSONObject(AbstractPriceModel priceModel) throws JSONException {
        return priceModel.toJSONObject();
    }


    protected String mType = "abstract";
}
