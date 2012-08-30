package com.soomla.store.domain.ui;

import android.util.Log;
import com.soomla.store.StoreConfig;
import org.json.JSONException;
import org.json.JSONObject;

public class StoreBuyMoreElement{

    public StoreBuyMoreElement(String mText, String imagePath) {
        this.mText = mText;
        this.mImgFilePath = imagePath;
    }

    public StoreBuyMoreElement(JSONObject jsonObject) throws JSONException {
        this.mText = jsonObject.getString("text");
        this.mImgFilePath = jsonObject.getString("imgFilePath");
    }

    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("text", mText);
            jsonObject.put("imgFilePath", mImgFilePath);
        } catch (JSONException e) {
            if (StoreConfig.debug){
                Log.d(TAG, "An error occured while generating JSON object.");
            }
        }

        return jsonObject;
    }

    public String getText() {
        return mText;
    }

    public String getImgFilePath() {
        return mImgFilePath;
    }


    private static final String TAG = "SOOMLA StoreBuyMoreElement";

    private String mText;
    private String mImgFilePath;
}