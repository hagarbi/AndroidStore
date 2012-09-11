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
package com.soomla.store.storefront;

import com.soomla.store.domain.ui.StoreTemplate;
import com.soomla.store.domain.ui.StoreTheme;

/**
 * This interface allows you to add all information relevant to the Storefront.
 * The information provided here will be used when initializing StorefrontInfo.
 */
public interface IStorefrontAssets {
    /**
     * The template that you chose to use for your store's.
     * @return your chosen template.
     */
    StoreTemplate getStoreTemplate();

    /**
     * The theme for the selected template
     * @return your chosen theme.
     */
    StoreTheme getStoreTheme();

    /**
     * A path to the image that you want to use as your store background.
     * @return a path to an image file.
     */
    String getStoreBackground();
}
