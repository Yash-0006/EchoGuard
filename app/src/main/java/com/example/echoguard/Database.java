package com.example.echoguard;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "safeguard.db";
    private static final int DATABASE_VERSION = 1;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE emergency_contacts (_id INTEGER PRIMARY KEY, name TEXT, phone_number TEXT)");
        db.execSQL("CREATE TABLE code_words (_id INTEGER PRIMARY KEY, code_word TEXT)"); // Adding code_words table
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS emergency_contacts");
        db.execSQL("DROP TABLE IF EXISTS code_words"); // Drop the code_words table during upgrade
        onCreate(db);
    }

    public void addEmergencyContact(String name, String phoneNumber) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("phone_number", phoneNumber);
        db.insert("emergency_contacts", null, values);
        db.close();
    }

    public List<String> getEmergencyContactNumbers() {
        List<String> contactNumbers = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("emergency_contacts", new String[] {"phone_number"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String phoneNumber = cursor.getString(0);
            contactNumbers.add(phoneNumber);
        }
        cursor.close();
        db.close();
        return contactNumbers;
    }

    // New method to add a code word to the database
    public void addCodeWord(String codeWord) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("code_word", codeWord);
        db.insert("code_words", null, values);
        db.close();
    }

    // New method to get the code word from the database
    public String getCodeWord() {
        String codeWord = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("code_words", new String[]{"code_word"}, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            codeWord = cursor.getString(0); // Get the first (and only) code word
        }

        cursor.close();
        db.close();
        return codeWord;
    }
}
