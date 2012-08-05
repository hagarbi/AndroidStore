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
package com.soomla.store.domain;

/**
 * This is a representation of the game's virtual currency.
 * Each game usually has one instance of this class globally
 */
public class VirtualCurrency extends VirtualItem {

    /** Constructor
     *
     * @param mNameId is the name of the virtual currency. For example: "Coin", "Clam", "Gem" ...
     * @param mDescriptionId is the description of the virtual currency. This will show up
     *                       in the store in the description section.
     * @param mImgFilePath is the path to the image that corresponds to the currency.
     * @param itemId is the id of the virtual currency.
     */
    public VirtualCurrency(int mNameId, int mDescriptionId, String mImgFilePath, String itemId) {
        super(mNameId, mDescriptionId, mImgFilePath, itemId);
    }
}
