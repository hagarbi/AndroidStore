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
import com.soomla.store.domain.MarketPurchaseHistory;
import com.soomla.store.domain.VirtualCurrencyPack;
import com.soomla.store.exceptions.VirtualItemNotFoundException;

import java.io.IOException;
import java.util.HashMap;


/**
 * This class holds the virtual currency packs purchase history.
 */
public class MarketPurchaseStorage {

    /** Constructor
     *
     * @param storeDatabase is the class responsible to persist the data for this storage.
     */
    public MarketPurchaseStorage(StoreDatabase storeDatabase) {
        this.mStoreDatabase = storeDatabase;
    }

    public void add(final Consts.PurchaseState purchaseState, final String productId,
                   final String orderId, final long purchaseTime, final String developerPayload) throws VirtualItemNotFoundException {
        if (SoomlaPrefs.debug){
            Log.d(TAG, "adding market purchase data for orderId " + orderId + " and productId " + productId);
        }

        VirtualCurrencyPack pack = StoreInfo.getInstance().getPackByGoogleProductId(productId);

        mStoreDatabase.addOrUpdatePurchaseHistory(orderId, productId, pack.getItemId(),
                purchaseState, purchaseTime, developerPayload, StorageManager.getInstance().getVirtualCurrencyStorage().getBalance());
    }

    /** Private members **/
    private static final String TAG = "SOOMLA MarketPurchaseStorage";

    private StoreDatabase mStoreDatabase;
}
