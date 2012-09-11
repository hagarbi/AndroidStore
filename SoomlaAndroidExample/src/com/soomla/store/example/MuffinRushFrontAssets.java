package com.soomla.store.example;

import com.soomla.store.domain.ui.*;
import com.soomla.store.storefront.IStorefrontAssets;

public class MuffinRushFrontAssets implements IStorefrontAssets {

    @Override
    public StoreTemplate getStoreTemplate(){
        return new StoreTemplate(
            new StoreTemplateElements(
                    new StoreTitleElement("Store"),
                    new StoreBuyMoreElement("More coins", "themes/muffinRush/img/GetMore.png")
            ),
            new StoreTemplateProperties(3),
            false
        );
    }

    @Override
    public StoreTheme getStoreTheme() {
        return new StoreTheme(
                new StoreView("Components.CollectionListView", new StoreViewItem("Components.ListItemView", "item")),
                new StoreView("Components.CollectionListView", new StoreViewItem("Components.ListItemView", "currencyPack")),
                new StoreModalDialog("modalDialog"),
                "template", "muffinRush"
        );
    }

    @Override
    public String getStoreBackground() {
        return "themes/muffinRush/img/Bg_Wall.png";
    }
}
