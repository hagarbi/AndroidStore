package com.soomla.store.domain.ui;

import android.util.Log;
import com.soomla.store.StoreConfig;
import org.json.JSONException;
import org.json.JSONObject;

public class StoreTemplateElements {

    public StoreTemplateElements(StoreTitleElement mTitleElement, StoreBuyMoreElement mBuyMoreElement) {
        this.mTitleElement = mTitleElement;
        this.mBuyMoreElement = mBuyMoreElement;
    }

    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("title", mTitleElement.toJSONObject());
            jsonObject.put("buyMore", mBuyMoreElement.toJSONObject());
        } catch (JSONException e) {
            if (StoreConfig.debug){
                Log.d(TAG, "An error occured while generating JSON object.");
            }
        }

        return jsonObject;
    }

    public StoreTitleElement getTitleElement() {
        return mTitleElement;
    }

    public StoreBuyMoreElement getBuyMoreElement() {
        return mBuyMoreElement;
    }


    private static final String TAG = "SOOMLA StoreTemplateElements";

    private StoreTitleElement mTitleElement;
    private StoreBuyMoreElement mBuyMoreElement;
}
