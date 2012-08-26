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

import java.util.LinkedList;
import java.util.List;

/**
 * This class is used to control all event handlers.
 * Use this class to invoke events on handlers when they occur.
 */
public class StoreEventHandlers {

    /**
     * A currency pack was just purchased.
     * @param pack is the pack that was just purchased.
     */
    public void onVirtualCurrencyPackPurchased(VirtualCurrencyPack pack){
        for(IStoreEventHandler handler : mEventHandlers){
            handler.onVirtualCurrencyPackPurchased(pack);
        }
    }

    /**
     * A virtual good was just purchased.
     * @param good is the virtual good that was just purchased.
     */
    public void onVirtualGoodPurchased(VirtualGood good){
        for(IStoreEventHandler handler : mEventHandlers){
            handler.onVirtualGoodPurchased(good);
        }
    }


    /**
     * Adds an event handler to the list of handlers.
     * @param eventHandler is the event handler to add.
     */
    public void addEventHandler(IStoreEventHandler eventHandler){
        mEventHandlers.add(eventHandler);
    }

    public static StoreEventHandlers getInstance(){
        if(sInstance == null){
            sInstance = new StoreEventHandlers();
        }

        return sInstance;
    }

    private StoreEventHandlers(){
        mEventHandlers = new LinkedList<IStoreEventHandler>();
    }

    /** Private members **/

    private static StoreEventHandlers sInstance = new StoreEventHandlers();
    private List<IStoreEventHandler> mEventHandlers;
}
