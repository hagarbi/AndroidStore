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

import android.util.Log;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soomla.store.SoomlaPrefs;
import com.soomla.store.SoomlaPrefs;

import java.io.IOException;
import java.util.HashMap;

/**
 * This is the storage for the virtual currency.
 */
public class VirtualCurrencyStorage {

    /** Constructor
     *
     * @param mPhysicalStorage is the class responsible to persist the data for this storage.
     */
    public VirtualCurrencyStorage(IPhysicalStorage mPhysicalStorage) {
        this.mPhysicalStorage = mPhysicalStorage;
    }

    /** Getters **/

    public int getBalance(){
        storageFromJson(mPhysicalStorage.load());
        return mBalance;
    }

    /** Public functions **/

    /**
     * Adds the given amount of currency to the storage.
     * @param amount is the amount of currency to add.
     */
    public void add(int amount){
        if (SoomlaPrefs.debug){
            Log.d(TAG, "adding " + amount + " currencies.");
        }

        mBalance += amount;
        mPhysicalStorage.save(storageToJson());
    }

    /**
     * Removes the given amount of currency from the storage.
     * @param amount is the amount of currency to remove.
     */
    public void remove(int amount){
        if (SoomlaPrefs.debug){
            Log.d(TAG, "removing " + amount + " currencies.");
        }

        mBalance -= amount;
        if (mBalance < 0) mBalance = 0; // you can't have negative amount of currency

        mPhysicalStorage.save(storageToJson());
    }

    /** Private functions **/

    private void storageFromJson(String storageJson) {
        mBalance = 0;

        ObjectMapper mapper = new ObjectMapper();
        try {
            HashMap<String, Object> json = mapper.readValue(storageJson,
                    new TypeReference<HashMap<String,Object>>() {});
            mBalance =       (Integer)json.get("balance");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String storageToJson() {
        ObjectMapper mapper = new ObjectMapper();

        HashMap<String, Object> json = new HashMap<String, Object>();
        json.put("balance", mBalance);

        try {
            return mapper.writeValueAsString(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    /** Private members **/
    private static final String TAG = "SOOMLA VirtualCurrencyStorage";

    private int     mBalance;
    private IPhysicalStorage mPhysicalStorage;
}
