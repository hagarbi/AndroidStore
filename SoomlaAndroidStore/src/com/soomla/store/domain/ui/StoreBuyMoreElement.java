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
 * This is the "Buy more ..." element in the UI.
 */
public class StoreBuyMoreElement{

    /** Constructor
     *
     * @param mText is the text that will be written inside the element. For example: "Buy More Clams"/
     * @param imagePath is a path to the element's background image.
     */
    public StoreBuyMoreElement(String mText, String imagePath) {
        this.mText = mText;
        this.mImgFilePath = imagePath;
    }

    /** Constructor
     *
     * Generates an instance of {@link StoreBuyMoreElement} from a JSONObject.
     * @param jsonObject is a JSONObject representation of the wanted {@link StoreBuyMoreElement}.
     * @throws JSONException
     */
    public StoreBuyMoreElement(JSONObject jsonObject) throws JSONException {
        this.mText = jsonObject.getString("text");
        this.mImgFilePath = jsonObject.getString("imgFilePath");
    }

    /**
     * Converts the current {@link StoreBuyMoreElement} to a JSONObject.
     * @return a JSONObject representation of the current {@link StoreBuyMoreElement}.
     */
    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("text", mText);
            jsonObject.put("imgFilePath", mImgFilePath);
        } catch (JSONException e) {
            if (StoreConfig.debug){
                Log.d(TAG, "An error occurred while generating JSON object.");
            }
        }

        return jsonObject;
    }

    public String getText() {
        return mText;
    }

    public String getImgFilePath() {
        return mImgFilePath;
    }


    /** Private Members **/

    private static final String TAG = "SOOMLA StoreBuyMoreElement";

    private String mText;
    private String mImgFilePath;
}