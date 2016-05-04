package com.example.phama.youtube;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phama on 2/5/2016.
 */
public class Database extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "History";
    private static final String TABLE_ITEM_NAME = "tbhis";

    private Context mContext;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db  ) {
        String stringCreateTableItem = "CREATE TABLE " + TABLE_ITEM_NAME + " ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, videoId TEXT, title TEXT,url TEXT)";
        db.execSQL(stringCreateTableItem);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM_NAME);
        onCreate(db);
    }


    public Video getItem(int itemId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_ITEM_NAME + " WHERE id =" + itemId, null);
        try {
            Video Video = null;
            if (cursor.moveToFirst()) {
                int Id = cursor.getInt(cursor
                        .getColumnIndex("id"));

                String videoId = cursor.getString(cursor
                        .getColumnIndex("videoId"));
                String title = cursor.getString(cursor
                        .getColumnIndex("title"));
                String url = cursor.getString(cursor
                        .getColumnIndex("url"));
                Video = new Video(Id, videoId, title, url);
            }
            return Video;
        } finally {
            db.close();
            cursor.close();
        }

    }

    public List<Video> getItems() {
        List<Video> Video = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_ITEM_NAME +" order by id DESC", null);
        try {
            if (cursor.moveToFirst()) {
                while (cursor.isAfterLast() == false) {
                    int Id = cursor.getInt(cursor
                            .getColumnIndex("id"));

                    String videoId = cursor.getString(cursor
                            .getColumnIndex("videoId"));
                    String title = cursor.getString(cursor
                            .getColumnIndex("title"));
                    String url = cursor.getString(cursor
                            .getColumnIndex("url"));
                    Video.add(new Video(Id, videoId, title, url));
                    cursor.moveToNext();
                }
            }
            return Video;
        } finally {
            db.close();
            cursor.close();
        }

    }


    public boolean insertItem(Video Video) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues itemContentValues = new ContentValues();

            itemContentValues.put("videoId", Video.getVideoId());
            itemContentValues.put("title", Video.getTitle());
            itemContentValues.put("url", Video.getUrl());
            try {
                db.insert(TABLE_ITEM_NAME, null, itemContentValues);
            } catch (Exception ex) {
                return false;
            }
            return true;
        } finally {
            db.close();
        }
    }




    public boolean deleteItem(int id) {
        boolean result = true;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(TABLE_ITEM_NAME, "id = " + id, null);
        } catch (Exception ex) {
            result = false;
        } finally {
            db.close();
            return result;
        }
    }

}