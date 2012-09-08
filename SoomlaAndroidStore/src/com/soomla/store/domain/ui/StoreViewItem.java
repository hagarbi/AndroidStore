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


import com.soomla.store.data.JSONConsts;
import org.json.JSONException;
import org.json.JSONObject;

public class StoreViewItem {

    public StoreViewItem(String mType, String mTemplate) {
        this.mType = mType;
        this.mTemplate = mTemplate;
    }

    public StoreViewItem(JSONObject jsonObject) throws JSONException {
        mType     = jsonObject.getString(JSONConsts.STORE_VIEWITEM_TYPE);
        mTemplate = jsonObject.getString(JSONConsts.STORE_VIEWITEM_TEMPLATE);
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSONConsts.STORE_VIEWITEM_TYPE, mType);
        jsonObject.put(JSONConsts.STORE_VIEWITEM_TEMPLATE, mTemplate);

        return jsonObject;
    }

    private String mType;
    private String mTemplate;
}
