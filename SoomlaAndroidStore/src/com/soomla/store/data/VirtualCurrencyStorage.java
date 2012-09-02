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
import com.soomla.store.StoreConfig;
import com.soomla.store.domain.data.VirtualCurrency;

/**
 * This class provide basic storage operations on VirtualCurrencies.
 */
public class VirtualCurrencyStorage {

    /** Constructor
     *
     */
    public VirtualCurrencyStorage() {
    }

    /** Public functions **/

    /**
     * Fetch the balance of the given virtual currency.
     * @param virtualCurrency is the required virtual currency.
     * @return the balance of the required virtual currency.
     */
    public int getBalance(VirtualCurrency virtualCurrency){
        if (StoreConfig.debug){
            Log.d(TAG, "trying to fetch balance for virtual currency");
        }

        String itemId = virtualCurrency.getItemId();
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

                if (StoreConfig.debug){
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

    /**
     * Adds the given amount of currency to the storage.
     * @param virtualCurrency is the required virtual currency.
     * @param amount is the amount of currency to add.
     * @return the new balance after adding amount.
     */
    public int add(VirtualCurrency virtualCurrency, int amount){
        if (StoreConfig.debug){
            Log.d(TAG, "adding " + amount + " currencies.");
        }

        int balance = getBalance(virtualCurrency);
        String quantityStr = "" + (balance + amount);
        String itemId = virtualCurrency.getItemId();
        if (StorageManager.getObfuscator() != null){
            quantityStr = StorageManager.getObfuscator().obfuscateString(quantityStr);
            itemId      = StorageManager.getObfuscator().obfuscateString(itemId);
        }
        StorageManager.getDatabase().updateVirtualCurrency(itemId, quantityStr);

        return balance + amount;
    }

    /**
     * Removes the given amount of currency from the storage.
     * @param virtualCurrency is the required virtual currency.
     * @param amount is the amount of currency to remove.
     */
    public int remove(VirtualCurrency virtualCurrency, int amount){
        if (StoreConfig.debug){
            Log.d(TAG, "removing " + amount + " currencies.");
        }

        String itemId = virtualCurrency.getItemId();
        int quantity = getBalance(virtualCurrency) - amount;
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
