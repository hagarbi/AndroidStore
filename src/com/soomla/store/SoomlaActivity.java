package com.soomla.store;

/**
 * Created with IntelliJ IDEA.
 * User: refaelos
 * Date: 7/21/12
 * Time: 2:14 PM
 * To change this template use File | Settings | File Templates.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.soomla.store.data.FilePersistenceStrategy;
import com.soomla.store.data.VStoreManager;

/**
 * USED FOR TESTING !!!
 */
public class SoomlaActivity extends Activity {

    private Context mContext;
    private VStoreManager mVStoreManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mContext = getApplicationContext();
        VStoreManager.getInstance().initialize(getApplicationContext(),
                new FilePersistenceStrategy("soomla.vcoins", getApplicationContext()),
                new FilePersistenceStrategy("soomla.vgoods", getApplicationContext()));

        Button btn = (Button) findViewById(R.id.main_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SoomlaStoreActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("layout", "layout");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
