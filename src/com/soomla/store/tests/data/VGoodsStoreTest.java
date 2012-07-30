package com.soomla.store.tests.data;

import com.soomla.store.domain.VGood;
import com.soomla.store.data.VGoodsStore;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: refaelos
 * Date: 7/17/12
 * Time: 9:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class VGoodsStoreTest extends TestCase {
    VGoodsStore mStore;
    VGood       mCoat;

    @Before
    public void setUp() throws Exception {
        mStore = new VGoodsStore();
        mCoat = new VGood("Coat", "Coat for child.", "");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testAdd() throws Exception {
        mStore.add(mCoat, 12);
        assertEquals(mStore.getBalance(mCoat), 12);

        mStore.add(mCoat, 2);
        assertEquals(mStore.getBalance(mCoat), 14);
    }

    @Test
    public void testRemove() throws Exception {
        mStore.add(mCoat, 12);

        mStore.remove(mCoat, 2);
        assertEquals(mStore.getBalance(mCoat), 10);
    }

    @Test
    public void testSync() throws Exception {

    }
}
