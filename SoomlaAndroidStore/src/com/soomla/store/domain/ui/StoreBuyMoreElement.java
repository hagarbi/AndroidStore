package com.soomla.store.domain.ui;

public class StoreBuyMoreElement{

    public StoreBuyMoreElement(String mText, String imagePath) {
        this.mText = mText;
        this.mImgFilePath = imagePath;
    }

    public String getText() {
        return mText;
    }

    public String getImgFilePath() {
        return mImgFilePath;
    }

    private String mText;
    private String mImgFilePath;
}