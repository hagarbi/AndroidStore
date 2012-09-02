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
 * This is a representation of a store-front template.
 */
public class StoreTemplate {

    /** Constructor
     *
     * @param mName is the template name.
     * @param mElements is the various elements in the templates.
     * @param mProperties is the template's properties.
     * @param orientationLandscape determines the screen orientation.
     */
    public StoreTemplate(String mName,
                         StoreTemplateElements mElements,
                         StoreTemplateProperties mProperties,
                         boolean orientationLandscape) {
        this.mName = mName;
        this.mElements = mElements;
        this.mProperties = mProperties;
        this.mOrientationLandscape = orientationLandscape;
    }

    /** Constructor
     *
     * Generates an instance of {@link StoreTemplate} from a JSONObject.
     * @param jsonObject is a JSONObject representation of the wanted {@link StoreTemplate}.
     * @throws JSONException
     */
    public StoreTemplate(JSONObject jsonObject) throws JSONException {
        mName = jsonObject.getString("name");
        mElements = new StoreTemplateElements(jsonObject.getJSONObject("elements"));
        mProperties = new StoreTemplateProperties(jsonObject.getJSONObject("properties"));
        mOrientationLandscape = jsonObject.getBoolean("orientationLandscape");
    }

    /**
     * Converts the current {@link StoreTemplate} to a JSONObject.
     * @return a JSONObject representation of the current {@link StoreTemplate}.
     */
    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", mName);
            jsonObject.put("elements", mElements.toJSONObject());
            jsonObject.put("properties", mProperties.toJSONObject());
            jsonObject.put("orientationLandscape", mOrientationLandscape);
        } catch (JSONException e) {
            if (StoreConfig.debug){
                Log.d(TAG, "An error occured while generating JSON object.");
            }
        }

        return jsonObject;
    }

    public String getName() {
        return mName;
    }

    public StoreTemplateElements getElements() {
        return mElements;
    }

    public StoreTemplateProperties getProperties() {
        return mProperties;
    }

    public boolean isOrientationLandscape() {
        return mOrientationLandscape;
    }


    private static final String TAG = "SOOMLA StoreTemplate";

    private String                  mName;
    private StoreTemplateElements   mElements;
    private StoreTemplateProperties mProperties;
    private boolean                 mOrientationLandscape;
}
