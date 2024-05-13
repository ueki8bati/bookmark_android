package com.example.bookmark;

import androidx.appcompat.app.AppCompatActivity;
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
import android.os.Bundle;

public class CategoryTextActivity extends AppCompatActivity {

    private int id = 0;


    private EditText editTitle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_text);

        editTitle = findViewById(R.id.editTitle);


    }

    // 「登録」ボタン　タップ時に呼び出されるメソッド
    public void btnReg_onClick(View view) {

        // ヘルパーを準備
        SampDatabaseHelper helper = new SampDatabaseHelper(this);

        // 入力欄に入力されたタイトルとコンテンツを取得
        String category = editTitle.getText().toString();


        // 書き込みモードでデータベースをオープン
        try (SQLiteDatabase db = helper.getWritableDatabase()) {

            // 入力されたタイトルとコンテンツをContentValuesに設定
            // ContentValuesは、項目名と値をセットで保存できるオブジェクト
            ContentValues cv = new ContentValues();
            cv.put(CategoryEntry.COLUMN_NAME_CATEGORY, category);


            if(id == 0) {
                // データ新規登録
                db.insert(CategoryEntry.TABLE_NAME, null, cv);
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
