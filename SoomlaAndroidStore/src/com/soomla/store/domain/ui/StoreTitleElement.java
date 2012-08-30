package com.soomla.store.domain.ui;

import android.util.Log;
import com.soomla.store.StoreConfig;
import org.json.JSONException;
import org.json.JSONObject;

public class StoreTitleElement{

    public StoreTitleElement(String mName) {
        this.mName = mName;
    }

    public StoreTitleElement(JSONObject jsonObject) throws JSONException {
        this.mName = jsonObject.getString("name");
    }

    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", mName);
        } catch (JSONException e) {
            if (StoreConfig.debug){
                Log.d(TAG, "An error occured while generating JSON object.");
            }
        }

        return jsonObject;
    }

    public String getName() {
        return mName;
    }


    private static final String TAG = "SOOMLA StoreTitleElement";

    private String mName;
}