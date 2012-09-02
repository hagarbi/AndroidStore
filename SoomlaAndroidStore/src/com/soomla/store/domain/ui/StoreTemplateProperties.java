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
 * This class represents the template's properties.
 */
public class StoreTemplateProperties {

    public StoreTemplateProperties(int mColumns) {
        this.mColumns = mColumns;
    }

    /** Constructor
     *
     * Generates an instance of {@link StoreTemplateProperties} from a JSONObject.
     * @param jsonObject is a JSONObject representation of the wanted {@link StoreTemplateProperties}.
     * @throws JSONException
     */
    public StoreTemplateProperties(JSONObject jsonObject) throws JSONException {
        this.mColumns = jsonObject.getInt("columns");
    }

    /**
     * Converts the current {@link StoreTemplateProperties} to a JSONObject.
     * @return a JSONObject representation of the current {@link StoreTemplateProperties}.
     */
    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("columns", new Integer(mColumns));
        } catch (JSONException e) {
            if (StoreConfig.debug){
                Log.d(TAG, "An error occured while generating JSON object.");
            }
        }

        return jsonObject;
    }

    public int getColumns() {
        return mColumns;
    }


    /** Private Members **/

    private static final String TAG = "SOOMLA StoreTemplateProperties";

    private int mColumns;
}