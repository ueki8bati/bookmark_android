package com.example.bookmark;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.AdapterView;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AppCompatActivity;
import static com.example.bookmark.DBContract.CategoryEntry;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;

import android.os.Bundle;

public class CategoryActivity extends AppCompatActivity {

    private SampDatabaseHelper helper = null;
    MainListAdapter sc_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        final Toolbar toolbar =  findViewById(R.id.toolbar_category);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // データを一覧表示
        on_category_Show();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menuresourcefile,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int add_id = item.getItemId();
        if(add_id==R.id.add_button){
            Intent intent = new Intent(CategoryActivity.this,CategoryTextActivity.class);
            startActivity(intent);
        }
        return true;
    }

    protected void on_category_Show() {

        // データベースヘルパーを準備
        helper = new SampDatabaseHelper(this);

        // データベースを検索する項目を定義
        String[] cols = {CategoryEntry._ID, CategoryEntry.COLUMN_NAME_CATEGORY, CategoryEntry.COLUMN_NAME_CATEGORY_REFERENCE_COUNT };

        // 読み込みモードでデータベースをオープン
        try (SQLiteDatabase db = helper.getReadableDatabase()){
            String sortOrder = CategoryEntry.COLUMN_NAME_CATEGORY_REFERENCE_COUNT + " DESC";

            // データベースを検索
            Cursor cursor = db.query(CategoryEntry.TABLE_NAME, cols, null,
                    null, null, null, sortOrder);

            // 検索結果から取得する項目を定義
            String[] from = {CategoryEntry.COLUMN_NAME_CATEGORY};

            // データを設定するレイアウトのフィールドを定義
            int[] to = {R.id.title};

            // ListViewの1行分のレイアウト(row_main.xml)と検索結果を関連付け
            sc_adapter = new MainListAdapter(
                    this, R.layout.row_category, cursor, from, to,0);

            // activity_main.xmlに定義したListViewオブジェクトを取得
            ListView list = findViewById(R.id.categoryList);

            // ListViewにアダプターを設定
            list.setAdapter(sc_adapter);

            // リストの項目をクリックしたときの処理
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> av, View view, int position, long id) {
                    Cursor cursor = (Cursor) av.getItemAtPosition(position);

                    incrementReferenceCount(id);

                    // DetailActivityへのインテントを作成
                    Intent intent = new Intent(CategoryActivity.this, MainActivity.class);
                    intent.putExtra(CategoryEntry._ID, cursor.getInt(0));
                    intent.putExtra(CategoryEntry.COLUMN_NAME_CATEGORY, cursor.getString(1));


                    // アクティビティを起動
                    startActivity(intent);
                }
            });

        }
    }

    private void incrementReferenceCount(long itemId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(CategoryEntry.TABLE_NAME, new String[]{CategoryEntry.COLUMN_NAME_CATEGORY_REFERENCE_COUNT},
                CategoryEntry._ID + " = ?", new String[]{String.valueOf(itemId)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(CategoryEntry.COLUMN_NAME_CATEGORY_REFERENCE_COUNT);
            if (columnIndex >= 0) {
                int currentCount = cursor.getInt(columnIndex);
                ContentValues values = new ContentValues();
                values.put(CategoryEntry.COLUMN_NAME_CATEGORY_REFERENCE_COUNT, currentCount + 1);

                // 参照回数を更新
                db.update(CategoryEntry.TABLE_NAME, values, CategoryEntry._ID + " = ?", new String[]{String.valueOf(itemId)});
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
            db.delete(CategoryEntry.TABLE_NAME, CategoryEntry._ID+" = ?", new String[] {String.valueOf(id)});
        }

        // データを一覧表示
        on_category_Show();
    }
}