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
package com.soomla.store.data;

import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;
import com.soomla.billing.util.AESObfuscator;
import com.soomla.store.StoreConfig;
import com.soomla.store.domain.ui.StoreTemplate;
import com.soomla.store.domain.ui.StoreTheme;
import com.soomla.store.storefront.IStorefrontAssets;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class holds the store's meta data including:
 * - Store UI template
 * - Store UI theme
 * - Store UI background
 */
public class StorefrontInfo {

    public static StorefrontInfo getInstance(){
        if (sInstance == null){
            sInstance = new StorefrontInfo();
        }

        return sInstance;
    }

    /**
     * This function initializes StorefrontInfo. On first initialization, when the
     * database doesn't have any previous version of the store metadata, StorefrontInfo
     * is being loaded from the given {@link com.soomla.store.storefront.IStorefrontAssets}. After the first initialization,
     * StorefrontInfo will be initialized from the database.
     * NOTE: If you want to override the current StorefrontInfo, you'll have to bump the
     * database version (the old database will be destroyed).
     */
    public void initialize(IStorefrontAssets storefrontAssets){
        if (storefrontAssets == null){
            Log.e(TAG, "The given storefront assets can't be null !");
            return;
        }

        if (!initializeFromDB()){
            /// fall-back here if the json parsing fails or doesn't exist, we load the storefront from the given
            // {@link IStorefrontAssets}.
            mStoreBackground      = storefrontAssets.getStoreBackground();
            mTemplate             = storefrontAssets.getStoreTemplate();
            mTheme                = storefrontAssets.getStoreTheme();

            // put StorefrontInfo in the database as JSON
            String storefront_json = toJSONObject().toString();
            if (StorageManager.getObfuscator() != null){
                storefront_json = StorageManager.getObfuscator().obfuscateString(storefront_json);
            }
            StorageManager.getDatabase().setStorefrontInfo(storefront_json);
        }
    }

    public boolean initializeFromDB() {
        // first, trying to load StorefrontInfo from the local DB.
        Cursor cursor = StorageManager.getDatabase().getMetaData();
        if (cursor != null) {
            String storefront_json = "";
            try {
                int storefrontVal = cursor.getColumnIndexOrThrow(
                        StoreDatabase.METADATA_COLUMN_STOREFRONTINFO);
                if (cursor.moveToNext()) {
                    storefront_json = cursor.getString(storefrontVal);
                    if (StorageManager.getObfuscator() != null){
                        storefront_json = StorageManager.getObfuscator().unobfuscateToString(storefront_json);
                    }

                    if (StoreConfig.debug){
                        Log.d(TAG, "the metadata json (from DB) is " + storefront_json);
                    }
                }
            } catch (AESObfuscator.ValidationException e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }

            if (!TextUtils.isEmpty(storefront_json)){
                try {
                    fromJSONObject(new JSONObject(storefront_json));

                    // everything went well... StorefrontInfo is initialized from the local DB.
                    // it's ok to return now.
                    return true;
                } catch (JSONException e) {
                    if (StoreConfig.debug){
                        Log.d(TAG, "Can't parse metadata json: " + storefront_json);
                    }
                }
            }
        }
        return false;
    }

    /** Getters **/

    public StoreTemplate getTemplate() {
        return mTemplate;
    }

    public String getStoreBackground() {
        return mStoreBackground;
    }

    public boolean isIsCurrencyStoreDisabled() {
        return mIsCurrencyStoreDisabled;
    }

    /** Private functions **/

    private StorefrontInfo() { }

    private void fromJSONObject(JSONObject jsonObject){
        try {
            mTemplate = new StoreTemplate(jsonObject.getJSONObject(JSONConsts.STORE_TEMPLATE));
            mTheme = new StoreTheme(jsonObject.getJSONObject(JSONConsts.STORE_THEME));
            mStoreBackground = jsonObject.getString(JSONConsts.STORE_BACKGROUND);
            mIsCurrencyStoreDisabled = jsonObject.getBoolean(JSONConsts.STORE_ISCURRENCYDISABLED);
        } catch (JSONException e) {
            if (StoreConfig.debug){
                Log.d(TAG, "An error occurred while parsing JSON object.");
            }
        }
    }

    /**
     * Converts StorefrontInfo to a JSONObject.
     * @return a JSONObject representation of the StorefrontInfo.
     */
    public JSONObject toJSONObject(){
        JSONObject template = mTemplate.toJSONObject();
        JSONObject theme = mTheme.toJSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(JSONConsts.STORE_TEMPLATE, template);
            jsonObject.put(JSONConsts.STORE_THEME, theme);
            jsonObject.put(JSONConsts.STORE_BACKGROUND, mStoreBackground);
            jsonObject.put(JSONConsts.STORE_ISCURRENCYDISABLED, mIsCurrencyStoreDisabled);
        } catch (JSONException e) {
            if (StoreConfig.debug){
                Log.d(TAG, "An error occurred while generating JSON object.");
            }
        }

        return jsonObject;
    }

    /** Private members **/

    private static final String TAG = "SOOMLA StorefrontInfo";
    private static StorefrontInfo sInstance = null;

    private StoreTemplate                           mTemplate;
    private StoreTheme                              mTheme;
    private String                                  mStoreBackground;
    private boolean                                 mIsCurrencyStoreDisabled;
}