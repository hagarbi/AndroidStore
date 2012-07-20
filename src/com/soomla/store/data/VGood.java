package com.soomla.store.data;

public class VGood {
	private String mName;
	private String mDescription;
    private String mImgFilePath;

    public VGood(String mName, String mDescription, String mImgFilePath) {
        this.mName = mName;
        this.mDescription = mDescription;
        this.mImgFilePath = mImgFilePath;
    }

    public String getName() {
		return mName;
	}
	
	public String getDescription() {
		return mDescription;
	}

    public String getImageFilePath() {
        return mImgFilePath;
    }
}
