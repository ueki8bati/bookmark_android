package com.example.bookmark;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import static com.example.bookmark.DBContract.DBEntry;
import static com.example.bookmark.DBContract.CategoryEntry;


public class TextActivity extends AppCompatActivity {
    private int id = 0;
    private int parent_id = 0;
    private String parent_name;

    private EditText editTitle = null;
    private EditText editContents = null;
    private EditText editUrl = null;
    private EditText editTag = null;
    //private EditText editParent = null;
    private TextView editParent = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text2);

        // ビューオブジェクトを取得
        editTitle = findViewById(R.id.editTitle);
        editContents = findViewById(R.id.editContents);
        editUrl = findViewById(R.id.editUrl);
        editTag = findViewById(R.id.editTag);
        editParent = findViewById(R.id.editParent);

        // インテントを取得
        Intent intent = getIntent();

        //intentのデータを取得(データがない場合、第２引数の 0 が返る)
        id = intent.getIntExtra(DBEntry._ID,0);
        String title = intent.getStringExtra(DBEntry.COLUMN_NAME_TITLE);
        String contents = intent.getStringExtra(DBEntry.COLUMN_NAME_CONTENTS);
        String url = intent.getStringExtra(DBEntry.COLUMN_NAME_URL);
        String tags = intent.getStringExtra(DBEntry.COLUMN_NAME_TAGS);
        parent_id = intent.getIntExtra(CategoryEntry._ID, 0);
        parent_name = intent.getStringExtra(CategoryEntry.COLUMN_NAME_CATEGORY);

        //editParent.setText(parent_name);


        // データ更新の場合
        if (id > 0){
            editTitle.setText(title);
            editContents.setText(contents);
            editUrl.setText(url);
            editTag.setText(tags);
            editParent.setText(parent_name);
        }
    }

    // 「登録」ボタン　タップ時に呼び出されるメソッド
    public void btnReg_onClick(View view) {

        // ヘルパーを準備
        SampDatabaseHelper helper = new SampDatabaseHelper(this);

        // 入力欄に入力されたタイトルとコンテンツを取得
        String title    = editTitle.getText().toString();
        String contents = editContents.getText().toString();
        String url = editUrl.getText().toString();
        String tags = editTag.getText().toString();

        // 書き込みモードでデータベースをオープン
        try (SQLiteDatabase db = helper.getWritableDatabase()) {

            // 入力されたタイトルとコンテンツをContentValuesに設定
            // ContentValuesは、項目名と値をセットで保存できるオブジェクト
            ContentValues cv = new ContentValues();
            cv.put(DBEntry.COLUMN_NAME_TITLE, title);
            cv.put(DBEntry.COLUMN_NAME_CONTENTS, contents);
            cv.put(DBEntry.COLUMN_NAME_URL, url);
            cv.put(DBEntry.COLUMN_NAME_TAGS, tags);
            cv.put(DBEntry.COLUMN_NAME_PARENT, parent_id);

            if(id == 0) {
                // データ新規登録
                db.insert(DBEntry.TABLE_NAME, null, cv);
            } else {
                // データ更新
                db.update(DBEntry.TABLE_NAME, cv, DBEntry._ID + " = ?", new String[]{String.valueOf(id)});
            }
        }

        // TextActivityを終了
        finish();
    }

    // 「キャンセル」ボタン　タップ時に呼び出されるメソッド
    public void btnCancel_onClick(View view) {

        // TextActivityを終了
        finish();
    }

}