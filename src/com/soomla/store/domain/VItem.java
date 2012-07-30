package com.soomla.store.domain;

/**
 * Created with IntelliJ IDEA.
 * User: refael
 * Date: 7/29/12
 * Time: 1:40 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class VItem {
    /* The Id of the name of this VGood in goods_strings.xml */
    private int mNameId;
    /* The Id of the description of this VGood in goods_strings.xml */
    private int mDescriptionId;
    private String mImgFilePath;
    private String mSoomlaId;

    public VItem(int mNameId, int mDescriptionId, String mImgFilePath, String mSoomlaId) {
        this.mNameId = mNameId;
        this.mDescriptionId = mDescriptionId;
        this.mImgFilePath = mImgFilePath;
        this.mSoomlaId = mSoomlaId;
    }

    public int getmNameId() {
        return mNameId;
    }

    public int getmDescriptionId() {
        return mDescriptionId;
    }

    public String getmImgFilePath() {
        return mImgFilePath;
    }

    public String getSoomlaId(){
        return mSoomlaId;
    }
}
