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

import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.soomla.store.domain.VGood;

public class VGoodsStore extends AbstractVStore {
	private HashMap<String, Integer> mStore;

    public VGoodsStore() {
        this.mStore = new HashMap<String, Integer>();
    }

    public int getBalance(VGood vgood){
		return mStore.get(vgood.getSoomlaId());
	}

    public void add(VGood vgood, int amount){
		if (!mStore.containsKey(vgood.getSoomlaId())){
			mStore.put(vgood.getSoomlaId(), 0);
		}
		
		mStore.put(vgood.getSoomlaId(), mStore.get(vgood.getSoomlaId()) + amount);
	}

    public void remove(VGood vgood, int amount){
		if (!mStore.containsKey(vgood.getSoomlaId())){
			return;
		}
		
		int balance = mStore.get(vgood.getSoomlaId()) - amount;
		mStore.put(vgood.getSoomlaId(), balance > 0 ? balance : 0);
	}

    @Override
    protected String storeFilePath() {
        return "soomla.store";
    }

    @Override
    protected void storeFromJson(String storeJson) {
        Gson gson = new Gson();
        mStore = gson.fromJson(storeJson, new TypeToken<HashMap<String, Integer>>() {}.getType());
    }

    @Override
    protected String storeToJson() {
        Gson gson = new Gson();
        return gson.toJson(mStore);
    }

}
