package com.soomla.store;

import android.content.Context;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: refaelos
 * Date: 7/20/12
 * Time: 7:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class Utils {
    public static void saveToFile(String storeJson, String filePath) throws IOException {
        FileOutputStream fos = Settings.APPLICATION_CONTEXT.openFileOutput(filePath, Context.MODE_PRIVATE);
        fos.write(storeJson.getBytes());
        fos.close();
    }

    public static String readFromFile(String filePath) throws IOException {

        FileInputStream fis = Settings.APPLICATION_CONTEXT.openFileInput(filePath);
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
