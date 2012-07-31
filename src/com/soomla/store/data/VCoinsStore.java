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

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

public class VCoinsStore extends AbstractVStore {
	private int     mBalance;
    private String  mImageFilePath;

    public VCoinsStore(Context context, IPersistenceStrategy strategy) {
        super(strategy, context);
    }

    @Override
    protected void storeFromJson(String storeJson) {
        Gson gson = new Gson();
        HashMap<String, Object> json = gson.fromJson(storeJson, new TypeToken<HashMap<String, Integer>>() {}.getType());
        mBalance =       (Integer)json.get("balance");
        mImageFilePath = (String)json.get("image");
    }

    @Override
    protected String storeToJson() {
        Gson gson = new Gson();

        HashMap<String, Object> json = new HashMap<String, Object>();
        json.put("balance", mBalance);
        json.put("image", mImageFilePath);

        return gson.toJson(json);
    }
}
