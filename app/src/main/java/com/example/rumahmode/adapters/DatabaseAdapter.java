package com.example.rumahmode.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.example.rumahmode.R;

import java.io.ByteArrayOutputStream;


public class DatabaseAdapter {

    private static final String DATABASE_NAME = "myapp.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USER = "user";
    private static final String TABLE_PRODUCT = "product";
    private static final String TABLE_HISTORY = "history";

    public static final String KEY_ID = "_id";

    public static final String KEY_NAME = "name";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_PASSWORD = "password";

    public static final String KEY_TITLE = "title";
    public static final String KEY_PRICE = "price";
    public static final String KEY_IMAGE = "image";

    private static final String KEY_USER_ID = "user_id";
    public static final String KEY_PRODUCT_ID = "product_id";
    public static final String KEY_DATE = "date";

    private static final String CREATE_TABLE_USER = "CREATE TABLE "
            + TABLE_USER + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT," + KEY_ADDRESS + " TEXT,"
            + KEY_PASSWORD + " TEXT" + ")";

    private static final String CREATE_TABLE_PRODUCT = "CREATE TABLE "
            + TABLE_PRODUCT + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TITLE + " TEXT," + KEY_PRICE + " REAL,"
            + KEY_IMAGE + " BLOB" + ")";

    private static final String CREATE_TABLE_HISTORY = "CREATE TABLE "
            + TABLE_HISTORY + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_USER_ID + " INTEGER," + KEY_PRODUCT_ID + " INTEGER,"
            + KEY_DATE + " TEXT" + ")";

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase db;

    public DatabaseAdapter(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_USER);
            db.execSQL(CREATE_TABLE_PRODUCT);
            db.execSQL(CREATE_TABLE_HISTORY);

            initialInsertProduct("Dress", 150000.0, R.drawable.dress, db);
            initialInsertProduct("Jacket", 250000.0, R.drawable.jaket, db);
            initialInsertProduct("Outer", 300000.0, R.drawable.outer, db);
            initialInsertProduct("Pants", 280000.0, R.drawable.pants, db);
            initialInsertProduct("Tshirt", 190000.0, R.drawable.tshirt, db);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
            onCreate(db);
        }

        private void initialInsertProduct(String title, Double price, int drawableId, SQLiteDatabase db) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] imageData = stream.toByteArray();
            ContentValues values = new ContentValues();
            values.put(KEY_TITLE, title);
            values.put(KEY_PRICE, price);
            values.put(KEY_IMAGE, imageData);
            db.insert(TABLE_PRODUCT, null, values);

        }
    }

    public void close() {
        db.close();
        dbHelper.close();
    }

    public long insertUser(String name, String address, String password) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_ADDRESS, address);
        values.put(KEY_PASSWORD, password);
        return db.insert(TABLE_USER, null, values);
    }

    public int updateUser(long id, String name, String address) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_ADDRESS, address);
        return db.update(TABLE_USER, values, KEY_ID + " = ?", new String[] {String.valueOf(id)});
    }

    public Cursor getUser(long id) {
        return db.query(TABLE_USER, null, KEY_ID + " = ?", new String[] {String.valueOf(id)}, null, null, null);
    }

    public Cursor getProduct(long id) {
        return db.query(TABLE_PRODUCT, null, KEY_ID + " = ?", new String[] {String.valueOf(id)}, null, null, null);
    }

    public Cursor getAllProducts() {
        return db.query(TABLE_PRODUCT, null, null, null, null, null, null);
    }

    public long insertHistory(long user_id, long product_id, String date) {
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, user_id);
        values.put(KEY_PRODUCT_ID, product_id);
        values.put(KEY_DATE, date);
        return db.insert(TABLE_HISTORY, null, values);
    }

    public int deleteHistory(long id) {
        return db.delete(TABLE_HISTORY, KEY_ID + " = ?", new String[] {String.valueOf(id)});
    }

    public Cursor getHistoryByUser(long user_id) {
        return db.query(TABLE_HISTORY, null, KEY_USER_ID + " = ?", new String[] {String.valueOf(user_id)}, null, null, KEY_DATE + " DESC");
    }

}
