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
import android.util.Log;

import java.io.*;

/**
 * This class is an implementation of {@link IPhysicalStorage} that supports saving data to files.
 */
public class FileStorage implements IPhysicalStorage {

    /* Public functions */

    /**
     * This function loads the store's data from the given persistence strategy.
     */
    @Override
    public String load() {
        try {
            return readFromFile();
        } catch (IOException e) {
            Log.e(TAG, "error reading from file", e);
        }

        return "";
    }

    /**
     * This function saves the store's data from the given persistence strategy.
     */
    @Override
    public void save(String data) {
        try {
            saveToFile(data);
        } catch (IOException e) {
            Log.e(TAG, "error saving to file", e);
        }
    }

    /** Constructor
     *
     * @param mContext is the current application's context
     * @param mFilePath is the path of the file to save data to.
     */
    public FileStorage(Context mContext, String mFilePath) {
        this.mContext =  mContext;
        this.mFilePath = mFilePath;
    }

    private static final String TAG = "SOOMLA FileStorage";
    /**
     * The current application's context.
     */
    private Context mContext;

    /**
     * The storage file.
     */
    private String  mFilePath;

    /**
     * Saving the given data to the given file path.
     * @param data is the actual data to be saved to the file. Usually it's the storage data.
     * @throws IOException
     */
    private void saveToFile(String data) throws IOException {
        FileOutputStream fos = mContext.openFileOutput(mFilePath, Context.MODE_PRIVATE);
        fos.write(data.getBytes());
        fos.close();
    }

    /**
     * Reading all data from the given file path.
     * @return the data from the file as String.
     * @throws IOException
     */
    private String readFromFile() throws IOException {

        FileInputStream fis = mContext.openFileInput(mFilePath);
        InputStreamReader inputStreamReader = new InputStreamReader(fis);
        StringBuilder fileContent = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            fileContent.append(line);
        }
        fis.close();

        return new String(fileContent);
    }

}
