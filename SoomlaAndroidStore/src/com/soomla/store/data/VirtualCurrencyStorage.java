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
import com.soomla.billing.util.AESObfuscator;
import com.soomla.store.SoomlaPrefs;

/**
 * This is the storage for the virtual currency.
 */
public class VirtualCurrencyStorage {

    /** Constructor
     *
     */
    public VirtualCurrencyStorage() {
    }

    /** Getters **/

    public int getBalance(){
        if (SoomlaPrefs.debug){
            Log.d(TAG, "trying to fetch balance for virtual currency");
        }

        String itemId = SoomlaPrefs.CURRENCY_ITEM_ID;
        if (StorageManager.getObfuscator() != null){
            itemId = StorageManager.getObfuscator().obfuscateString(itemId);
        }
        Cursor cursor = StorageManager.getDatabase().getVirtualCurrency(itemId);

        if (cursor == null) {
            return 0;
        }

        try {
            int balanceCol = cursor.getColumnIndexOrThrow(
                    StoreDatabase.VIRTUAL_CURRENCY_COLUMN_BALANCE);
            if (cursor.moveToNext()) {
                String balanceStr = cursor.getString(balanceCol);
                int balance;
                if (StorageManager.getObfuscator() != null){
                    balance = StorageManager.getObfuscator().unobfuscateToInt(balanceStr);
                }
                else {
                    balance = Integer.parseInt(balanceStr);
                }

                if (SoomlaPrefs.debug){
                    Log.d(TAG, "the currency balance is " + balance);
                }
                return balance;
            }
        } catch (AESObfuscator.ValidationException e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }

        return 0;
    }

    /** Public functions **/

    /**
     * Adds the given amount of currency to the storage.
     * @param amount is the amount of currency to add.
     * @return the new balance after adding amount.
     */
    public int add(int amount){
        if (SoomlaPrefs.debug){
            Log.d(TAG, "adding " + amount + " currencies.");
        }

        String itemId = SoomlaPrefs.CURRENCY_ITEM_ID;
        int balance = getBalance();
        String quantityStr = "" + (balance + amount);
        if (StorageManager.getObfuscator() != null){
            quantityStr = StorageManager.getObfuscator().obfuscateString(quantityStr);
            itemId      = StorageManager.getObfuscator().obfuscateString(itemId);
        }
        StorageManager.getDatabase().updateVirtualCurrency(itemId, quantityStr);

        return balance + amount;
    }

    /**
     * Removes the given amount of currency from the storage.
     * @param amount is the amount of currency to remove.
     */
    public int remove(int amount){
        if (SoomlaPrefs.debug){
            Log.d(TAG, "removing " + amount + " currencies.");
        }

        String itemId = SoomlaPrefs.CURRENCY_ITEM_ID;
        int quantity = getBalance() - amount;
        quantity = quantity > 0 ? quantity : 0;
        String quantityStr = "" + quantity;
        if (StorageManager.getObfuscator() != null){
            quantityStr = StorageManager.getObfuscator().obfuscateString(quantityStr);
            itemId      = StorageManager.getObfuscator().obfuscateString(itemId);
        }
        StorageManager.getDatabase().updateVirtualCurrency(itemId, quantityStr);

        return quantity;
    }

    /** Private members **/
    private static final String TAG = "SOOMLA VirtualCurrencyStorage";
}
