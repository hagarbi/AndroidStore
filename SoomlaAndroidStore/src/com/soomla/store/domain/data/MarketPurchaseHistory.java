package com.soomla.store.domain.data;

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
    private int                  mCurrentBalance;
    private String               mItemId;

    public MarketPurchaseHistory(){}

    public MarketPurchaseHistory(Consts.PurchaseState mState, String mProductId, String mOrderId,
                                 long mPurchaseTime, String mDevPayload, int mCurrentBalance, String mItemId) {
        this.mState = mState;
        this.mProductId = mProductId;
        this.mOrderId = mOrderId;
        this.mPurchaseTime = mPurchaseTime;
        this.mDevPayload = mDevPayload;
        this.mCurrentBalance = mCurrentBalance;
        this.mItemId = mItemId;
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

    public int getmCurrentBalance() {
        return mCurrentBalance;
    }

    public String getmItemId() {
        return mItemId;
    }
}
