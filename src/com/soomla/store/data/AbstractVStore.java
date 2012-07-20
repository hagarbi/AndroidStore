package com.soomla.store.data;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.soomla.store.Settings;
import com.soomla.store.Utils;

import java.io.*;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: refaelos
 * Date: 7/20/12
 * Time: 7:06 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractVStore {

    protected abstract String storeFilePath();

    protected abstract void storeFromJson(String storeJson);

    protected abstract String storeToJson();

    public void load() throws IOException {
        String storeJson = Utils.readFromFile(storeFilePath());
        storeFromJson(storeJson);
    }

    public void save() throws IOException {
        Utils.saveToFile(storeToJson(), storeFilePath());
    }
}
