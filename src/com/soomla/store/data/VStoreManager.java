package com.soomla.store.data;

import android.content.Context;

/**
 * Created with IntelliJ IDEA.
 * User: refael
 * Date: 7/31/12
 * Time: 9:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class VStoreManager {

    public static VStoreManager getInstance(){
        if (mInstance == null){
            mInstance = new VStoreManager();
        }

        return mInstance;
    }

    public void initialize(Context context, IPersistenceStrategy vCoinsStrategy, IPersistenceStrategy vGoodsStrategy){
        mVCoinsStore = new VCoinsStore(context, vCoinsStrategy);
        mVGoodsStore = new VGoodsStore(context, vGoodsStrategy);
    }

    public VCoinsStore getVCoinsStore(){
        return mVCoinsStore;
    }

    public VGoodsStore getVGoodsStore(){
        return mVGoodsStore;
    }

    private VStoreManager(){ }

    private VGoodsStore mVGoodsStore;
    private VCoinsStore mVCoinsStore;
    private static VStoreManager mInstance;
}
