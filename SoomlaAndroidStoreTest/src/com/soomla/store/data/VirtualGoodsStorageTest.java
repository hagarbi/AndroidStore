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
