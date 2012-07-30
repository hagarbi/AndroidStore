package com.soomla.store.data;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: refael
 * Date: 7/30/12
 * Time: 5:09 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IPersistenceStrategy {
    String fetch() throws IOException;

    void persist(String data) throws IOException;
}
