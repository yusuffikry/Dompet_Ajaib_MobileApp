package com.example.dompetajaib;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dompetajaib.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "transaksi";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_JENIS = "jenis";
    private static final String COLUMN_JUMLAH = "jumlah";
    private static final String COLUMN_CATATAN = "catatan";
    private static final String COLUMN_KATEGORI = "kategori";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_JENIS + " TEXT, " +
                COLUMN_JUMLAH + " INTEGER, " +
                COLUMN_CATATAN + " TEXT, " +
                COLUMN_KATEGORI + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String jenis, int jumlah, String catatan, String kategori) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_JENIS, jenis);
        contentValues.put(COLUMN_JUMLAH, jumlah);
        contentValues.put(COLUMN_CATATAN, catatan);
        contentValues.put(COLUMN_KATEGORI, kategori);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }



    public int getTotal(String jenis) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_JUMLAH + ") as total FROM " + TABLE_NAME + " WHERE " + COLUMN_JENIS + "=?", new String[]{jenis});
        if (cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndexOrThrow("total"));
        } else {
            return 0;
        }
    }

    public boolean updateData(int id, String jenis, int jumlah, String catatan, String kategori) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_JENIS, jenis);
        contentValues.put(COLUMN_JUMLAH, jumlah);
        contentValues.put(COLUMN_CATATAN, catatan);
        contentValues.put(COLUMN_KATEGORI, kategori);
        int result = db.update(TABLE_NAME, contentValues, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public boolean deleteData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(id)}) > 0;
    }

}
