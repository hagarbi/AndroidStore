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

public class StoreModalDialog {

    public StoreModalDialog(String mTemplate) {
        this.mTemplate = mTemplate;
    }

    public StoreModalDialog(JSONObject jsonObject) throws JSONException {
        mTemplate = jsonObject.getString(JSONConsts.STORE_MODALDIALOG_TEMPLATE);
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSONConsts.STORE_MODALDIALOG_TEMPLATE, mTemplate);

        return jsonObject;
    }

    private String mTemplate;
}
