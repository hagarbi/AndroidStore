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

import com.soomla.store.StoreInfo;
import com.soomla.store.domain.data.VirtualGood;
import com.soomla.store.example.ExampleSurfStoreAssets;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RobolectricTestRunner.class)
public class VirtualGoodsStorageTest {
    private VirtualGoodsStorage mStorage;
    private VirtualGood         mYellowHat;

    private final String YELLOW_HAT_NAME = "Yellow Hat";
    private final String YELLOW_HAT_DESC = "Just a hat to test with";

    @Before
    public void setUp() {
        StoreInfo.getInstance().initialize(new ExampleSurfStoreAssets());
        StorageManager.getInstance().initialize(Robolectric.application.getApplicationContext(), null);
        mStorage = new VirtualGoodsStorage();

        mYellowHat = new VirtualGood(YELLOW_HAT_NAME, YELLOW_HAT_DESC, "image", 3, "yellow_hat");
    }

    @Test
    public void testAdd(){
        mStorage.add(mYellowHat, 10);
        Assert.assertEquals(10, mStorage.getBalance(mYellowHat));
    }

    @Test
    public void testRemove(){
        mStorage.remove(mYellowHat, 2);
        Assert.assertEquals(0, mStorage.getBalance(mYellowHat));
    }

    @Test
    public void testAddRemove(){
        mStorage.add(mYellowHat, 10);
        mStorage.remove(mYellowHat, 2);
        Assert.assertEquals(8, mStorage.getBalance(mYellowHat));
    }
}
