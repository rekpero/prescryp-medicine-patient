package com.prescywallet.presdigi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.prescywallet.presdigi.Model.AttachedDigitalPrescriptionItem;

import java.util.ArrayList;
import java.util.List;

public class AttachedDigitalPrescriptionDBHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "PrescyDigitalAttachedPres";
    // Contacts table name
    private static final String TABLE_NAME = "AttachedDigitalPrescription";
    // Shops Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_PRESCRIPTION_ID = "prescription_id";
    private static final String KEY_DATE = "date";

    public AttachedDigitalPrescriptionDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
        + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PRESCRIPTION_ID + " TEXT,"
                + KEY_DATE + " TEXT)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Creating tables again
        onCreate(db);
    }

    public List<AttachedDigitalPrescriptionItem> getDigitalPrescription(){

        List<AttachedDigitalPrescriptionItem> result = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                result.add(new AttachedDigitalPrescriptionItem( cursor.getString(1), cursor.getString(2)));
            } while (cursor.moveToNext());
        }
        db.close();
        return result;
    }

    public void addDigitalPrescription(AttachedDigitalPrescriptionItem digitalPrescriptionItem){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues(); // Shop Name
        values.put(KEY_PRESCRIPTION_ID, digitalPrescriptionItem.getPrescriptionId()); // Shop Phone Number
        values.put(KEY_DATE, digitalPrescriptionItem.getDate());


        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }


    public void deleteDigitalPrescription(String prescription_id){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_PRESCRIPTION_ID + " =?",
                new String[] { prescription_id });
        db.close();
    }

    public void deleteAllDigitalPrescription(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

}
