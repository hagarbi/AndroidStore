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
import com.soomla.billing.Consts;
import com.soomla.store.SoomlaPrefs;
import com.soomla.store.SoomlaPrefs;
import com.soomla.store.StoreInfo;
import com.soomla.store.domain.data.MarketPurchaseHistory;
import com.soomla.store.domain.data.VirtualCurrencyPack;
import com.soomla.store.exceptions.VirtualItemNotFoundException;

import java.io.IOException;
import java.util.HashMap;


/**
 * This class holds the virtual currency packs purchase history.
 */
public class MarketPurchaseStorage {

    /** Constructor
     *
     */
    public MarketPurchaseStorage() {
    }

    public void add(final Consts.PurchaseState purchaseState, String productId,
                   String orderId, final long purchaseTime, String developerPayload) throws VirtualItemNotFoundException {
        if (SoomlaPrefs.debug){
            Log.d(TAG, "adding market purchase data for orderId " + orderId + " and productId " + productId);
        }

        String itemId = StoreInfo.getInstance().getPackByGoogleProductId(productId).getItemId();
        String purchaseStateStr = "" + purchaseState.ordinal();
        String purchaseTimeStr  = "" + purchaseTime;
        String currentBalanceStr = "" + StorageManager.getInstance().getVirtualCurrencyStorage().getBalance();
        if (StorageManager.getObfuscator() != null){
            orderId   =         StorageManager.getObfuscator().obfuscateString(orderId);
            productId =         StorageManager.getObfuscator().obfuscateString(productId);
            itemId =            StorageManager.getObfuscator().obfuscateString(itemId);
            purchaseStateStr =  StorageManager.getObfuscator().obfuscateString(purchaseStateStr);
            purchaseTimeStr  =  StorageManager.getObfuscator().obfuscateString(purchaseTimeStr);
            developerPayload =  StorageManager.getObfuscator().obfuscateString(developerPayload);
            currentBalanceStr = StorageManager.getObfuscator().obfuscateString(currentBalanceStr);
        }

        StorageManager.getDatabase().addOrUpdatePurchaseHistory(orderId, productId, itemId,
                purchaseStateStr, purchaseTimeStr, developerPayload, currentBalanceStr);
    }

    /** Private members **/
    private static final String TAG = "SOOMLA MarketPurchaseStorage";
}
