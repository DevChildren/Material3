package com.develite.pro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "project_manager.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_ESTIMASI = "CREATE TABLE estimasi (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "nama_proyek TEXT, " +
            "biaya DOUBLE)";
    private static final String TABLE_MATERIALS = "CREATE TABLE materials(" + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "name TEXT NOT NULL, " + "unit TEXT NOT NULL, " + "price REAL NOT NULL)";
    private static final String TABLE_ESTIMATIONS = "CREATE TABLE estimations(" + "project_id INTEGER NOT NULL, " + "material_id INTEGER NOT NULL)";
    private static final String TABLE_PROJECTS = "CREATE TABLE projcts (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "name TEXT NOT NULL, " + "client TEXT, " + "start_date TEXT, " + "end_date TEXT, " + "status TEXT CHECK(status IN ('Pending', 'Ongoing', 'Completed')) DEFAULT 'Pending')";
    private static final String TABLE_TAKS = "CREATE TABLE taks (" + "project_id INTEGER NOT NULL, " + "task_name TEXT NOT NULL, " + "assigned_to TEXT, " + "deadline TEXT, " + "status TEXT CHECK(status IN ('Pending', 'In Progress', 'Done')) DEFAULT 'Pending')";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_ESTIMASI);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS estimasi");
        onCreate(db);
    }

    // Method to get all projects
    public List<Proyek> getAllProyek() {
        List<Proyek> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM estimasi", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String nama = cursor.getString(cursor.getColumnIndexOrThrow("nama_proyek"));
                double biaya = cursor.getDouble(cursor.getColumnIndexOrThrow("biaya"));

                list.add(new Proyek(id, nama, biaya));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }

    // Method to delete a project
    public boolean deleteProyek(int id) {
    SQLiteDatabase db = this.getWritableDatabase();
    try {
        int result = db.delete("estimasi", "id = ?", new String[]{String.valueOf(id)});
        return result > 0;  // Jika berhasil, return true
    } catch (Exception e) {
        e.printStackTrace();
        return false;  // Jika gagal, return false
    }
}



    // Method to update a project
    public void updateProyek(int id, String namaBaru, double biayaBaru) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nama_proyek", namaBaru);
        values.put("biaya", biayaBaru);
        db.update("estimasi", values, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
