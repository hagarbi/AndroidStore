package com.soomla.store.domain.ui;

import android.util.Log;
import com.soomla.store.StoreConfig;
import org.json.JSONException;
import org.json.JSONObject;

public class StoreTemplate {

    public StoreTemplate(String mName,
                         StoreTemplateElements mElements,
                         StoreTemplateProperties mProperties,
                         boolean orientationLandscape) {
        this.mName = mName;
        this.mElements = mElements;
        this.mProperties = mProperties;
        this.mOrientationLandscape = orientationLandscape;
    }

    public StoreTemplate(JSONObject jsonObject){
        try {
            mName = jsonObject.getString("name");
            mElements = new StoreTemplateElements(jsonObject.getJSONObject("elements"));
            mProperties = new StoreTemplateProperties(jsonObject.getJSONObject("properties"));
            mOrientationLandscape = jsonObject.getBoolean("orientationLandscape");
        } catch (JSONException e) {
            if (StoreConfig.debug){
                Log.d(TAG, "An error occured while parsing JSON object.");
            }
        }
    }

    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", mName);
            jsonObject.put("elements", mElements.toJSONObject());
            jsonObject.put("properties", mProperties.toJSONObject());
            jsonObject.put("orientationLandscape", mOrientationLandscape);
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

    public StoreTemplateElements getElements() {
        return mElements;
    }

    public StoreTemplateProperties getProperties() {
        return mProperties;
    }

    public boolean isOrientationLandscape() {
        return mOrientationLandscape;
    }


    private static final String TAG = "SOOMLA StoreTemplate";

    private String                  mName;
    private StoreTemplateElements   mElements;
    private StoreTemplateProperties mProperties;
    private boolean                 mOrientationLandscape;
}
