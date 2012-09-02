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
 * This is the title element in the UI.
 */
public class StoreTitleElement{

    /** Constructor
     *
     * @param mName is the text displayed inside the title element.
     */
    public StoreTitleElement(String mName) {
        this.mName = mName;
    }

    /** Constructor
     *
     * Generates an instance of {@link StoreTitleElement} from a JSONObject.
     * @param jsonObject is a JSONObject representation of the wanted {@link StoreTitleElement}.
     * @throws JSONException
     */
    public StoreTitleElement(JSONObject jsonObject) throws JSONException {
        this.mName = jsonObject.getString("name");
    }

    /**
     * Converts the current {@link StoreTitleElement} to a JSONObject.
     * @return a JSONObject representation of the current {@link StoreTitleElement}.
     */
    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", mName);
        } catch (JSONException e) {
            if (StoreConfig.debug){
                Log.d(TAG, "An error occurred while generating JSON object.");
            }
        }

        return jsonObject;
    }

    public String getName() {
        return mName;
    }


    /** Private Members **/

    private static final String TAG = "SOOMLA StoreTitleElement";

    private String mName;
}