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
package com.soomla.store.domain.ui;

import android.util.Log;
import com.soomla.store.StoreConfig;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class represents the various elements in the template.
 */
public class StoreTemplateElements {

    /** Constructor
     *
     * @param mTitleElement is the element representing the title in the template.
     * @param mBuyMoreElement is the element representing the "buy more ..." in the template.
     */
    public StoreTemplateElements(StoreTitleElement mTitleElement, StoreBuyMoreElement mBuyMoreElement) {
        this.mTitleElement = mTitleElement;
        this.mBuyMoreElement = mBuyMoreElement;
    }

    /** Constructor
     *
     * Generates an instance of {@link StoreTemplateElements} from a JSONObject.
     * @param jsonObject is a JSONObject representation of the wanted {@link StoreTemplateElements}.
     * @throws JSONException
     */
    public StoreTemplateElements(JSONObject jsonObject) throws JSONException {
        this.mTitleElement = new StoreTitleElement(jsonObject.getJSONObject("title"));
        this.mBuyMoreElement = new StoreBuyMoreElement(jsonObject.getJSONObject("buyMore"));
    }

    /**
     * Converts the current {@link StoreTemplateElements} to a JSONObject.
     * @return a JSONObject representation of the current {@link StoreTemplateElements}.
     */
    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("title", mTitleElement.toJSONObject());
            jsonObject.put("buyMore", mBuyMoreElement.toJSONObject());
        } catch (JSONException e) {
            if (StoreConfig.debug){
                Log.d(TAG, "An error occured while generating JSON object.");
            }
        }

        return jsonObject;
    }

    public StoreTitleElement getTitleElement() {
        return mTitleElement;
    }

    public StoreBuyMoreElement getBuyMoreElement() {
        return mBuyMoreElement;
    }


    private static final String TAG = "SOOMLA StoreTemplateElements";

    private StoreTitleElement mTitleElement;
    private StoreBuyMoreElement mBuyMoreElement;
}
