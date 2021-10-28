package de.uni_bamberg.mi.sem.speedreader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andreas on 17.06.2017.
 */

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "textx.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLENAME = "Text_Table";
    private static final String COL_ID = "Text_ID";
    private static final String COL_NAME = "Text_Name";
    private static final String COL_TEXT = "Text";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT)", TABLENAME, COL_ID, COL_NAME, COL_TEXT);
        db.execSQL(createTable);
    }

    public void storeText(String[] text) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, text[0]);
        contentValues.put(COL_TEXT, text[1]);

        long rowId = db.insert(TABLENAME, null, contentValues);
        Log.d(MainActivity.DEBUGTAG, String.valueOf(rowId));
    }

    public String getText(String textName) {
        SQLiteDatabase db = getReadableDatabase();
        String text = "";

        String[] projection = {
                COL_ID,
                COL_NAME,
                COL_TEXT
        };

        String selection = COL_NAME + " = ?";
        String[] selectionArgs = {textName};

        Cursor cursor = db.query(
                TABLENAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToNext()) {
            text = cursor.getString(cursor.getColumnIndex(COL_TEXT));
        }

        return text;
    }

    public List<Text> getAllTexts() {
        List<Text> texts = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String sql = String.format("SELECT %s, %s FROM %s", COL_NAME, COL_TEXT, TABLENAME);
        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            String title = cursor.getString(0);
            String text = cursor.getString(1);
            texts.add(new Text(title, text));
        }
        db.close();

        return texts;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
