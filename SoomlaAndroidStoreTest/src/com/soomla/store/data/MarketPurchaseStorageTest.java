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
import android.provider.Settings;
import com.soomla.billing.Consts;
import com.soomla.store.StoreInfo;
import com.soomla.store.example.ExampleSurfStoreAssets;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.HashMap;

@RunWith(RobolectricTestRunner.class)
public class MarketPurchaseStorageTest {
    private MarketPurchaseStorage mStorage;

    @Before
    public void setUp(){
        StoreInfo.getInstance().initialize(new ExampleSurfStoreAssets());
        HashMap<String, String> secureData = new HashMap<String, String>();
        String deviceId = Settings.Secure.getString(Robolectric.application.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        secureData.put("applicationId", Robolectric.application.getApplicationContext().getPackageName());
        secureData.put("deviceId", deviceId);
        StorageManager.getInstance().initialize(Robolectric.application.getApplicationContext(), secureData);
        mStorage = new MarketPurchaseStorage();
    }

    @Test
    public void testAdd() throws Exception{
        long purchaseTime = Calendar.getInstance().getTimeInMillis();
        mStorage.add(Consts.PurchaseState.PURCHASED, "pipeline_pumpin_pack", "123456", purchaseTime,
                "This is the dev payload");

        Cursor cursor = StorageManager.getDatabase().getPurchaseHistory("123456");
        Assert.assertNotNull(cursor);

        try {
            int productIdCol = cursor.getColumnIndexOrThrow(
                    StoreDatabase.PURCHASE_HISTORY_COLUMN_PRODUCT_ID);
            if (cursor.moveToNext()) {
                String productId = cursor.getString(productIdCol);
                Assert.assertEquals(productId, "pipeline_pumpin_pack");
            }
        } finally {
            cursor.close();
        }
    }


}
