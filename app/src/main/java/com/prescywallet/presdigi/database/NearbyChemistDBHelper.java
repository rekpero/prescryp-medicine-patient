package com.prescywallet.presdigi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.maps.model.LatLng;
import com.prescywallet.presdigi.Model.ChemistLatLngItem;

import java.util.ArrayList;
import java.util.List;

public class NearbyChemistDBHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "PrescyNearbyChemist";
    // Contacts table name
    private static final String TABLE_NAME = "NearbyMedicineShop";
    // Shops Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_CHEMIST_MOB_NUM = "chemist_mobile_number";
    private static final String KEY_CHEMIST_STORE_NAME = "chemist_store_name";
    private static final String KEY_CHEMIST_LATITUDE = "chemist_latitude";
    private static final String KEY_CHEMIST_LONGITUDE = "chemist_longitude";

    public NearbyChemistDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
        + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CHEMIST_MOB_NUM + " TEXT,"
                + KEY_CHEMIST_STORE_NAME + " TEXT,"
                + KEY_CHEMIST_LATITUDE + " TEXT,"
                + KEY_CHEMIST_LONGITUDE + " TEXT)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Creating tables again
        onCreate(db);
    }

    public List<ChemistLatLngItem> getNearbyChemist(){

        List<ChemistLatLngItem> result = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                result.add(new ChemistLatLngItem( cursor.getString(1), cursor.getString(2),
                        new LatLng(Double.valueOf(cursor.getString(3)), Double.valueOf(cursor.getString(4)))));
            } while (cursor.moveToNext());
        }
        db.close();
        return result;
    }

    public void addNearbyChemist(ChemistLatLngItem chemistLatLngItem){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues(); // Shop Name
        values.put(KEY_CHEMIST_MOB_NUM, chemistLatLngItem.getChemistMobileNumber()); // Shop Phone Number
        values.put(KEY_CHEMIST_STORE_NAME, chemistLatLngItem.getChemistStoreName());
        values.put(KEY_CHEMIST_LATITUDE, chemistLatLngItem.getLatLng().latitude);
        values.put(KEY_CHEMIST_LONGITUDE, chemistLatLngItem.getLatLng().longitude);


        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }



    public void deleteAllNearbyChemist(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

}
