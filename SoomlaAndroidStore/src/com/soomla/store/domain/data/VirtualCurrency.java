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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This is a representation of the game's virtual currency.
 * Each game can have multiple instances of a virtual currency, all kept in {@link com.soomla.store.data.StoreInfo};
 */
public class VirtualCurrency extends VirtualItem {

    /** Constructor
     *
     * @param mName is the name of the virtual currency. For example: "Coin", "Clam", "Gem" ...
     * @param mDescription is the description of the virtual currency. This will show up
     *                       in the store in the description section.
     * @param mImgFilePath is the path to the image that corresponds to the currency.
     * @param itemId is the id of the virtual currency.
     */
    public VirtualCurrency(String mName, String mDescription, String mImgFilePath, String itemId) {
        super(mName, mDescription, mImgFilePath, itemId);
    }

    /** Constructor
     *
     * Generates an instance of {@link VirtualCurrency} from a JSONObject.
     * @param jsonObject is a JSONObject representation of the wanted {@link VirtualCurrency}.
     * @throws org.json.JSONException
     */
    public VirtualCurrency(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
    }

    /**
     * Converts the current {@link VirtualCurrency} to a JSONObject.
     * @return a JSONObject representation of the current {@link VirtualCurrency}.
     */
    public JSONObject toJSONObject(){
        return super.toJSONObject();
    }
}
