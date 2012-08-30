package com.soomla.store.domain.ui;

import android.util.Log;
import com.soomla.store.StoreConfig;
import org.json.JSONException;
import org.json.JSONObject;

public class StoreTemplateProperties {

    public StoreTemplateProperties(int mColumns) {
        this.mColumns = mColumns;
    }

    public StoreTemplateProperties(JSONObject jsonObject) throws JSONException {
        this.mColumns = jsonObject.getInt("columns");
    }

    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("columns", new Integer(mColumns));
        } catch (JSONException e) {
            if (StoreConfig.debug){
                Log.d(TAG, "An error occured while generating JSON object.");
            }
        }

        return jsonObject;
    }

    public int getColumns() {
        return mColumns;
    }


    private static final String TAG = "SOOMLA StoreTemplateProperties";

    private int mColumns;
}