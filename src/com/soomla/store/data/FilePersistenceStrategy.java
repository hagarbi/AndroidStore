package com.soomla.store.data;

import android.content.Context;
import com.soomla.store.Settings;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: refael
 * Date: 7/30/12
 * Time: 5:05 PM
 * To change this template use File | Settings | File Templates.
 */

// TODO: data encryption !!
public class FilePersistenceStrategy implements IPersistenceStrategy {
    private String  mFilePath;
    private Context mContext;

    public FilePersistenceStrategy(String mFilePath, Context mContext) {
        this.mFilePath = mFilePath;
        this.mContext =  mContext;
    }

    @Override
    public String fetch() throws IOException {
        return readFromFile(mFilePath);
    }

    @Override
    public void persist(String data) throws IOException {
        saveToFile(data, mFilePath);
    }

    public void saveToFile(String storeJson, String filePath) throws IOException {
        FileOutputStream fos = mContext.openFileOutput(filePath, Context.MODE_PRIVATE);
        fos.write(storeJson.getBytes());
        fos.close();
    }

    public String readFromFile(String filePath) throws IOException {

        FileInputStream fis = mContext.openFileInput(filePath);
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
