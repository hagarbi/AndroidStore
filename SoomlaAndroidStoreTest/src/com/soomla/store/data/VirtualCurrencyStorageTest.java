package com.soomla.store.data;

import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Assert;

@RunWith(RobolectricTestRunner.class)
public class VirtualCurrencyStorageTest{

    private VirtualCurrencyStorage mStorage;

    @Before
    public void setUp() {
        mStorage = new VirtualCurrencyStorage(new FileStorage(Robolectric.application.getApplicationContext(),
                "soomla.virtualcurrency.test"));

//        System.out.println(Robolectric.application.getApplicationContext().getFilesDir());
    }

    @Test
    public void testAdd(){
        mStorage.add(10);
        Assert.assertEquals(mStorage.getBalance(), 10);
    }

    @Test
    public void testRemove(){
        mStorage.remove(2);
        Assert.assertEquals(mStorage.getBalance(), 0);
    }

    @Test
    public void testAddRemove(){
        mStorage.add(10);
        mStorage.remove(2);
        Assert.assertEquals(mStorage.getBalance(), 8);
    }
}
