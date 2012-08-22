package com.soomla.store;

import com.soomla.store.domain.data.VirtualCurrencyPack;
import com.soomla.store.domain.data.VirtualGood;

public interface ISoomlaStoreEventHandler {
    void onVirtualCurrencyPackPurchased(VirtualCurrencyPack pack);
    void onVirtualGoodPurchased(VirtualGood good);
}
