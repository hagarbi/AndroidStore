package com.soomla.store.domain;

/**
 * Created with IntelliJ IDEA.
 * User: refael
 * Date: 7/29/12
 * Time: 1:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class GoogleMarketItem {
    /**
     * Each product in the catalog can be MANAGED, UNMANAGED, or SUBSCRIPTION.  MANAGED
     * means that the product can be purchased only once per user (such as a new
     * level in a game). The purchase is remembered by Android Market and
     * can be restored if this application is uninstalled and then
     * re-installed. UNMANAGED is used for products that can be used up and
     * purchased multiple times (such as poker chips). It is up to the
     * application to keep track of UNMANAGED products for the user.
     * SUBSCRIPTION is just like MANAGED except that the user gets charged monthly
     * or yearly.
     */
    public static enum Managed { MANAGED, UNMANAGED, SUBSCRIPTION }

    /* The Id of this VGood in Google Market */
    private String mMarketId;
    private Managed mManaged;

    public GoogleMarketItem(String mMarketId, Managed mManaged) {

        this.mMarketId = mMarketId;
        this.mManaged = mManaged;
    }

    public String getMarketId() {
        return mMarketId;
    }

    public Managed getManaged() {
        return mManaged;
    }
}
