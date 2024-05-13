package com.example.bookmark;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static com.example.bookmark.DBContract.DBEntry;
import static com.example.bookmark.DBContract.CategoryEntry;

// データベースをアプリから使用するために、 SQLiteOpenHelperを継承する
// SQLiteOpenHelperは、データベースやテーブルが存在する場合はそれを開き、存在しない場合は作成してくれる
public class SampDatabaseHelper extends SQLiteOpenHelper {

    // データベースのバージョン
    // テーブルの内容などを変更したら、この数字を変更する
    static final private int VERSION = 11;

    // データベース名
    static final private String DBNAME = "samp.db";

    // コンストラクタは必ず必要
    public SampDatabaseHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    // データベース作成時にテーブルを作成
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE " + CategoryEntry.TABLE_NAME + " (" +
                        CategoryEntry._ID + " INTEGER PRIMARY KEY, " +
                        CategoryEntry.COLUMN_NAME_CATEGORY_REFERENCE_COUNT + " INTEGER DEFAULT 0, " +
                        CategoryEntry.COLUMN_NAME_CATEGORY + " TEXT)"
        );


        // テーブルを作成
        db.execSQL(
                "CREATE TABLE "+ DBEntry.TABLE_NAME + " (" +
                        DBEntry._ID + " INTEGER PRIMARY KEY, " +
                        DBEntry.COLUMN_NAME_TITLE + " TEXT default 'タイトル', " +
                        DBEntry.COLUMN_NAME_CONTENTS + " TEXT default '', " +
                        DBEntry.COLUMN_NAME_UPDATE + " INTEGER DEFAULT (datetime(CURRENT_TIMESTAMP,'localtime'))," +
                        DBEntry.COLUMN_NAME_REFERENCE_COUNT + " INTEGER DEFAULT 0, " +
                        DBEntry.COLUMN_NAME_TAGS + " TEXT, " +
                        DBEntry.COLUMN_NAME_URL + " TEXT, " +
                        DBEntry.COLUMN_NAME_PARENT + " INTEGER, " +
                        "FOREIGN KEY(" + DBEntry.COLUMN_NAME_PARENT + ") REFERENCES " +
                        CategoryEntry.TABLE_NAME + "(" + CategoryEntry._ID + "))"

        );


        // トリガーを作成
        db.execSQL(
                "CREATE TRIGGER trigger_samp_tbl_update AFTER UPDATE ON " + DBEntry.TABLE_NAME +
                        " BEGIN "+
                        " UPDATE " + DBEntry.TABLE_NAME + " SET up_date = DATETIME('now', 'localtime') WHERE rowid == NEW.rowid; "+
                        " END;");
    }

    // データベースをバージョンアップした時、テーブルを削除してから再作成
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DBEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CategoryEntry.TABLE_NAME);

        onCreate(db);
    }
}