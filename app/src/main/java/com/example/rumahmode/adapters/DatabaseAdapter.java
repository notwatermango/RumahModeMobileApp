package com.example.rumahmode.adapters;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import com.example.rumahmode.MainActivity;
import com.example.rumahmode.R;
import com.example.rumahmode.fragments.HistoryFragment;

import java.io.ByteArrayOutputStream;


public class DatabaseAdapter {

    // Database name and version
    private static final String DATABASE_NAME = "myapp.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    private static final String TABLE_USER = "user";
    private static final String TABLE_PRODUCT = "product";
    private static final String TABLE_HISTORY = "history";

    // Common column names
    public static final String KEY_ID = "_id";

    // User table column names
    public static final String KEY_NAME = "name";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_PASSWORD = "password";

    // Product table column names
    public static final String KEY_TITLE = "title";
    public static final String KEY_PRICE = "price";
    public static final String KEY_IMAGE = "image";

    // History table column names
    private static final String KEY_USER_ID = "user_id";
    public static final String KEY_PRODUCT_ID = "product_id";
    public static final String KEY_DATE = "date";

    // Table creation statements
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

    // Database helper class
    private DatabaseHelper dbHelper;

    private Context context;

    // Database object
    private SQLiteDatabase db;

    // Constructor
    public DatabaseAdapter(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    // Database helper inner class
    private class DatabaseHelper extends SQLiteOpenHelper {

        // Constructor
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        // Create tables
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

        // Upgrade database
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

    // Close database connection
    public void close() {
        db.close();
        dbHelper.close();
    }

    // Insert a user into the user table
    public long insertUser(String name, String address, String password) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_ADDRESS, address);
        values.put(KEY_PASSWORD, password);
        return db.insert(TABLE_USER, null, values);
    }

    // Update a user in the user table by id
    public int updateUser(long id, String name, String address) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_ADDRESS, address);
        return db.update(TABLE_USER, values, KEY_ID + " = ?", new String[] {String.valueOf(id)});
    }

    // Delete a user from the user table by id
    public int deleteUser(long id) {
        return db.delete(TABLE_USER, KEY_ID + " = ?", new String[] {String.valueOf(id)});
    }

    // Get a user from the user table by id
    public Cursor getUser(long id) {
        return db.query(TABLE_USER, null, KEY_ID + " = ?", new String[] {String.valueOf(id)}, null, null, null);
    }

    // Get a user from the user table by name and password (for login)
    public Cursor getUser(String name, String password) {
        return db.query(TABLE_USER, null, KEY_NAME + " = ? AND " + KEY_PASSWORD + " = ?", new String[] {name, password}, null, null, null);
    }

    // Insert a product into the product table
    public long insertProduct(String title, double price, byte[] image) {
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, title);
        values.put(KEY_PRICE, price);
        values.put(KEY_IMAGE, image);
        return db.insert(TABLE_PRODUCT, null, values);
    }

    // Update a product in the product table by id
    public int updateProduct(long id, String title, double price, byte[] image) {
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, title);
        values.put(KEY_PRICE, price);
        values.put(KEY_IMAGE, image);
        return db.update(TABLE_PRODUCT, values, KEY_ID + " = ?", new String[] {String.valueOf(id)});
    }

    // Delete a product from the product table by id
    public int deleteProduct(long id) {
        return db.delete(TABLE_PRODUCT, KEY_ID + " = ?", new String[] {String.valueOf(id)});
    }

    // Get a product from the product table by id
    public Cursor getProduct(long id) {
        return db.query(TABLE_PRODUCT, null, KEY_ID + " = ?", new String[] {String.valueOf(id)}, null, null, null);
    }

    // Get all products from the product table
    public Cursor getAllProducts() {
        return db.query(TABLE_PRODUCT, null, null, null, null, null, null);
    }

    // Insert a history record into the history table
    public long insertHistory(long user_id, long product_id, String date) {
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, user_id);
        values.put(KEY_PRODUCT_ID, product_id);
        values.put(KEY_DATE, date);
        return db.insert(TABLE_HISTORY, null, values);
    }

    // Delete a history record from the history table by id
    public int deleteHistory(long id) {
        return db.delete(TABLE_HISTORY, KEY_ID + " = ?", new String[] {String.valueOf(id)});
    }

    // Delete all history records from the history table by user id
    public int deleteHistoryByUser(long user_id) {
        return db.delete(TABLE_HISTORY, KEY_USER_ID + " = ?", new String[] {String.valueOf(user_id)});
    }

    // Get a history record from the history table by id
    public Cursor getHistory(long id) {
        return db.query(TABLE_HISTORY, null, KEY_ID + " = ?", new String[] {String.valueOf(id)}, null, null, null);
    }

    // Get all history records from the history table by user id
    public Cursor getHistoryByUser(long user_id) {
        return db.query(TABLE_HISTORY, null, KEY_USER_ID + " = ?", new String[] {String.valueOf(user_id)}, null, null, KEY_DATE + " DESC");
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        // We ask for the bounds if they have been set as they would be most
        // correct, then we check we are  > 0
        final int width = !drawable.getBounds().isEmpty() ?
                drawable.getBounds().width() : drawable.getIntrinsicWidth();

        final int height = !drawable.getBounds().isEmpty() ?
                drawable.getBounds().height() : drawable.getIntrinsicHeight();

        // Now we check we are > 0
        final Bitmap bitmap = Bitmap.createBitmap(width <= 0 ? 1 : width, height <= 0 ? 1 : height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
