package com.example.bookmark;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.AdapterView;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AppCompatActivity;
import static com.example.bookmark.DBContract.DBEntry;
import static com.example.bookmark.DBContract.CategoryEntry;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;

public class MainActivity extends AppCompatActivity {

    private int parent_id = 0;
    private String parent_name;

    private SampDatabaseHelper helper = null;
    MainListAdapter sc_adapter;

    // アクティビティの初期化処理
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar =  findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        parent_id = intent.getIntExtra(DBContract.CategoryEntry._ID, 0);
        parent_name = intent.getStringExtra(DBContract.CategoryEntry.COLUMN_NAME_CATEGORY);

    }

    // アクティビティの再開処理
    @Override
    protected void onResume() {
        super.onResume();

        // データを一覧表示
        onShow();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menuresourcefile,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int add_id = item.getItemId();
        if(add_id==R.id.add_button){
            Intent intent = new Intent(MainActivity.this,TextActivity.class);
            intent.putExtra(DBEntry._ID, 0);
            intent.putExtra(CategoryEntry._ID, parent_id);
            intent.putExtra(CategoryEntry.COLUMN_NAME_CATEGORY, parent_name);
            startActivity(intent);
        }
        return true;
    }

    // データを一覧表示
    protected void onShow() {

        // データベースヘルパーを準備
        helper = new SampDatabaseHelper(this);

        // データベースを検索する項目を定義
        String[] cols = {DBEntry._ID, DBEntry.COLUMN_NAME_TITLE, DBEntry.COLUMN_NAME_CONTENTS, DBEntry.COLUMN_NAME_URL, DBEntry.COLUMN_NAME_TAGS, DBEntry.COLUMN_NAME_PARENT, DBEntry.COLUMN_NAME_REFERENCE_COUNT };

        // 読み込みモードでデータベースをオープン
        try (SQLiteDatabase db = helper.getReadableDatabase()){
            String sortOrder = DBEntry.COLUMN_NAME_REFERENCE_COUNT + " DESC";
            //String selection = DBEntry.COLUMN_NAME_PARENT + " = ?";
            //String[] selectionArgs = new String[] { String.valueOf(parent_id) };

            // データベースを検索
            /*Cursor cursor = db.query(DBEntry.TABLE_NAME, cols, selection,
                    selectionArgs, null, null, sortOrder);*/
            Cursor cursor = db.query(DBEntry.TABLE_NAME, cols, null,
                    null, null, null, sortOrder);

            // 検索結果から取得する項目を定義
            String[] from = {DBEntry.COLUMN_NAME_TITLE, DBEntry.COLUMN_NAME_CONTENTS};

            // データを設定するレイアウトのフィールドを定義
            int[] to = {R.id.title, R.id.content};

            // ListViewの1行分のレイアウト(row_main.xml)と検索結果を関連付け
            sc_adapter = new MainListAdapter(
                    this, R.layout.row_main, cursor, from, to,0);

            // activity_main.xmlに定義したListViewオブジェクトを取得
            ListView list = findViewById(R.id.mainList);

            // ListViewにアダプターを設定
            list.setAdapter(sc_adapter);

            // リストの項目をクリックしたときの処理
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> av, View view, int position, long id) {
                    Cursor cursor = (Cursor) av.getItemAtPosition(position);

                    incrementReferenceCount(id);

                    // DetailActivityへのインテントを作成
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra(DBEntry._ID, cursor.getInt(0));
                    intent.putExtra(DBEntry.COLUMN_NAME_TITLE, cursor.getString(1));
                    intent.putExtra(DBEntry.COLUMN_NAME_CONTENTS, cursor.getString(2));
                    intent.putExtra(DBEntry.COLUMN_NAME_URL, cursor.getString(3));
                    intent.putExtra(DBEntry.COLUMN_NAME_TAGS, cursor.getString(4));
                    //intent.putExtra(DBEntry.COLUMN_NAME_PARENT, cursor.getInt(5));
                    intent.putExtra(CategoryEntry._ID, parent_id);
                    intent.putExtra(CategoryEntry.COLUMN_NAME_CATEGORY, parent_name);


                    // アクティビティを起動
                    startActivity(intent);
                }
            });

        }
    }

    private void incrementReferenceCount(long itemId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(DBEntry.TABLE_NAME, new String[]{DBEntry.COLUMN_NAME_REFERENCE_COUNT},
                DBEntry._ID + " = ?", new String[]{String.valueOf(itemId)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DBEntry.COLUMN_NAME_REFERENCE_COUNT);
            if (columnIndex >= 0) {
                int currentCount = cursor.getInt(columnIndex);
                ContentValues values = new ContentValues();
                values.put(DBEntry.COLUMN_NAME_REFERENCE_COUNT, currentCount + 1);

                // 参照回数を更新
                db.update(DBEntry.TABLE_NAME, values, DBEntry._ID + " = ?", new String[]{String.valueOf(itemId)});
            }
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
    }




    // 削除ボタン　タップ時に呼び出されるメソッド
    public void btnDel_onClick(View view){

        // MainListAdapterで設定されたリスト内の位置を取得
        int pos = (Integer)view.getTag();

        // アダプターから、_idの値を取得
        int id = ((Cursor) sc_adapter.getItem(pos)).getInt(0);

        // データを削除
        try (SQLiteDatabase db = helper.getWritableDatabase()) {
            db.delete(DBEntry.TABLE_NAME, DBEntry._ID+" = ?", new String[] {String.valueOf(id)});
        }

        // データを一覧表示
        onShow();
    }

    // 「+」フローティング操作ボタン　タップ時に呼び出されるメソッド
    public void fab_reg_onClick(View view) {

        // テキスト登録画面 Activity へのインテントを作成
        Intent intent  = new Intent(MainActivity.this, com.example.bookmark.TextActivity.class);

        // アクティビティを起動
        startActivity(intent);
    }
}