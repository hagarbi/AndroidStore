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
import com.soomla.store.domain.VirtualGood;

/**
 * This is the storage for the virtual goods.
 */
public class VirtualGoodsStorage {

    /** Constructor
     *
     * @param mPhysicalStorage is the class responsible to persist the data for this storage.
     */
    public VirtualGoodsStorage(IPhysicalStorage mPhysicalStorage) {
        this.mPhysicalStorage = mPhysicalStorage;
        this.mStore = new HashMap<String, Integer>();
    }

    /** Getters **/

    public int getBalance(VirtualGood vgood){
        storageFromJson(mPhysicalStorage.load());
		return mStore.get(vgood.getItemId());
	}

    /** Public functions **/

    /**
    * Adds the given amount of currency to the storage.
    * @param amount is the amount of currency to add.
    */
    public void add(VirtualGood vgood, int amount){
		if (!mStore.containsKey(vgood.getItemId())){
			mStore.put(vgood.getItemId(), 0);
		}
		
		mStore.put(vgood.getItemId(), mStore.get(vgood.getItemId()) + amount);
        mPhysicalStorage.save(storageToJson());
	}

    /**
     * Removes the given amount from the given virtual good's balance.
     * @param virtualGood is the virtual good to remove the given amount from.
     * @param amount is the amount to remove.
     */
    public void remove(VirtualGood virtualGood, int amount){
		if (!mStore.containsKey(virtualGood.getItemId())){
			return;
		}
		
		int balance = mStore.get(virtualGood.getItemId()) - amount;
		mStore.put(virtualGood.getItemId(), balance > 0 ? balance : 0);
        mPhysicalStorage.save(storageToJson());
	}

    /** Private functions **/

    private void storageFromJson(String storageJson) {
        Gson gson = new Gson();
        mStore = gson.fromJson(storageJson, new TypeToken<HashMap<String, Integer>>() {}.getType());
    }

    private String storageToJson() {
        Gson gson = new Gson();
        return gson.toJson(mStore);
    }

    /** Private members **/

    private HashMap<String, Integer> mStore;
    private IPhysicalStorage mPhysicalStorage;

}
