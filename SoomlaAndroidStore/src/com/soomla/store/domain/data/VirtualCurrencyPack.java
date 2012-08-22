/*
 * Copyright (C) 2012 Soomla Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.soomla.store.domain.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class represents a pack of the game's virtual currency.
 * For example: If you have a "Coin" as a virtual currency, you might
 * want to sell packs of "Coins". e.g. "10 Coins".
 * The currency pack usually has a google item related to it. As a developer,
 * you'll define the google item in Google's in-app purchase dashboard.
 */
public class VirtualCurrencyPack extends VirtualItem {

    /** Constructor
     *
     * @param mName is the name of the virtual currency pack.
     * @param mDescription is the description of the virtual currency pack. This will show up
     *                       in the store in the description section.
     * @param mImgFilePath is the path to the image that corresponds to the currency pack.
     * @param mItemId is the id of the virtual currency pack.
     * @param productId is the product id on Google Market..
     * @param mCost is the actual $$ cost of the virtual currency pack.
     * @param mCurrencyAmout is the amount of currency in the pack.
     */
    public VirtualCurrencyPack(String mName, String mDescription, String mImgFilePath, String mItemId,
                               String productId, double mCost, int mCurrencyAmout, boolean consumable) {
        super(mName, mDescription, mImgFilePath, mItemId);
        this.mGoogleItem = new GoogleMarketItem(productId, GoogleMarketItem.Managed.UNMANAGED);
        this.mCost = mCost;
        this.mCurrencyAmount = mCurrencyAmout;
        this.mConsumable = consumable;
    }

    /** Getters **/

    @JsonIgnore
    public GoogleMarketItem getmGoogleItem() {
        return mGoogleItem;
    }

    public String getProductId(){
        return mGoogleItem.getMarketId();
    }

    @JsonProperty("price")
    public double getCost() {
        return mCost;
    }

    @JsonProperty("amount")
    public int getCurrencyAmount() {
        return mCurrencyAmount;
    }

    public boolean isConsumable() {
        return mConsumable;
    }

    /** Private members **/

    private GoogleMarketItem mGoogleItem;
    private double           mCost;
    private int              mCurrencyAmount;
    private boolean          mConsumable;
}
