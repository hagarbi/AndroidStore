package com.soomla.store.domain;

import com.soomla.billing.Consts;

/**
 * This class is a representation of a purchase history.
 * We use it to save/load a single history.
 */
public class MarketPurchaseHistory {
    private Consts.PurchaseState mState;
    private String               mProductId;
    private String               mOrderId;
    private long                 mPurchaseTime;
    private String               mDevPayload;

    public MarketPurchaseHistory(){}

    public MarketPurchaseHistory(Consts.PurchaseState mState, String mProductId, String mOrderId, long mPurchaseTime, String mDevPayload) {
        this.mState = mState;
        this.mProductId = mProductId;
        this.mOrderId = mOrderId;
        this.mPurchaseTime = mPurchaseTime;
        this.mDevPayload = mDevPayload;
    }

    public Consts.PurchaseState getmState() {
        return mState;
    }

    public String getmProductId() {
        return mProductId;
    }

    public String getmOrderId() {
        return mOrderId;
    }

    public long getmPurchaseTime() {
        return mPurchaseTime;
    }

    public String getmDevPayload() {
        return mDevPayload;
    }
}
