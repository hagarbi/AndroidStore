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

/**
 * This interface defines the functions used to access the physical storage (files, DB, ...)
 */
public interface IPhysicalStorage {
    /**
     * This function loads the store's data from the given persistence strategy.
     */
    String load();
    /**
     * This function saves the store's data from the given persistence strategy.
     */
    void save(String data);
}
