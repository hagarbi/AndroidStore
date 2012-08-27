Welcome to The SOOMLA Project
---
The SOOMLA Project is a series of open-source projects that aims to help game developers get better in-app purchasing stores for their games. The open-source platform-specific projects include everything a game developer needs, from storage of virtual items to purchasing mechanisms with the various devices' native stores. As part of our open-source projects you also get the store's layout which you can customize with your own game's assets.

SOOMLA-Android-Store
---
The Android store is a Java project that works seamlessly with Google Play's in-app purchasing API. SOOMLA-Android-Store is tightly coupled with Soomla-Storefront to provide you with the store's UI. All you need to do is let it know your specific game's assets and you're good to go.

Getting Started (Using source code)
---
1. Clone SOOMLA-Android-Store and its submodules (notice the '--recursive' that clones submodules). Copy all files from SoomlaAndroidStore's subfolders to their equivallent folders in your Android project (And link the jars from 'libs' folder):

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

        <activity android:name="com.soomla.store.StoreActivity" />

3. Create your own implementation of IStoreAssets in order to describe the UI template, your store's art and meta-data. Initialize StoreInfo with the class you just created:

 `StoreInfo.getInstance().initialize(new YourStoreAssetsImplementation());`

4. Decide where in your code you want to open the store and put this code there. This loads the store's activity in order to let the user purchase virtual items.:

        Intent intent = new Intent(getApplicationContext(), StoreActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("publicKey", [Your Google Play public key here]);
        intent.putExtras(bundle);
        startActivityForResult(intent, 0);

Security
---

If you want to protect your application from 'bad people' (and who doesn't?!), you might want to follow some guidelines:

+ SOOMLA keeps the game's data in an encrypted database. In order to encrypt your data, SOOMLA generates a private key out of several parts of information. StoreInfo.customSecret is one of them. SOOMLA recommends that you change this value before you release your game. BE CAREFUL: You can always change this value once! If you try to change it again, old data from the database will become unavailable.
+ Following Google's recommendation, SOOMLA also recommends that you split your public key and construct it on runtime or even use bit manipulation on it in order to hide it. The key itself is not secret information but if someone replaces it, your application might get fake messages that might harm it.

Event Handling
---

SOOMLA lets you create your own event handler and add it to StoreEventHandlers. That way you'll be able to get notifications on various events and implement your own application specific behaviour to those events.

NOTE: Your behaviour is an addition to the default behaviour implemented by SOOMLA. You don't replace SOOMLA's behaviour.

In order to create your event handler:

1. create a class that implements IStoreEventHandler.
2. Add the created class to StoreEventHandlers:

`StoreEventHandlers.getInstance().addEventHandler(new YourEventHandler());`

Contribution
---

We want you!

Fork -> Clone -> implement -> Test -> Pull-Request. We have great respect for contribution.

License
---
MIT License. Copyright (c) 2012 SOOMLA. http://project.soom.la
+ http://www.opensource.org/licenses/MIT
