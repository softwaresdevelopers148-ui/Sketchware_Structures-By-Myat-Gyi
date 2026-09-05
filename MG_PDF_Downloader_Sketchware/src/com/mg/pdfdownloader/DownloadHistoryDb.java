package com.mg.pdfdownloader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/** Small local database for download history. */
public final class DownloadHistoryDb extends SQLiteOpenHelper {
    private static final String DB_NAME = "mg_pdf_downloader.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE = "downloads";

    public DownloadHistoryDb(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " ("
                + "book_id TEXT PRIMARY KEY, title TEXT NOT NULL, file_name TEXT, "
                + "download_id INTEGER, status TEXT, progress INTEGER, updated_at INTEGER"
                + ")");
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public void saveQueued(Book book, long downloadId) {
        ContentValues values = new ContentValues();
        values.put("book_id", book.id);
        values.put("title", book.title);
        values.put("file_name", book.title + ".pdf");
        values.put("download_id", downloadId);
        values.put("status", "queued");
        values.put("progress", 0);
        values.put("updated_at", System.currentTimeMillis());
        getWritableDatabase().insertWithOnConflict(TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void updateProgress(long downloadId, String status, int progress) {
        ContentValues values = new ContentValues();
        values.put("status", status);
        values.put("progress", progress);
        values.put("updated_at", System.currentTimeMillis());
        getWritableDatabase().update(TABLE, values, "download_id = ?",
                new String[]{String.valueOf(downloadId)});
    }

    public List<String> getHistoryLabels() {
        ArrayList<String> result = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query(
                TABLE,
                new String[]{"title", "status", "progress"},
                null, null, null, null, "updated_at DESC");
        try {
            while (cursor.moveToNext()) {
                result.add(cursor.getString(0) + "\n"
                        + cursor.getString(1) + " • " + cursor.getInt(2) + "%");
            }
        } finally {
            cursor.close();
        }
        return result;
    }
}
