package com.example.assignment4;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "wallpaper_db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "IMAGES";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TEMP_NAME = "temp_name"; // Optional: for image name
    public static final String COLUMN_IMAGE_DATA = "image_data";

    private static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_TEMP_NAME + " TEXT, " +
            COLUMN_IMAGE_DATA + " BLOB);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public long insertImage(int id,String temp, Bitmap imageBitmap) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Convert bitmap to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE_DATA, byteArray);
        values.put(COLUMN_ID,id);
        values.put(COLUMN_TEMP_NAME,temp);
        values.put(COLUMN_IMAGE_DATA,byteArray);

        // Insert the image data into the database
        long newRowId = db.insert(TABLE_NAME, null, values);
        db.close(); // Close the database connection
        return newRowId;
    }

    public boolean delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete(TABLE_NAME, COLUMN_ID + "=?",new String[] {String.valueOf(id)});
        db.close();
        return deletedRows > 0;
    }
}

