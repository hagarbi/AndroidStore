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
import com.soomla.store.data.JSONConsts;
import org.json.JSONException;
import org.json.JSONObject;

public class StoreTheme {

    public StoreTheme(StoreView mVirtualGoodsView, StoreView mCurrencyPacksView,
                      StoreModalDialog mModalDialog, String mTemplateName, String mName) {
        this.mVirtualGoodsView = mVirtualGoodsView;
        this.mCurrencyPacksView = mCurrencyPacksView;
        this.mModalDialog = mModalDialog;
        this.mTemplateName = mTemplateName;
        this.mName = mName;
    }

    public StoreTheme(JSONObject jsonObject) throws JSONException {
        mVirtualGoodsView = new StoreView(jsonObject.getJSONObject(JSONConsts.STORE_THEME_GOODSVIEW));
        mCurrencyPacksView = new StoreView(jsonObject.getJSONObject(JSONConsts.STORE_THEME_CURRENCYPACKSVIEW));
        mModalDialog = new StoreModalDialog(jsonObject.getJSONObject(JSONConsts.STORE_THEME_MODALDIALOG));
        mTemplateName = jsonObject.getString(JSONConsts.STORE_THEME_TEMPLATE);
        mName = jsonObject.getString(JSONConsts.STORE_THEME_NAME);
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put(JSONConsts.STORE_THEME_GOODSVIEW, mVirtualGoodsView.toJSONObject());
            jsonObject.put(JSONConsts.STORE_THEME_CURRENCYPACKSVIEW, mCurrencyPacksView.toJSONObject());
            jsonObject.put(JSONConsts.STORE_THEME_MODALDIALOG, mModalDialog.toJSONObject());
            jsonObject.put(JSONConsts.STORE_THEME_TEMPLATE, mTemplateName);
            jsonObject.put(JSONConsts.STORE_THEME_NAME, mName);
        } catch (JSONException e) {
            if (StoreConfig.debug){
                Log.d(TAG, "An error occurred while generating JSON object.");
            }
        }

        return jsonObject;
    }


    private static final String TAG = "SOOMLA StoreTheme";

    private StoreView     mVirtualGoodsView;
    private StoreView     mCurrencyPacksView;
    private StoreModalDialog mModalDialog;
    private String        mTemplateName;
    private String        mName;
}
