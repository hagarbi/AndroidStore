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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soomla.billing.Consts;

import java.io.IOException;
import java.util.HashMap;


/**
 * This class holds the virtual currency packs purchase history.
 */
public class MarketPurchaseStorage {

    /** Constructor
     *
     * @param mPhysicalStorage is the class responsible to persist the data for this storage.
     */
    public MarketPurchaseStorage(IPhysicalStorage mPhysicalStorage) {
        this.mPhysicalStorage = mPhysicalStorage;
    }

    public void add(final Consts.PurchaseState purchaseState, final String productId,
                   final String orderId, final long purchaseTime, final String developerPayload){
        storageFromJson(mPhysicalStorage.load());
        if (!mPurchaseHistories.containsKey(orderId)){
            mPurchaseHistories.put(orderId, new MarketPurchaseHistory(purchaseState, productId, orderId, purchaseTime, developerPayload));
            mPhysicalStorage.save(storageToJson());
        }
    }

    /**
     * This class is a representation of a purchase history.
     * We use it to save/load a single history.
     */
    private class MarketPurchaseHistory{
        private Consts.PurchaseState mState;
        private String               mProductId;
        private String               mOrderId;
        private long                 mPurchaseTime;
        private String               mDevPayload;

        private MarketPurchaseHistory(Consts.PurchaseState mState, String mProductId, String mOrderId, long mPurchaseTime, String mDevPayload) {
            this.mState = mState;
            this.mProductId = mProductId;
            this.mOrderId = mOrderId;
            this.mPurchaseTime = mPurchaseTime;
            this.mDevPayload = mDevPayload;
        }

        public Consts.PurchaseState getmState() {
            return mState;
        }

        public String getmProductId() {
            return mProductId;
        }

        public String getmOrderId() {
            return mOrderId;
        }

        public long getmPurchaseTime() {
            return mPurchaseTime;
        }

        public String getmDevPayload() {
            return mDevPayload;
        }
    }

    /** Private functions **/

    private void storageFromJson(String storageJson) {
        ObjectMapper mapper = new ObjectMapper();
        if (storageJson.isEmpty()){
            mPurchaseHistories = new HashMap<String, MarketPurchaseHistory>();
        }
        else{
            try {
                mPurchaseHistories = mapper.readValue(storageJson,
                        new TypeReference<HashMap<String,MarketPurchaseHistory>>() {});
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String storageToJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(mPurchaseHistories);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    /** Private members **/

    private IPhysicalStorage mPhysicalStorage;
    private HashMap<String, MarketPurchaseHistory> mPurchaseHistories;
}
