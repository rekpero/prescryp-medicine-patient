package com.prescywallet.presdigi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.prescywallet.presdigi.Model.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartDbHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "PrescyForPatient";
    // Contacts table name
    private static final String TABLE_NAME = "OrderDetail";
    // Shops Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_MEDICINE_NAME = "medicine_name";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_PRICE = "price";
    private static final String KEY_SELLER_NAME = "seller_name";
    private static final String KEY_SELLER_MOBILE_NUMBER = "seller_mobile_number";
    private static final String KEY_REQUIRE_PRESCRIPTION = "require_prescription";
    private static final String KEY_PACKAGE_CONTAIN = "package_contain";


    public CartDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
        + KEY_ID + " INTEGER PRIMARY KEY," + KEY_MEDICINE_NAME + " TEXT,"
        + KEY_QUANTITY + " TEXT," + KEY_PRICE + " TEXT,"+ KEY_SELLER_NAME +
                " TEXT," + KEY_SELLER_MOBILE_NUMBER + " TEXT,"+ KEY_REQUIRE_PRESCRIPTION +
                " TEXT," + KEY_PACKAGE_CONTAIN +  " TEXT)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Creating tables again
        onCreate(db);
    }

    public List<CartItem> getCart(){

        List<CartItem> result = new ArrayList<CartItem>();
// Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                result.add(new CartItem(cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7)));
            } while (cursor.moveToNext());
        }
        db.close();
        return result;
// return conta
    }

    public void addToCart(CartItem cartItem){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_MEDICINE_NAME, cartItem.getMedicineName()); // Shop Name
        values.put(KEY_QUANTITY, cartItem.getQuantity()); // Shop Phone Number
        values.put(KEY_PRICE, cartItem.getPrice());
        values.put(KEY_SELLER_NAME, cartItem.getSellerName());
        values.put(KEY_SELLER_MOBILE_NUMBER, cartItem.getSellerMobileNumber());
        values.put(KEY_REQUIRE_PRESCRIPTION, cartItem.getRequirePrescription());
        values.put(KEY_PACKAGE_CONTAIN, cartItem.getPackageContain());

        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    public void updateCart(String medicineName, String updatedValue, String packageContain){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_QUANTITY, updatedValue); // Shop Phone Number

// updating row
        db.update(TABLE_NAME, values, KEY_MEDICINE_NAME + " = ? AND " + KEY_PACKAGE_CONTAIN + " =?",
                new String[]{medicineName, packageContain});
    }

    public void deleteCart(String MedicineName, String packageContain){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_MEDICINE_NAME + " = ? AND " + KEY_PACKAGE_CONTAIN + " =?",
                new String[] { MedicineName, packageContain });
        db.close();
    }

    public void deleteAllCart(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

}
