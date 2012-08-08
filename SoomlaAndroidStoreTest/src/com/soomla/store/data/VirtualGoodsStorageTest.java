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

import com.soomla.store.domain.VirtualGood;
import com.soomla.store.test.R;
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

    @Before
    public void setUp() {
        mStorage = new VirtualGoodsStorage(new FileStorage(Robolectric.application.getApplicationContext(),
                "soomla.virtualgoods.test"));

        mYellowHat = new VirtualGood(R.string.yellow_hat_name, R.string.yellow_hat_description, "image", 3, "yellow_hat");
    }

    @Test
    public void testAdd(){
        mStorage.add(mYellowHat, 10);
        Assert.assertEquals(mStorage.getBalance(mYellowHat), 10);
    }

    @Test
    public void testRemove(){
        mStorage.remove(mYellowHat, 2);
        Assert.assertEquals(mStorage.getBalance(mYellowHat), 0);
    }

    @Test
    public void testAddRemove(){
        mStorage.add(mYellowHat, 10);
        mStorage.remove(mYellowHat, 2);
        Assert.assertEquals(mStorage.getBalance(mYellowHat), 8);
    }
}
