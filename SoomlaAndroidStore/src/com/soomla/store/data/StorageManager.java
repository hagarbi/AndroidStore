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

import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import com.soomla.billing.util.AESObfuscator;
import com.soomla.store.StoreConfig;

/**
 * This is the place where all the relevant storage classes are created.
 * This is a singleton class and you can call it from your application in order
 * to get the instances of the Virtual goods/currency storages.
 *
 * You will usually need the storage in order to get/set the amounts of virtual goods/currency.
 */
public class StorageManager {

    public static StorageManager getInstance(){
        if (sInstance == null){
            sInstance = new StorageManager();
        }

        return sInstance;
    }

    /**
     * {@link StorageManager} is initizlized by {@link com.soomla.store.StoreController} right after the latter is
     * initialized buy your application.
     * @param context is the application context. {@link StorageManager} uses the context to let {@link
     * StoreDatabase} know where to save the DB file and to provide {@link AESObfuscator} with parts of information
     * to create the encryption secret.
     */
    public void initialize(Context context){
        if (StoreConfig.debug){
            Log.d(TAG, "initializing StorageManager");
        }

        sDatabase = new StoreDatabase(context);

        if(StoreConfig.DB_SECURE){
            String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            sObfuscator = new AESObfuscator(StoreConfig.obfuscationSalt, context.getPackageName(), deviceId);
        }

        mVirtualCurrencyStorage =   new VirtualCurrencyStorage();
        mVirtualGoodsStorage =      new VirtualGoodsStorage();
    }


    /** Getters **/

    public VirtualCurrencyStorage getVirtualCurrencyStorage(){
        return mVirtualCurrencyStorage;
    }

    public VirtualGoodsStorage getVirtualGoodsStorage(){
        return mVirtualGoodsStorage;
    }

    public static AESObfuscator getObfuscator(){
        return sObfuscator;
    }

    public static StoreDatabase getDatabase(){
        return sDatabase;
    }

    private StorageManager(){ }

    /** Private members **/
    private static final String TAG = "SOOMLA StorageManager";

    private VirtualGoodsStorage     mVirtualGoodsStorage;
    private VirtualCurrencyStorage  mVirtualCurrencyStorage;
    private static StorageManager   sInstance;

    private static AESObfuscator    sObfuscator;
    private static StoreDatabase    sDatabase;
}
