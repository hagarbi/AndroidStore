package com.soomla.store.domain.ui;

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

    private String                  mName;
    private StoreTemplateElements   mElements;
    private StoreTemplateProperties mProperties;
    private boolean                 mOrientationLandscape;
}
