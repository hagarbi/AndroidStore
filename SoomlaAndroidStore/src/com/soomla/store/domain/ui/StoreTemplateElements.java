package com.soomla.store.domain.ui;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StoreTemplateElements {

    public StoreTemplateElements(StoreTitleElement mTitleElement, StoreBuyMoreElement mBuyMoreElement) {
        this.mTitleElement = mTitleElement;
        this.mBuyMoreElement = mBuyMoreElement;
    }

    @JsonProperty("title")
    public StoreTitleElement getTitleElement() {
        return mTitleElement;
    }

    @JsonProperty("buyMore")
    public StoreBuyMoreElement getBuyMoreElement() {
        return mBuyMoreElement;
    }

    private StoreTitleElement mTitleElement;
    private StoreBuyMoreElement mBuyMoreElement;
}
