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
 * This class is the parent of all virtual items in the application.
 */
public abstract class VirtualItem {

    /** Constructor
     *
     * @param mNameId is the name of the virtual item.
     * @param mDescriptionId is the description of the virtual item. This will show up
     *                       in the store in the description section.
     * @param mImgFilePath is the path to the image that corresponds to the item.
     * @param mItemId is the id of the virtual item.
     */
    public VirtualItem(int mNameId, int mDescriptionId, String mImgFilePath, String mItemId) {
        this.mNameId = mNameId;
        this.mDescriptionId = mDescriptionId;
        this.mImgFilePath = mImgFilePath;
        this.mItemId = mItemId;
    }

    /** Getters **/

    public int getmNameId() {
        return mNameId;
    }

    public int getmDescriptionId() {
        return mDescriptionId;
    }

    public String getmImgFilePath() {
        return mImgFilePath;
    }

    public String getItemId(){
        return mItemId;
    }

    /** Private members **/

    private int mNameId;
    private int mDescriptionId;
    private String mImgFilePath;
    private String mItemId;
}
