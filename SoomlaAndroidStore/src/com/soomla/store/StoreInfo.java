package com.soomla.store;

import android.content.Context;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soomla.store.domain.GoogleMarketItem;
import com.soomla.store.domain.VirtualCurrency;
import com.soomla.store.domain.VirtualCurrencyPack;
import com.soomla.store.domain.VirtualGood;
import com.soomla.store.exceptions.VirtualItemNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * This class also holds and serves the pointer to the single {@link com.soomla.store.domain.VirtualCurrency}
 * in the entire application.
 */
public class StoreInfo {

    public static StoreInfo getInstance(){
        if (sInstance == null){
            sInstance = new StoreInfo();
        }

        return sInstance;
    }

    /**
     * This function parses store_def.json in order to load necessary
     * store info values.
     * @param context is the application context.
     */
    public void initialize(Context context){
        InputStream is = null;
        byte[] buffer = null;
        try {
            is = context.getAssets().open("store_def.json");
            int size = is.available();
            buffer = new byte[size];
            is.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (is != null){
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mJsonString = new String(buffer);

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readValue(mJsonString, JsonNode.class);
//            JsonNode templateNode =      rootNode.path("template");
//            JsonNode backgroundNode =    rootNode.path("background");

            JsonNode currencyNode =      rootNode.path("currency");
            mVirtualCurrency = new VirtualCurrency(currencyNode.path("name").textValue(),
                    "", currencyNode.path("image").textValue(), "");

            JsonNode virtualGoodsNode =  rootNode.path("virtualGoods");
            mVirtualGoodOptions = new HashMap<String, VirtualGood>();
            for (final JsonNode jsonNode : virtualGoodsNode){
                VirtualGood good = new VirtualGood(jsonNode.path("name").textValue(),
                        jsonNode.path("description").textValue(),
                        jsonNode.path("src").textValue(),
                        jsonNode.path("price").intValue(),
                        jsonNode.path("itemId").textValue());
                mVirtualGoodOptions.put(jsonNode.path("itemId").textValue(), good);
            }

            JsonNode currencyPacksNode = rootNode.path("currencyPacks");
            mPacksOptions = new HashMap<String, VirtualCurrencyPack>();
            for (final JsonNode jsonNode : currencyPacksNode){
                GoogleMarketItem.Managed managed = jsonNode.path("managed").booleanValue() ?
                        GoogleMarketItem.Managed.MANAGED :
                        GoogleMarketItem.Managed.UNMANAGED;
                GoogleMarketItem gItem = new GoogleMarketItem(
                        jsonNode.path("productId").textValue(),
                        managed);
                VirtualCurrencyPack pack = new VirtualCurrencyPack(jsonNode.path("name").textValue(),
                        jsonNode.path("description").textValue(),
                        jsonNode.path("image").textValue(),
                        jsonNode.path("itemId").textValue(),
                        gItem,
                        jsonNode.path("price").doubleValue(),
                        jsonNode.path("amount").intValue());

                mPacksOptions.put(jsonNode.path("productId").textValue(), pack);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Use this function if you need to know the definition of the virtual currency used in this game.
     * @return the definition of a virtual currency in the current game;
     */
    public VirtualCurrency getVirtualCurrency(){
        return mVirtualCurrency;
    }

    /**
     * Use this function if you need to know the definition of a specific virtual currency pack.
     * @param productId is the requested pack's product id.
     * @return the definition of the virtual pack requested.
     * @throws VirtualItemNotFoundException
     */
    public VirtualCurrencyPack getPackByGoogleProductId(String productId) throws VirtualItemNotFoundException {{
        if (!mPacksOptions.containsKey(productId)){
            throw new VirtualItemNotFoundException("productId", productId);
        }
    }
        return mPacksOptions.get(productId);
    }

    /**
     * Use this function if you need to know the definition of a specific virtual good.
     * @param itemId is the requested good's item id.
     * @return the definition of the virtual good requested.
     * @throws VirtualItemNotFoundException
     */
    public VirtualGood getVirtualGoodBySoomlaId(String itemId) throws VirtualItemNotFoundException {
        if (!mVirtualGoodOptions.containsKey(itemId)){
            throw new VirtualItemNotFoundException("itemId", itemId);
        }

        return mVirtualGoodOptions.get(itemId);
    }

    /**
     * Use this function to get the json string (store_def.json)
     * @return the json that's kept in store_def.json.
     */
    public String getJsonString(){
        return mJsonString;
    }

    private StoreInfo() { }


    /** Private members **/

    private static StoreInfo                        sInstance = null;
    private String                                  mJsonString;
    private VirtualCurrency                         mVirtualCurrency;
    private HashMap<String, VirtualCurrencyPack>    mPacksOptions;
    private HashMap<String, VirtualGood>            mVirtualGoodOptions;
}