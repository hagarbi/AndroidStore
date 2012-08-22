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
     * @param mName is the name of the virtual item.
     * @param mDescription is the description of the virtual item. This will show up
     *                       in the store in the description section.
     * @param mImgFilePath is the path to the image that corresponds to the item.
     * @param mItemId is the id of the virtual item.
     */
    public VirtualItem(String mName, String mDescription, String mImgFilePath, String mItemId) {
        this.mName = mName;
        this.mDescription = mDescription;
        this.mImgFilePath = mImgFilePath;
        this.mItemId = mItemId;
    }

    /** Getters **/

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getImgFilePath() {
        return mImgFilePath;
    }

    public String getItemId(){
        return mItemId;
    }

    /** Setters **/

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public void setmImgFilePath(String mImgFilePath) {
        this.mImgFilePath = mImgFilePath;
    }

    public void setmItemId(String mItemId) {
        this.mItemId = mItemId;
    }

    /** Private members **/

    private String mName;
    private String mDescription;
    private String mImgFilePath;
    private String mItemId;
}
