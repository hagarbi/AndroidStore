package com.soomla.store.example;

import com.soomla.store.domain.ui.*;
import com.soomla.store.storefront.IStorefrontAssets;

public class SurfStoreFrontAssets implements IStorefrontAssets {

    @Override
    public StoreTemplate getStoreTemplate(){
        return new StoreTemplate(
            new StoreTemplateElements(
                    new StoreTitleElement("Surf Store"),
                    new StoreBuyMoreElement("Get more clams", "img/examples/surf/clam.png")
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
                "template", "basic"
        );
    }

    @Override
    public String getStoreBackground() {
        return "img/theme-lime-bubbles.jpg";
    }
}
