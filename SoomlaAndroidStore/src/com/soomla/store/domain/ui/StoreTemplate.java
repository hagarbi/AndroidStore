package com.soomla.store.domain.ui;

public class StoreTemplate {

    public StoreTemplate(String mName, StoreTemplateElements mElements, StoreTemplateProperties mProperties) {
        this.mName = mName;
        this.mElements = mElements;
        this.mProperties = mProperties;
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

    private String                  mName;
    private StoreTemplateElements   mElements;
    private StoreTemplateProperties mProperties;
}
