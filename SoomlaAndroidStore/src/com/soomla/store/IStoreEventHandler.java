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

import com.soomla.store.domain.data.VirtualCurrencyPack;
import com.soomla.store.domain.data.VirtualGood;

/**
 * This interface represents an event handler.
 * If you want, you can implement your own using this interface and
 * add it to {@link StoreEventHandlers} using
 * 'StoreEventHandlers.getInstance().addEventHandler([your handler here]);'
 */
public interface IStoreEventHandler {
    void onVirtualCurrencyPackPurchased(VirtualCurrencyPack pack);
    void onVirtualGoodPurchased(VirtualGood good);
    void onBillingSupported();
    void onBillingNotSupported();
    void onMarketPurchaseProcessStarted();
    void onGoodsPurchaseProcessStarted();
    void onClosingStore();
    void onUnexpectedErrorInStore();
    void onOpeningStore();
}
