package com.prescywallet.presdigi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.prescywallet.presdigi.Model.DeliveryAddressItem;

import java.util.ArrayList;
import java.util.List;

public class PatientAddressDBHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "PrescyCompleteAddress";
    // Contacts table name
    private static final String TABLE_NAME = "PatientCompleteAddress";
    // Shops Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_LOCALITY = "locality";
    private static final String KEY_COMPLETE_ADDRESS = "complete_address";
    private static final String KEY_DELIVERY_INSTRUCTION = "delivery_instruction";
    private static final String KEY_DELIVERY_NICKNAME = "delivery_nickname";
    private static final String KEY_DELIVERY_LATITUDE = "delivery_latitude";
    private static final String KEY_DELIVERY_LONGITUDE = "delivery_longitude";

    public PatientAddressDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
        + KEY_ID + " INTEGER PRIMARY KEY," + KEY_LOCALITY + " TEXT,"
                + KEY_COMPLETE_ADDRESS + " TEXT,"
                + KEY_DELIVERY_INSTRUCTION + " TEXT,"
                + KEY_DELIVERY_NICKNAME + " TEXT,"
                + KEY_DELIVERY_LATITUDE + " TEXT,"
                + KEY_DELIVERY_LONGITUDE + " TEXT)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Creating tables again
        onCreate(db);
    }

    public List<DeliveryAddressItem> getAllDeliveryAddress(){

        List<DeliveryAddressItem> result = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                result.add(new DeliveryAddressItem( cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5),
                        cursor.getString(6)));
            } while (cursor.moveToNext());
        }
        db.close();
        return result;
    }

    public void addDeliveryAddress(DeliveryAddressItem addressItem){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues(); // Shop Name
        values.put(KEY_LOCALITY, addressItem.getLocality()); // Shop Phone Number
        values.put(KEY_COMPLETE_ADDRESS, addressItem.getCompleteAddress());
        values.put(KEY_DELIVERY_INSTRUCTION, addressItem.getDeliveryInstruction());
        values.put(KEY_DELIVERY_NICKNAME, addressItem.getDeliveryNickname());
        values.put(KEY_DELIVERY_LATITUDE, addressItem.getDeliveryLatitude());
        values.put(KEY_DELIVERY_LONGITUDE, addressItem.getDeliveryLongitude());


        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    public void deleteAddress(String latitude, String longitude){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_DELIVERY_LATITUDE + " =? AND " + KEY_DELIVERY_LONGITUDE + " =?",
                new String[] { latitude, longitude });
        db.close();
    }



    public void deleteAllAddress(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

}
