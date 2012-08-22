package com.soomla.store.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StoreDatabase {

    public StoreDatabase(Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
        mStoreDB = mDatabaseHelper.getWritableDatabase();
    }

    public void close() {
        mDatabaseHelper.close();
    }

    public void addOrUpdatePurchaseHistory(String orderId, String productId, String itemId,
                                      String purchaseState, String purchaseTime,
                                      String developerPayload, String currentBalance){
        ContentValues values = new ContentValues();
        values.put(PURCHASE_HISTORY_COLUMN_ORDER_ID, orderId);
        values.put(PURCHASE_HISTORY_COLUMN_PRODUCT_ID, productId);
        values.put(PURCHASE_HISTORY_COLUMN_ITEM_ID, itemId);
        values.put(PURCHASE_HISTORY_COLUMN_STATE, purchaseState);
        values.put(PURCHASE_HISTORY_COLUMN_TIME, purchaseTime);
        values.put(PURCHASE_HISTORY_COLUMN_DEVPAYLOAD, developerPayload);
        values.put(PURCHASE_HISTORY_COLUMN_BALANCE, currentBalance);
        mStoreDB.replace(PURCHASE_HISTORY_TABLE_NAME, null /* nullColumnHack */, values);
    }

    public Cursor getPurchaseHistory(String orderId){
        return mStoreDB.query(PURCHASE_HISTORY_TABLE_NAME, PURCHASE_HISTORY_COLUMNS,
                PURCHASE_HISTORY_COLUMN_ORDER_ID + " = '" + orderId + "'", null, null, null, null);
    }

    public void updateVirtualCurrency(String itemId, String quantity){
        ContentValues values = new ContentValues();
        values.put(VIRTUAL_CURRENCY_COLUMN_ITEM_ID, itemId);
        values.put(VIRTUAL_CURRENCY_COLUMN_BALANCE, quantity);
        mStoreDB.replace(VIRTUAL_CURRENCY_TABLE_NAME, null, values);
    }

    public Cursor getVirtualCurrencies(){
        return mStoreDB.query(VIRTUAL_CURRENCY_TABLE_NAME, VIRTUAL_CURRENCY_COLUMNS,
                null, null, null, null, null);
    }

    public Cursor getVirtualCurrency(String itemId){
        return mStoreDB.query(VIRTUAL_CURRENCY_TABLE_NAME, VIRTUAL_CURRENCY_COLUMNS,
                VIRTUAL_CURRENCY_COLUMN_ITEM_ID +  " = '" + itemId + "'", null, null, null, null);
    }

    public void updateVirtualGood(String itemId, String quantity){
        ContentValues values = new ContentValues();
        values.put(VIRTUAL_GOODS_COLUMN_ITEM_ID, itemId);
        values.put(VIRTUAL_GOODS_COLUMN_BALANCE, quantity);
        mStoreDB.replace(VIRTUAL_GOODS_TABLE_NAME, null, values);
    }

    public Cursor getVirtualGoods(){
        return mStoreDB.query(VIRTUAL_GOODS_TABLE_NAME, VIRTUAL_GOODS_COLUMNS,
                null, null, null, null, null);
    }

    public Cursor getVirtualGood(String itemId){
        return mStoreDB.query(VIRTUAL_GOODS_TABLE_NAME, VIRTUAL_GOODS_COLUMNS,
                VIRTUAL_GOODS_COLUMN_ITEM_ID + "='" + itemId + "'", null, null, null, null);
    }

    private class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            if (!sqLiteDatabase.isReadOnly()){
                sqLiteDatabase.execSQL("PRAGMA foreign_key=ON");
            }

            createPurchaseTable(sqLiteDatabase);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            // TODO: implement this !!
        }

        private void createPurchaseTable(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("CREATE TABLE " + PURCHASE_HISTORY_TABLE_NAME + "(" +
                        PURCHASE_HISTORY_COLUMN_ORDER_ID + " TEXT PRIMARY KEY, " +
                        PURCHASE_HISTORY_COLUMN_PRODUCT_ID + " TEXT, " +
                        PURCHASE_HISTORY_COLUMN_ITEM_ID + " TEXT, " +
                        PURCHASE_HISTORY_COLUMN_STATE + " TEXT, " +
                        PURCHASE_HISTORY_COLUMN_TIME + " TEXT, " +
                        PURCHASE_HISTORY_COLUMN_DEVPAYLOAD + " TEXT, " +
                        PURCHASE_HISTORY_COLUMN_BALANCE + " TEXT)");
            sqLiteDatabase.execSQL("CREATE TABLE " + VIRTUAL_CURRENCY_TABLE_NAME + "(" +
                        VIRTUAL_CURRENCY_COLUMN_ITEM_ID + " TEXT PRIMARY KEY, " +
                        VIRTUAL_CURRENCY_COLUMN_BALANCE + " TEXT)");
            sqLiteDatabase.execSQL("CREATE TABLE " + VIRTUAL_GOODS_TABLE_NAME + "(" +
                        VIRTUAL_GOODS_COLUMN_ITEM_ID + " TEXT PRIMARY KEY, " +
                        VIRTUAL_GOODS_COLUMN_BALANCE + " TEXT)");
        }
    }

    private static final String TAG = "StoreDatabase";

    private static final String DATABASE_NAME               = "store.db";
    private static final int    DATABASE_VERSION            = 1;

    // Virtual Currency Table
    private static final String VIRTUAL_CURRENCY_TABLE_NAME     = "virtual_currency";
    public static final String VIRTUAL_CURRENCY_COLUMN_BALANCE  = "balance";
    public static final String VIRTUAL_CURRENCY_COLUMN_ITEM_ID  = "item_id";
    private static final String[] VIRTUAL_CURRENCY_COLUMNS = {
            VIRTUAL_CURRENCY_COLUMN_ITEM_ID, VIRTUAL_CURRENCY_COLUMN_BALANCE
    };

    // Virtual Goods Table
    private static final String VIRTUAL_GOODS_TABLE_NAME        = "virtual_goods";
    public static final String VIRTUAL_GOODS_COLUMN_BALANCE     = "balance";
    public static final String VIRTUAL_GOODS_COLUMN_ITEM_ID     = "item_id";
    private static final String[] VIRTUAL_GOODS_COLUMNS = {
            VIRTUAL_GOODS_COLUMN_ITEM_ID, VIRTUAL_GOODS_COLUMN_BALANCE
    };

    // Purchase History Table
    private static final String PURCHASE_HISTORY_TABLE_NAME       = "purchase_history";
    public static final String PURCHASE_HISTORY_COLUMN_STATE      = "purchase_state";
    public static final String PURCHASE_HISTORY_COLUMN_PRODUCT_ID = "product_id";
    public static final String PURCHASE_HISTORY_COLUMN_ORDER_ID   = "order_id";
    public static final String PURCHASE_HISTORY_COLUMN_ITEM_ID    = "item_id";
    public static final String PURCHASE_HISTORY_COLUMN_TIME       = "purchase_time";
    public static final String PURCHASE_HISTORY_COLUMN_DEVPAYLOAD = "developer_payload";
    public static final String PURCHASE_HISTORY_COLUMN_BALANCE    = "current_balance";
    private static final String[] PURCHASE_HISTORY_COLUMNS = {
            PURCHASE_HISTORY_COLUMN_ORDER_ID, PURCHASE_HISTORY_COLUMN_PRODUCT_ID, PURCHASE_HISTORY_COLUMN_ITEM_ID,
            PURCHASE_HISTORY_COLUMN_STATE, PURCHASE_HISTORY_COLUMN_TIME, PURCHASE_HISTORY_COLUMN_DEVPAYLOAD,
            PURCHASE_HISTORY_COLUMN_BALANCE
    };

    private SQLiteDatabase mStoreDB;
    private DatabaseHelper mDatabaseHelper;
}
