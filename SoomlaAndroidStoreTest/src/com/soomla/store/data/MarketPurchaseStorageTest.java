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
import com.soomla.billing.Consts;
import com.soomla.store.StoreInfo;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

@RunWith(RobolectricTestRunner.class)
public class MarketPurchaseStorageTest {
    private MarketPurchaseStorage mStorage;
    private StoreDatabase         mDB;

    @Before
    public void setUp(){
        StoreInfo.getInstance().initialize(Robolectric.application.getApplicationContext());
        StorageManager.getInstance().initialize(Robolectric.application.getApplicationContext());
        mDB = new StoreDatabase(Robolectric.application.getApplicationContext());
        mStorage = new MarketPurchaseStorage(mDB);
    }

    @Test
    public void testAdd() throws Exception{
        long purchaseTime = Calendar.getInstance().getTimeInMillis();
        mStorage.add(Consts.PurchaseState.PURCHASED, "pipeline_pumpin_pack", "123456", purchaseTime,
                "This is the dev payload");

        Cursor cursor = mDB.getPurchaseHistory("123456");
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
