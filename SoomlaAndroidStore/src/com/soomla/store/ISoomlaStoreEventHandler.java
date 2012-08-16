package com.soomla.store;

import com.soomla.store.domain.VirtualCurrencyPack;
import com.soomla.store.domain.VirtualGood;

public interface ISoomlaStoreEventHandler {
    void onVirtualCurrencyPackPurchased(VirtualCurrencyPack pack);
    void onVirtualGoodPurchased(VirtualGood good);
}
