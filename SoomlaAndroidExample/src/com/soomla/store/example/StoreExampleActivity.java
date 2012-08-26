package com.soomla.store.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import com.soomla.store.StoreActivity;
import com.soomla.store.StoreEventHandlers;
import com.soomla.store.StoreInfo;

public class StoreExampleActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        StoreInfo.getInstance().initialize(new ExampleSurfStoreAssets());
        StoreEventHandlers.getInstance().addEventHandler(
                new ExampleEventHandler(getApplicationContext()));

        final ImageButton button = (ImageButton) findViewById(R.id.main_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StoreActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("debug", true);

                /**
                 * Compute your public key (that you got from the Android Market publisher site).
                 *
                 * Instead of just storing the entire literal string here embedded in the
                 * program,  construct the key at runtime from pieces or
                 * use bit manipulation (for example, XOR with some other string) to hide
                 * the actual key.  The key itself is not secret information, but we don't
                 * want to make it easy for an adversary to replace the public key with one
                 * of their own and then fake messages from the server.
                 *
                 * Generally, encryption keys / passwords should only be kept in memory
                 * long enough to perform the operation they need to perform.
                 */
                bundle.putString("publicKey", "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAndHbBVrbynZ9LOQhRCA/+dzYyQeT7qcbo6BD16O+7ltau6JLy78emOo4615+N3dl5RJ3FBlRw14aS+KhNAf0gMlrk3RBQA5d+sY/8oD22kC8Gn7blwsmk3LWYqOiGGXFtRxUyBxdibjFo0+qBz+BXJzfKYV+Y3wSDz0RBUoY9+akbF3EHuB6d02fXLeeIAswB28OlAM4PUuHSbj9lDNFefJwawQ7kgUALETJ98ImKlPUzG0jVh1t9vUOarsIZdzWmVu69+Au3mniqzcGY9gZyfYf0n7cNR3isSDfNOjeisDpfNpY/ljf71/6ns3/WjDwtXB2eDal5fz7fbsLEWRkSwIDAQAB");

                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            }
        });
    }
}
