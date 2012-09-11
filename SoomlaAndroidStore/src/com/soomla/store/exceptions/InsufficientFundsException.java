package com.soomla.store.exceptions;

public class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String itemId) {
        super("You tried to buy with itemId: " + itemId + " but you don't have enough money to buy it.");

        mItemId = itemId;
    }

    public String getItemId() {
        return mItemId;
    }

    private String mItemId;
}
