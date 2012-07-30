package com.soomla.store.tests.data;

import com.soomla.store.domain.VGood;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * Created with IntelliJ IDEA.
 * User: refaelos
 * Date: 7/17/12
 * Time: 8:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class VGoodTest extends TestCase {
    VGood mVgood;

    @Before
    public void setUp() throws Exception {
        mVgood = new VGood("Coat", "Coat for child", "");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetName() throws Exception {
        assertEquals(mVgood.getName(), "Coat");
    }

    @Test
    public void testGetDescription() throws Exception {
        assertEquals(mVgood.getDescription(), "Coat for child");
    }
}
