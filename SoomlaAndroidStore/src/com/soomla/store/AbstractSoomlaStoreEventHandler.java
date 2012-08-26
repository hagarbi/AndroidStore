package com.soomla.store;

import android.os.Parcel;
import android.os.Parcelable;
import com.soomla.store.domain.data.VirtualCurrencyPack;
import com.soomla.store.domain.data.VirtualGood;

public abstract class AbstractSoomlaStoreEventHandler implements Parcelable{
    public abstract void onVirtualCurrencyPackPurchased(VirtualCurrencyPack pack);
    public abstract void onVirtualGoodPurchased(VirtualGood good);

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
