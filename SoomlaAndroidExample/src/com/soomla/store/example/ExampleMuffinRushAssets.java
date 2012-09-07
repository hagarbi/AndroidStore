package com.soomla.store.example;

import com.soomla.store.IStoreAssets;
import com.soomla.store.domain.data.*;
import com.soomla.store.domain.ui.*;

import java.util.HashMap;

public class ExampleMuffinRushAssets implements IStoreAssets {

    @Override
    public StoreTemplate getStoreTemplate(){
        return new StoreTemplate(
            "muffinRush",
            new StoreTemplateElements(
                    new StoreTitleElement("Store"),
                    new StoreBuyMoreElement("More coins", "examples/muffinRush/GetMore.png")
            ),
            new StoreTemplateProperties(3),
            false
        );
    }

    @Override
    public String getStoreBackground() {
        return "examples/muffinRush/Bg_Wall.png";
    }

    @Override
    public VirtualCurrency[] getVirtualCurrencies(){
        return  new VirtualCurrency[] {
                MUFFIN_CURRENCY
        };
    }

    @Override
    public VirtualGood[] getVirtualGoods(){
        return new VirtualGood[] {
                MUFFINCAKE_GOOD, PAVLOVA_GOOD,
                CHOCLATECAKE_GOOD, CREAMCUP_GOOD
        };
    }

    @Override
    public VirtualCurrencyPack[] getVirtualCurrencyPacks(){
        return new VirtualCurrencyPack[] {
                TENMUFF_PACK, FIFTYMUFF_PACK, FORTYMUFF_PACK, THOUSANDMUFF_PACK
        };
    }


    /** Static Final members **/

    public static final String MUFFIN_CURRENCT_ITEM_ID = "currency_muffin";

    /** Virtual Currencies **/
    public static final VirtualCurrency MUFFIN_CURRENCY = new VirtualCurrency(
            "Muffins",
            "",
            "examples/muffinRush/muffin.png",
            MUFFIN_CURRENCT_ITEM_ID
    );

    /** Virtual Goods **/
    private static final HashMap<String, Integer> MUFFINCAKE_PRICE =
            new HashMap<String, Integer>();
    static {
        MUFFINCAKE_PRICE.put(MUFFIN_CURRENCT_ITEM_ID, 225);
    }
    public static final VirtualGood MUFFINCAKE_GOOD = new VirtualGood(
            "Fruit Cake",                                   // name
            "Customers buy a double portion on each purchase of this cake", // description
            "examples/muffinRush/items/fruit_cake.png",     // image file path
            new StaticPriceModel(MUFFINCAKE_PRICE),         // currency value
            "fruit_cake"                                    // item id
    );

    private static final HashMap<String, Integer> PAVLOVA_PRICE =
            new HashMap<String, Integer>();
    static {
        PAVLOVA_PRICE.put(MUFFIN_CURRENCT_ITEM_ID, 175);
    }
    public static final VirtualGood PAVLOVA_GOOD = new VirtualGood(
            "Pavlova",                                      // name
            "Gives customers a sugar rush and they call their friends",      // description
            "examples/muffinRush/items/pavlova.png",        // image file path
            new StaticPriceModel(PAVLOVA_PRICE),            // currency value
            "pavlova"                                       // item id
    );

    private static final HashMap<String, Integer> CHOCLATECAKE_PRICE =
            new HashMap<String, Integer>();
    static {
        CHOCLATECAKE_PRICE.put(MUFFIN_CURRENCT_ITEM_ID, 250);
    }
    public static final VirtualGood CHOCLATECAKE_GOOD = new VirtualGood(
            "Chocolate Cake",                               // name
            "A classic cake to maximize customer satisfaction",  // description
            "examples/muffinRush/items/chocolate_cake.png", // image file path
            new StaticPriceModel(CHOCLATECAKE_PRICE),       // currency value
            "chocolate_cake"                                // item id
    );

    private static final HashMap<String, Integer> CREAMCUP_PRICE =
            new HashMap<String, Integer>();
    static {
        CREAMCUP_PRICE.put(MUFFIN_CURRENCT_ITEM_ID, 50);
    }
    public static final VirtualGood CREAMCUP_GOOD = new VirtualGood(
            "Cream Cup",                                    // name
            "Increase bakery reputation with this original pastry",   // description
            "examples/muffinRush/items/cream_cup.png",      // image file path
            new StaticPriceModel(CREAMCUP_PRICE),           // currency value
            "cream_cup"                                     // item id
    );

    /** Virtual Currency Packs **/

    public static final VirtualCurrencyPack TENMUFF_PACK = new VirtualCurrencyPack(
            "10 Muffins",                                   // name
            " (refund test)",                               // description
            "examples/muffinRush/currencyPacks/muffins01.png",// image file path
            "muffins_10",                                   // item id
            "android.test.refunded",                        // product id in Google Market
            0.99,                                           // actual price in $$
            10,                                             // number of currencies in the pack
            MUFFIN_CURRENCY);

    public static final VirtualCurrencyPack FIFTYMUFF_PACK = new VirtualCurrencyPack(
            "50 Muffins",                                   // name
            " (canceled test)",                             // description
            "examples/muffinRush/currencyPacks/muffins02.png",// image file path
            "muffins_50",                                   // item id
            "android.test.canceled",                        // product id in Google Market
            1.99,                                           // actual price in $$
            50,                                             // number of currencies in the pack
            MUFFIN_CURRENCY);

    public static final VirtualCurrencyPack FORTYMUFF_PACK = new VirtualCurrencyPack(
            "400 Muffins",                                  // name
            "ONLY THIS WORKS IN THIS EXAMPLE (purchase test)",// description
            "examples/muffinRush/currencyPacks/muffins03.png",// image file path
            "muffins_400",                                  // item id
            "android.test.purchased",                       // product id in Google Market
            4.99,                                           // actual price in $$
            400,                                            // number of currencies in the pack
            MUFFIN_CURRENCY);

    public static final VirtualCurrencyPack THOUSANDMUFF_PACK = new VirtualCurrencyPack(
            "1000 Muffins",                                 // name
            " (item_unavailable test)",                     // description
            "examples/muffinRush/currencyPacks/muffins04.png",// image file path
            "muffins_1000",                                 // item id
            "android.test.item_unavailable",                // product id in Google Market
            8.99,                                           // actual price in $$
            1000,                                           // number of currencies in the pack
            MUFFIN_CURRENCY);

}
