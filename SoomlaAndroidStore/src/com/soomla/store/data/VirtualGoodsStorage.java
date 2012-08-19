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
import android.util.Log;
import com.soomla.store.SoomlaPrefs;
import com.soomla.store.domain.VirtualGood;

/**
 * This is the storage for the virtual goods.
 */
public class VirtualGoodsStorage {

    /** Constructor
     *
     * @param storeDatabase is the SQLite database.
     */
    public VirtualGoodsStorage(StoreDatabase storeDatabase) {
        this.mStoreDatabase = storeDatabase;
    }

    /** Getters **/

    public int getBalance(VirtualGood virtualGood){
        if (SoomlaPrefs.debug){
            Log.d(TAG, "trying to fetch balance for virtual good with itemId: " + virtualGood.getItemId());
        }
        Cursor cursor = mStoreDatabase.getVirtualGood(virtualGood.getItemId());

        if (cursor == null) {
            return 0;
        }

        try {
            int balanceCol = cursor.getColumnIndexOrThrow(
                    StoreDatabase.VIRTUAL_GOODS_COLUMN_BALANCE);
            if (cursor.moveToNext()) {
                int balance = cursor.getInt(balanceCol);
                if (SoomlaPrefs.debug){
                    Log.d(TAG, "the balance for " + virtualGood.getItemId() + " is " + balance);
                }
                return balance;
            }
        } finally {
            cursor.close();
        }

        return 0;
	}

    /** Public functions **/

    /**
    * Adds the given amount of currency to the storage.
    * @param amount is the amount of currency to add.
    */
    public int add(VirtualGood virtualGood, int amount){
        if (SoomlaPrefs.debug){
            Log.d(TAG, "adding " + amount + " " + virtualGood.getName() + ".");
        }

        int balance = getBalance(virtualGood);
        mStoreDatabase.updateVirtualGood(virtualGood.getItemId(), balance + amount);

        return balance + amount;
	}

    /**
     * Removes the given amount from the given virtual good's balance.
     * @param virtualGood is the virtual good to remove the given amount from.
     * @param amount is the amount to remove.
     */
    public int remove(VirtualGood virtualGood, int amount){
        if (SoomlaPrefs.debug){
            Log.d(TAG, "removing " + amount + " " + virtualGood.getName() + ".");
        }

        int quantity = getBalance(virtualGood) - amount;
        quantity = quantity > 0 ? quantity : 0;
        mStoreDatabase.updateVirtualGood(virtualGood.getItemId(), quantity);

        return quantity;
	}

    /** Private members **/
    private static final String TAG = "SOOMLA VirtualGoodsStorage";

    private StoreDatabase            mStoreDatabase;
}
