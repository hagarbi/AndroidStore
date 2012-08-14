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
import com.soomla.store.SoomlaConsts;

/**
 * This is the place where all the relevant storage classes are created.
 * This is a singleton class and you can call it from your application in order
 * to get he instances of the Virtual goods/currency storage. You will usually
 * need the storage in order to get/set the amounts of virtual goods/currency.
 */
public class StorageManager {

    public static StorageManager getInstance(){
        if (sInstance == null){
            sInstance = new StorageManager();
        }

        return sInstance;
    }

    public void initialize(IPhysicalStorage virtualCurrencyPhysicalStorage,
                           IPhysicalStorage virtualGoodsPhysicalStorage,
                           IPhysicalStorage marketPurchasePhysicalStorage){
        if (SoomlaConsts.DEBUG){
            Log.d(TAG, "initializing StorageManager");
        }
        mVirtualCurrencyStorage =   new VirtualCurrencyStorage(virtualCurrencyPhysicalStorage);
        mVirtualGoodsStorage =      new VirtualGoodsStorage(virtualGoodsPhysicalStorage);
        mMarketPurchaseStorage =    new MarketPurchaseStorage(marketPurchasePhysicalStorage);
    }

    public VirtualCurrencyStorage getVirtualCurrencyStorage(){
        return mVirtualCurrencyStorage;
    }

    public VirtualGoodsStorage getVirtualGoodsStorage(){
        return mVirtualGoodsStorage;
    }

    public MarketPurchaseStorage getMarketPurchaseStorage() {
        return mMarketPurchaseStorage;
    }

    private StorageManager(){ }

    /** Private members **/
    private static final String TAG = "StorageManager";

    private VirtualGoodsStorage     mVirtualGoodsStorage;
    private VirtualCurrencyStorage  mVirtualCurrencyStorage;
    private MarketPurchaseStorage   mMarketPurchaseStorage;
    private static StorageManager   sInstance;
}
