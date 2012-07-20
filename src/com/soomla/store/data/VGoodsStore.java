package com.soomla.store.data;

import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class VGoodsStore extends AbstractVStore {
	private HashMap<String, Integer> mStore;

    public VGoodsStore() {
        this.mStore = new HashMap<String, Integer>();
    }

    public int getBalance(VGood vgood){
		return mStore.get(vgood.getName());
	}

    public void add(VGood vgood, int amount){
		if (!mStore.containsKey(vgood.getName())){
			mStore.put(vgood.getName(), 0);
		}
		
		mStore.put(vgood.getName(), mStore.get(vgood.getName()) + amount);
	}

    public void remove(VGood vgood, int amount){
		if (!mStore.containsKey(vgood.getName())){
			return;
		}
		
		int balance = mStore.get(vgood.getName()) - amount;
		mStore.put(vgood.getName(), balance > 0 ? balance : 0);
	}

    @Override
    protected String storeFilePath() {
        return "soomla.store";
    }

    @Override
    protected void storeFromJson(String storeJson) {
        Gson gson = new Gson();
        mStore = gson.fromJson(storeJson, new TypeToken<HashMap<String, Integer>>() {}.getType());
    }

    @Override
    protected String storeToJson() {
        Gson gson = new Gson();
        return gson.toJson(mStore);
    }

}
