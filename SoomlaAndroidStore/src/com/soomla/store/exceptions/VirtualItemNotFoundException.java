package com.soomla.store.exceptions;

public class VirtualItemNotFoundException extends Exception {
    public VirtualItemNotFoundException(String lookupBy, String lookupVal) {
        super("Virtual item was not found when searching with " + lookupBy + "=" + lookupVal);
    }
}
