package com.soomla.store.domain;

/**
 * Created with IntelliJ IDEA.
 * User: refael
 * Date: 7/29/12
 * Time: 1:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class VCurrencyPack extends VItem{
    /* The Google Market item thet's related to this pack. */
    private GoogleMarketItem mGoogleItem;
    /* The cost of this pack in the Market. */
    private int mCost;
    /* The amount of currency this pack contains. */
    private int mCurrencyAmout;

    public VCurrencyPack(int mNameId, int mDescriptionId, String mImgFilePath, String mSoomlaId,
                         GoogleMarketItem mGoogleItem, int mCost, int mCurrencyAmout) {
        super(mNameId, mDescriptionId, mImgFilePath, mSoomlaId);
        this.mGoogleItem = mGoogleItem;
        this.mCost = mCost;
        this.mCurrencyAmout = mCurrencyAmout;
    }

    public GoogleMarketItem getmGoogleItem() {
        return mGoogleItem;
    }

    public int getmCost() {
        return mCost;
    }

    public int getmCurrencyAmout() {
        return mCurrencyAmout;
    }
}
