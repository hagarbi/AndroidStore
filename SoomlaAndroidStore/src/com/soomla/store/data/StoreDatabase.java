/*
 * Copyright (C) 2012 Soomla Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.soomla.store.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * The StoreDatabase provides basic SQLite database io functions for specific needs around the SDK.
 */
public class StoreDatabase {

    public StoreDatabase(Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
        mStoreDB = mDatabaseHelper.getWritableDatabase();
    }

    /**
     * Closes the database.
     */
    public void close() {
        mDatabaseHelper.close();
    }

    public synchronized void updateVirtualCurrency(String itemId, String quantity){
        ContentValues values = new ContentValues();
        values.put(VIRTUAL_CURRENCY_COLUMN_ITEM_ID, itemId);
        values.put(VIRTUAL_CURRENCY_COLUMN_BALANCE, quantity);
        mStoreDB.replace(VIRTUAL_CURRENCY_TABLE_NAME, null, values);
    }

    public synchronized Cursor getVirtualCurrencies(){
        return mStoreDB.query(VIRTUAL_CURRENCY_TABLE_NAME, VIRTUAL_CURRENCY_COLUMNS,
                null, null, null, null, null);
    }

    public synchronized Cursor getVirtualCurrency(String itemId){
        return mStoreDB.query(VIRTUAL_CURRENCY_TABLE_NAME, VIRTUAL_CURRENCY_COLUMNS,
                VIRTUAL_CURRENCY_COLUMN_ITEM_ID +  " = '" + itemId + "'", null, null, null, null);
    }

    public synchronized void updateVirtualGood(String itemId, String quantity){
        ContentValues values = new ContentValues();
        values.put(VIRTUAL_GOODS_COLUMN_ITEM_ID, itemId);
        values.put(VIRTUAL_GOODS_COLUMN_BALANCE, quantity);
        mStoreDB.replace(VIRTUAL_GOODS_TABLE_NAME, null, values);
    }

    public synchronized Cursor getVirtualGoods(){
        return mStoreDB.query(VIRTUAL_GOODS_TABLE_NAME, VIRTUAL_GOODS_COLUMNS,
                null, null, null, null, null);
    }

    public synchronized Cursor getVirtualGood(String itemId){
        return mStoreDB.query(VIRTUAL_GOODS_TABLE_NAME, VIRTUAL_GOODS_COLUMNS,
                VIRTUAL_GOODS_COLUMN_ITEM_ID + "='" + itemId + "'", null, null, null, null);
    }

    public synchronized void setMetaData(String metadata){
        ContentValues values = new ContentValues();
        values.put(METADATA_COLUMN_VALUE, metadata);
        mStoreDB.replace(METADATA_TABLE_NAME, null, values);
    }

    public synchronized Cursor getMetaData(){
        return mStoreDB.query(METADATA_TABLE_NAME, METADATA_COLUMNS,
                null, null, null, null, null);
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

            sqLiteDatabase.execSQL("drop table " + METADATA_TABLE_NAME);

            createPurchaseTable(sqLiteDatabase);
        }

        private void createPurchaseTable(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + VIRTUAL_CURRENCY_TABLE_NAME + "(" +
                    VIRTUAL_CURRENCY_COLUMN_ITEM_ID + " TEXT PRIMARY KEY, " +
                    VIRTUAL_CURRENCY_COLUMN_BALANCE + " TEXT)");

            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + VIRTUAL_GOODS_TABLE_NAME + "(" +
                    VIRTUAL_GOODS_COLUMN_ITEM_ID + " TEXT PRIMARY KEY, " +
                    VIRTUAL_GOODS_COLUMN_BALANCE + " TEXT)");

            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + METADATA_TABLE_NAME + "(" +
                    METADATA_COLUMN_VALUE + " TEXT PRIMARY KEY)");
        }
    }

    private static final String TAG = "StoreDatabase";

    private static final String DATABASE_NAME               = "store.db";
    private static final int    DATABASE_VERSION            = 2;

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

    // Store Meta-Data Table
    private static final String METADATA_TABLE_NAME       = "metadata";
    public static final String METADATA_COLUMN_VALUE      = "value";
    private static final String[] METADATA_COLUMNS = {
            METADATA_COLUMN_VALUE
    };

    private SQLiteDatabase mStoreDB;
    private DatabaseHelper mDatabaseHelper;
}
