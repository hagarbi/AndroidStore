package com.soomla.store.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

public class VCoinsStore extends AbstractVStore {
	private int     mBalance;
    private String  mImageFilePath;

    @Override
    protected String storeFilePath() {
        return "soomla.coins";
    }

    @Override
    protected void storeFromJson(String storeJson) {
        Gson gson = new Gson();
        HashMap<String, Object> json = gson.fromJson(storeJson, new TypeToken<HashMap<String, Integer>>() {}.getType());
        mBalance =       (Integer)json.get("balance");
        mImageFilePath = (String)json.get("image");
    }

    @Override
    protected String storeToJson() {
        Gson gson = new Gson();

        HashMap<String, Object> json = new HashMap<String, Object>();
        json.put("balance", mBalance);
        json.put("image", mImageFilePath);

        return gson.toJson(json);
    }
}
