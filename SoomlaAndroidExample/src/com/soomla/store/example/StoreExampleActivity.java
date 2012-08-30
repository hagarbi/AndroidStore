package com.soomla.store.example;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.soomla.store.StoreActivity;
import com.soomla.store.StoreEventHandlers;
import com.soomla.store.data.StorageManager;

public class StoreExampleActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mRobotView = (ImageView) findViewById(R.id.drag_img);
        mRobotView.setOnTouchListener(new MyTouchListener());
        findViewById(R.id.topright).setOnDragListener(new MyDragListener());

        Typeface font = Typeface.createFromAsset(getAssets(), "GoodDog.otf");
        ((TextView) findViewById(R.id.title_text)).setTypeface(font);
        ((TextView) findViewById(R.id.main_text)).setTypeface(font);

        /**
         * We initialize StorageManager and add event handler to StoreEventHandlers before
         * we open the store.
         */

        StorageManager.getInstance().initialize(getApplicationContext(),
                new ExampleSurfStoreAssets());
        StoreEventHandlers.getInstance().addEventHandler(
                new ExampleEventHandler(getApplicationContext(), this));

    }

    private Handler   mHandler = new Handler();
    private ImageView mRobotView;
    public void robotBackHome(){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup left = (ViewGroup)findViewById(R.id.topleft);
                ViewGroup right = (ViewGroup)findViewById(R.id.topright);

                if (mRobotView.getParent() != left){
                    right.removeView(mRobotView);
                    left.addView(mRobotView);
                }
            }
        });
    }

    private final class MyTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }

    private final class MyDragListener implements View.OnDragListener {
        Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
        Drawable normalShape = getResources().getDrawable(R.drawable.shape);

        @Override
        public boolean onDrag(View v, DragEvent event) {
            View view = (View) event.getLocalState();
//            ViewGroup owner = (ViewGroup) view.getParent();
//            LinearLayout container = (LinearLayout) v;
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundDrawable(enterShape);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundDrawable(normalShape);
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup

                    ViewGroup left = (ViewGroup)findViewById(R.id.topleft);
                    ViewGroup right = (ViewGroup)findViewById(R.id.topright);

                    if (right == v){
                        left.removeView(view);
                        right.addView(view);
                        view.setVisibility(View.VISIBLE);

                        openStore();
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    view.setVisibility(View.VISIBLE);

                    v.setBackgroundDrawable(normalShape);
                default:
                    break;
            }
            return true;
        }
    }

    private void openStore() {
        Intent intent = new Intent(getApplicationContext(), StoreActivity.class);
        Bundle bundle = new Bundle();

        /**
         * print debug messages.
         */
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
}
