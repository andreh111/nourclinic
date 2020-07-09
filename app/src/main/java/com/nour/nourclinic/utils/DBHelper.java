package com.nour.nourclinic.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nour.nourclinic.models.Treatment;

import java.util.ArrayList;


public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "clinicdatabase.db";
    public static final int DATABASE_VERSION = 1;
    // define constants for TREATMENTointments table
    public static final String TREATMENT_TABLE_NAME = "treatment";
    public static final String TREATMENT_COLUMN_ID = "treatment_ID";
    public static final String TREATMENT_COLUMN_NAME = "treatment_Name";
    public static final String TREATMENT_COLUMN_PRICE = "treatment_Price";
    public static final String TREATMENT_COLUMN_DESC = "treatment_Desc";
    public static final String TREATMENT_COLUMN_USER = "treatment_User";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table TREATMENTointments
        db.execSQL("create table " + TREATMENT_TABLE_NAME + " (" +
                TREATMENT_COLUMN_ID + " numeric(100) primary key, " +
                TREATMENT_COLUMN_NAME + " varchar(100) not null, " +
                TREATMENT_COLUMN_PRICE + " varchar(100) not null, " +
                TREATMENT_COLUMN_DESC + " varchar(100) not null, " +
                TREATMENT_COLUMN_USER + " varchar(100) not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TREATMENT_TABLE_NAME);
        onCreate(db);
    }

    public void addTreatment(Treatment treatment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TREATMENT_COLUMN_ID, treatment.getId());
        contentValues.put(TREATMENT_COLUMN_NAME, treatment.getName());
        contentValues.put(TREATMENT_COLUMN_PRICE, treatment.getPrice());
        contentValues.put(TREATMENT_COLUMN_DESC, treatment.getDesc());
        contentValues.put(TREATMENT_COLUMN_USER, treatment.getUser());
        db.insertWithOnConflict(TREATMENT_TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_ABORT);
        db.close();
    }



    public void deleteTreatment(int id) throws Exception {
        SQLiteDatabase db = this.getWritableDatabase();
        int count = db.delete(TREATMENT_TABLE_NAME,
                TREATMENT_COLUMN_ID + " = ? ",
                new String[] { Integer.toString(id) });
        db.close();
        if (count < 1) throw new Exception("record not found");
    }

    public ArrayList<Treatment> getTreatments() {
        ArrayList<Treatment> array_list = new ArrayList<Treatment>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from " +
                TREATMENT_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(new Treatment(res.getInt(0),res.getString(1),res.getInt(2),res.getString(3),res.getInt(4)));
            res.moveToNext();
        }
        db.close();
        return array_list;
    }

    public boolean treatmentExists(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        String sql ="SELECT treatment_ID FROM "+TREATMENT_TABLE_NAME+" WHERE treatment_ID="+TREATMENT_COLUMN_ID;
        cursor= db.rawQuery(sql,null);

        if(cursor.getCount()>0){
            return true;
        }else{
            return false;
        }

    }

}
