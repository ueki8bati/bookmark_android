package com.example.bookmark;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import static com.example.bookmark.DBContract.DBEntry;
import static com.example.bookmark.DBContract.CategoryEntry;

public class DetailActivity extends AppCompatActivity {
    private int id = 0;
    private String title;
    private String contents;
    private String url;
    private String tags;
    private int parent_id = 0;

    private String parent_name;

    private TextView edit_Title = null;
    private TextView edit_Contents = null;
    private TextView edit_Tag = null;
    private TextView edit_Parent = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail2);

        edit_Title = findViewById(R.id.edit_Title); // ここを変更
        edit_Contents = findViewById(R.id.edit_Contents); // ここを変更
        edit_Tag = findViewById(R.id.edit_Tag);
        edit_Parent = findViewById(R.id.edit_Parent);

        Intent intent = getIntent();
        id = intent.getIntExtra(DBContract.DBEntry._ID, 0);
        title = intent.getStringExtra(DBContract.DBEntry.COLUMN_NAME_TITLE);
        contents = intent.getStringExtra(DBContract.DBEntry.COLUMN_NAME_CONTENTS);
        url = intent.getStringExtra(DBContract.DBEntry.COLUMN_NAME_URL);
        tags = intent.getStringExtra(DBContract.DBEntry.COLUMN_NAME_TAGS);
        parent_id = intent.getIntExtra(DBContract.CategoryEntry._ID, 0);
        parent_name = intent.getStringExtra(CategoryEntry.COLUMN_NAME_CATEGORY);

        if (id > 0){
            edit_Title.setText(title);
            edit_Contents.setText(contents);
            edit_Tag.setText(tags);
            edit_Parent.setText(parent_name);
        }

        Button btnUrl = findViewById(R.id.btn_url);
        btnUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUrlButtonClicked(v);
            }
        });

        // TODO: Initialize your views here and set up the layout
    }

    public void onUrlButtonClicked(View view) {
        // IntentからURLを取得
        Intent intent = getIntent();
        String url = intent.getStringExtra(DBEntry.COLUMN_NAME_URL); // "URL_KEY"はURLを格納するために使用するキー

        // URLが存在し、有効な場合にブラウザで開く
        if(url != null && !url.isEmpty()){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        }
    }

    // 「編集」ボタンがタップされたときの処理
    public void onEditButtonClicked(View view) {
        // TextActivityに遷移するIntentを作成
        Intent intent = new Intent(this, TextActivity.class);
        // TODO: Add any data you want to pass to TextActivity

        intent.putExtra(DBEntry._ID, id);
        intent.putExtra(DBEntry.COLUMN_NAME_TITLE, title);
        intent.putExtra(DBEntry.COLUMN_NAME_CONTENTS, contents);
        intent.putExtra(DBEntry.COLUMN_NAME_URL, url);
        intent.putExtra(DBEntry.COLUMN_NAME_TAGS, tags);
        intent.putExtra(CategoryEntry._ID, parent_id);
        intent.putExtra(CategoryEntry.COLUMN_NAME_CATEGORY, parent_name);
        //intent.putExtra(DBEntry.COLUMN_NAME_PARENT, parent_id);
        startActivity(intent);
        finish();
    }

    // 「キャンセル」ボタンがタップされたときの処理
    public void onCancelButtonClicked(View view) {
        // DetailActivityを終了
        finish();
    }
}
