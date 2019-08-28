package com.prescywallet.presdigi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.prescywallet.presdigi.Model.PrescriptionImagePathItem;

import java.util.ArrayList;
import java.util.List;

public class AttachedPrescriptionDBHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "PrescyAttachedPres";
    // Contacts table name
    private static final String TABLE_NAME = "AttachedPrescription";
    // Shops Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_PRESCRIPTION_IMAGE_PATH = "prescription_image_path";

    public AttachedPrescriptionDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
        + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PRESCRIPTION_IMAGE_PATH + " TEXT)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Creating tables again
        onCreate(db);
    }

    public List<PrescriptionImagePathItem> getImagePath(){

        List<PrescriptionImagePathItem> result = new ArrayList<>();
// Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                result.add(new PrescriptionImagePathItem( cursor.getString(1)));
            } while (cursor.moveToNext());
        }
        db.close();
        return result;
// return conta
    }

    public void addPrescriptionImagePath(PrescriptionImagePathItem prescriptionImagePathItem){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues(); // Shop Name
        values.put(KEY_PRESCRIPTION_IMAGE_PATH, prescriptionImagePathItem.getPrescriptionImagePath()); // Shop Phone Number


        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }


    public void deletePrescriptionImagePath(String prescription_image_path){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_PRESCRIPTION_IMAGE_PATH + " =?",
                new String[] { prescription_image_path });
        db.close();
    }
    public void deleteAllDigitalPrescription(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }


}
