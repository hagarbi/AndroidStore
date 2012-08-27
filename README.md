Welcome to The SOOMLA Project
---
The SOOMLA Project is a series of open-source projects that aims to help game developers get better in-app purchasing stores for their games. The open-source platform-specific projects include everything a game developer needs, from storage of virtual items to purchasing mechanisms with the various devices' native stores. As part of our open-source projects you also get the store's layout which you can customize with your own game's assets.

SOOMLA-Android-Store
---
The Android store is a Java project that works seamlessly with Google Play's in-app purchasing API. SOOMLA-Android-Store is tightly coupled with Soomla-Storefront to provide you with the store's UI. All you need to do is let it know your specific game's assets and you're good to go.

Getting Started
---
1. Clone SOOMLA-Android-Store and its submodules. Copy all files from SoomlaAndroidStore's subfolders to their equivallent folders in your Android project (And link the jars from 'libs' folder):

 `git clone --recursive git@github.com:refaelos/Soomla-Android-Store.git`

2. Make the folowing changes to your AndroidManifest.xml:

  Add the following permission:

 `<uses-permission android:name="com.android.vending.BILLING" />`

  Add the following code to your 'application' element:

        <service android:name="com.soomla.billing.BillingService" />

        <receiver android:name="com.soomla.billing.BillingReceiver">
            <intent-filter>
                <action android:name="com.android.vending.billing.IN_APP_NOTIFY" />
                <action android:name="com.android.vending.billing.RESPONSE_CODE" />
                <action android:name="com.android.vending.billing.PURCHASE_STATE_CHANGED" />
            </intent-filter>
        </receiver>

        <activity
                android:name="com.soomla.store.StoreActivity" />
