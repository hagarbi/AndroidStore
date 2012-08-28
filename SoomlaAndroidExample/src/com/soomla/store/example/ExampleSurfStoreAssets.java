package com.soomla.store.example;

import com.soomla.store.IStoreAssets;
import com.soomla.store.StoreConfig;
import com.soomla.store.domain.data.VirtualCurrency;
import com.soomla.store.domain.data.VirtualCurrencyPack;
import com.soomla.store.domain.data.VirtualGood;
import com.soomla.store.domain.ui.*;

import java.util.Arrays;
import java.util.List;

public class ExampleSurfStoreAssets implements IStoreAssets {

    @Override
    public StoreTemplate getStoreTemplate(){
        return new StoreTemplate(
            "basic",
            new StoreTemplateElements(
                    new StoreTitleElement("Surf Store"),
                    new StoreBuyMoreElement("Get more clams", "img/examples/surf/clam.png")
            ),
            new StoreTemplateProperties(3),
            false
        );
    }

    @Override
    public String getStoreBackground() {
        return "img/theme-lime-bubbles.jpg";
    }

    @Override
    public VirtualCurrency getVirtualCurrency(){
        return  new VirtualCurrency(
                "clams",
                "",
                "img/examples/surf/clam.png",
                StoreConfig.CURRENCY_ITEM_ID
        );
    }

    @Override
    public List<VirtualGood> getVirtualGoods(){
        return Arrays.asList(
                    RipCurlShortBoard, BillanbogVintageLongboard,
                    KeelFish, PicassoPeeler, ReefShredder,
                    SixtiesGun, TraditionalFish
                );
    }

    @Override
    public List<VirtualCurrencyPack> getVirtualCurrencyPacks(){
        return Arrays.asList(
                    SuperSaverPack, MalibuMediumPack, PipelinePumpinPack, SilverPack
               );
    }


    /** Private members **/

    /** Virtual Goods **/
    private final VirtualGood RipCurlShortBoard = new VirtualGood(
            "Rip Curl Shortboard",                          // name
            "Shred small waves with this super-fast board", // description
            "img/examples/surf/blue-surfboard.png",         // image file path
            100,                                            // currency value
            "blue_surfboard"                                // item id
    );

    private final VirtualGood BillanbogVintageLongboard = new VirtualGood(
            "Billanbog Vintage Longboard",                  // name
            "Slowly hang five through low power surf",      // description
            "img/examples/surf/girl-surfboard-th.png",      // image file path
            150,                                            // currency value
            "girl_surfboard_th"                             // item id
    );

    private final VirtualGood KeelFish = new VirtualGood(
            "Keel Fish",                                    // name
            "Carve slow waves with this ever-riding board", // description
            "img/examples/surf/keelfish.png",               // image file path
            200,                                            // currency value
            "keelfish"                                      // item id
    );

    private final VirtualGood PicassoPeeler = new VirtualGood(
            "Picasso Peeler",                               // name
            "The master of surreal off-the-lip maneuvers",  // description
            "img/examples/surf/picasso-peeler.png",         // image file path
            300,                                            // currency value
            "picasso_peeler"                                // item id
    );

    private final VirtualGood ReefShredder = new VirtualGood(
            "Reef Shredder",                                // name
            "A mean paddling machine for the deep reefs",   // description
            "img/examples/surf/reef-shredder.png",          // image file path
            300,                                            // currency value
            "reef_shredder"                                // item id
    );

    private final VirtualGood SixtiesGun = new VirtualGood(
            "Sixties Gun",                                  // name
            "Ride Venice-style with this time machine",     // description
            "img/examples/surf/sixties-gun.png",            // image file path
            400,                                            // currency value
            "sixties_gun"                                   // item id
    );

    private final VirtualGood TraditionalFish = new VirtualGood(
            "Traditional Fish",                             // name
            "The all around conditions board of choice",    // description
            "img/examples/surf/traditional-fish.png",       // image file path
            450,                                            // currency value
            "traditional_fish"                              // item id
    );


    /** Virtual Currency Packs **/

    private final VirtualCurrencyPack SuperSaverPack = new VirtualCurrencyPack(
            "Super Saver Pack",                             // name
            "Testing for a refund ...",                      // description
            "img/examples/surf/clam.png",                                     // image file path
            "super_saver_pack",                             // item id
            "android.test.refunded",                             // product id in Google Market
            0.99,                                           // actual price in $$
            200,                                            // number of currencies in the pack
            false                                           // consumable
    );

    private final VirtualCurrencyPack MalibuMediumPack = new VirtualCurrencyPack(
            "Malibu Medium Pack",                           // name
            "Testing for an item purchase ...",                      // description
            "img/examples/surf/clam.png",                                     // image file path
            "malibu_medium_pack",                           // item id
            "android.test.purchased",                       // product id in Google Market
            1.99,                                           // actual price in $$
            500,                                            // number of currencies in the pack
            false                                           // consumable
    );

    private VirtualCurrencyPack PipelinePumpinPack = new VirtualCurrencyPack(
            "Pipeline Pumpin' Pack",                        // name
            "Testing for purchase cacellation ...",       // description
            "img/examples/surf/clam.png",                                     // image file path
            "pipeline_pumpin_pack",                         // item id
            "android.test.canceled",                         // product id in Google Market
            5.99,                                           // actual price in $$
            1500,                                           // number of currencies in the pack
            false                                           // consumable
    );

    private VirtualCurrencyPack SilverPack = new VirtualCurrencyPack(
            "Silver Pack",                        // name
            "Testing for item unavailable ...",       // description
            "img/examples/surf/clam.png",                                     // image file path
            "pipeline_pumpin_pack",                         // item id
            "android.test.item_unavailable",                         // product id in Google Market
            15.99,                                           // actual price in $$
            5500,                                           // number of currencies in the pack
            false                                           // consumable
    );
}
