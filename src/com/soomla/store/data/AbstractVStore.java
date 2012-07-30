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

package com.soomla.store.data;

import android.content.Context;

import java.io.*;

public abstract class AbstractVStore {

    /* Public functions */

    /**
     * This function loads the store's data from the given persistence strategy.
     * @throws IOException
     */
    public void load() throws IOException {
        storeFromJson(mPersistenceStrategy.fetch());
    }

    /**
     * This function saves the store's data from the given persistence strategy.
     * @throws IOException
     */
    public void save() throws IOException {
        mPersistenceStrategy.persist(storeToJson());
    }

    /**
     * Abstract class constructor
     * @param mPersistenceStrategy is the strategy used for persistence
     * @param mContext is the current application's context
     */
    protected AbstractVStore(IPersistenceStrategy mPersistenceStrategy, Context mContext) {
        this.mPersistenceStrategy = mPersistenceStrategy;
        this.mContext =             mContext;
    }

    /* Abstract protected functions */
    protected abstract void storeFromJson(String storeJson);
    protected abstract String storeToJson();

    protected Context getContext(){
        return mContext;
    }

    /**
     * The strategy used for persistence
     */
    protected IPersistenceStrategy mPersistenceStrategy;
    /**
     * The current application's context
     */
    private Context mContext;

}
