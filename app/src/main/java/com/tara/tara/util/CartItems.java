package com.tara.tara.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by M1032467 on 2/20/2017.
 */

public class CartItems extends SQLiteOpenHelper {
    private static final String DB_NAME = "cart_database";
    private static final int DB_VERSION = 1;

    public static final String TABLE_NAME = "cart";
    public static final String FOOD_ID = "food_id";
    public static final String FOOD_NAME = "food_name";
    public static final String FOOD_DESC = "desc";
    public static final String PRICE = "price";
    public static final String IMAGE_URL = "image_url";
    public static final String ORDER_DATE = "order_date";
    private static final String QUERY_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + FOOD_ID + " TEXT, " + FOOD_NAME + " TEXT, " + PRICE + "  NUMBER, " + IMAGE_URL + " TEXT, " + FOOD_DESC + "TEXT," + ORDER_DATE + " DATE);";
    private static final String QUERY_DELETE = "DELETE FROM " + TABLE_NAME + ";";
    private static final String QUERY_DELETE_BY_PRODUCT_ID = "DELETE FROM " + TABLE_NAME + " WHERE " + FOOD_ID + ";";
    private static final String QUERY_SELECT = "SELECT DISTINCT * FROM " + TABLE_NAME + " ORDER BY date(" + ORDER_DATE + ") DESC;";

    public CartItems(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("createDB=", QUERY_CREATE);
        db.execSQL(QUERY_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query;
        query = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(query);
        onCreate(db);
    }

    public void addFood(String foodId, String foodName, String desc, int price, String imageUrl) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues insertValues = new ContentValues();
        insertValues.put(FOOD_ID, foodId);
        insertValues.put(FOOD_NAME, foodName);
        insertValues.put(PRICE, price);
        insertValues.put(FOOD_DESC, desc);
        insertValues.put(IMAGE_URL, imageUrl);
        insertValues.put(ORDER_DATE, new java.util.Date().getTime());

        db.insert(TABLE_NAME, null, insertValues);

        Log.d("Inserted into db => ", "");
    }

    public Cursor getAllItems() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY_SELECT, null);
        return cursor;
    }

    public void deleteAll() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(QUERY_DELETE);
    }

    public void deleteByProductId(String id) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + FOOD_ID + " = " + id + ";");
    }

//    public boolean checkIfProductPresent(String id) {
//        SQLiteDatabase db = getWritableDatabase();
//        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + PRODUCT_ID + " = " + id;
//        Cursor cursor = db.rawQuery(query, null);
//        if (cursor.getCount() <= 0) {
//            cursor.close();
//            return false;
//        }
//        cursor.close();
//        return true;
//    }
//
//    public Cursor getProductById(String id) {
//        SQLiteDatabase db = getReadableDatabase();
//        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + PRODUCT_ID + " = " + id;
//        Cursor cursor = db.rawQuery(query, null);
//        return cursor;
//    }

    public void deleteRecords() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(QUERY_DELETE);
    }

}
