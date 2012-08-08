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

import com.soomla.billing.Consts;
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

    @Before
    public void setUp(){
        mStorage = new MarketPurchaseStorage(new FileStorage(
                Robolectric.application.getApplicationContext(),"soomla.marketpurchase.test"
        ));
    }

    @Test
    public void testAdd() throws Exception{
        long purchaseTime = Calendar.getInstance().getTimeInMillis();
        mStorage.add(Consts.PurchaseState.PURCHASED, "yellow_hat", "123456", purchaseTime,
                "This is the dev payload");

        String filename = Robolectric.application.getApplicationContext().getFilesDir() + "/soomla.marketpurchase.test";
        Assert.assertTrue(new File(filename).exists());

        FileInputStream fis = Robolectric.application.getApplicationContext().openFileInput("soomla.marketpurchase.test");
        InputStreamReader inputStreamReader = new InputStreamReader(fis);
        StringBuilder fileContent = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            fileContent.append(line);
        }
        fis.close();

        Assert.assertEquals(
                "{\"123456\":{\"mState\":\"PURCHASED\",\"mProductId\":\"yellow_hat\",\"mOrderId\":\"123456\",\"mPurchaseTime\":" +
                        purchaseTime +
                        ",\"mDevPayload\":\"This is the dev payload\"}}",
                new String(fileContent));
    }


}
