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
package com.soomla.store;

import android.os.Parcel;
import android.os.Parcelable;
import com.soomla.store.domain.data.VirtualCurrency;
import com.soomla.store.domain.data.VirtualCurrencyPack;
import com.soomla.store.domain.data.VirtualGood;
import com.soomla.store.domain.ui.StoreTemplate;

import java.util.List;

/**
 * This interface represents a single game's metadata.
 * Use this interface to create your assets class that will be transferred to StoreInfo
 * upon initialization.
 */
public abstract class AbstractStoreAssets implements Parcelable{
    /**
     * The template that you chose to use for your store's.
     * @return your chosen template.
     */
    public abstract StoreTemplate getStoreTemplate();

    /**
     * A path to the image that you want to use as your store background.
     * @return a path to an image file.
     */
    public abstract String getStoreBackground();

    /**
     * A representation of your game's virtual currency.
     * @return a representation of your game's virtual currency.
     */
    public abstract VirtualCurrency getVirtualCurrency();

    /**
     * A list of all virtual goods served by your store.
     * @return a list of all virtual goods served by your store.
     */
    public abstract List<VirtualGood> getVirtualGoods();

    /**
     * A list of all virtual currency packs served by your store.
     * @return a list of all virtual currency packs served by your store.
     */
    public abstract List<VirtualCurrencyPack> getVirtualCurrencyPacks();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }

}
