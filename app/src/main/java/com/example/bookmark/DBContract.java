package com.example.bookmark;

import android.provider.BaseColumns;

public final class DBContract {

    // 誤ってインスタンス化しないようにコンストラクタをプライベート宣言
    private DBContract() {}

    // テーブルの内容を定義
    public static class DBEntry implements BaseColumns {
        // BaseColumns インターフェースを実装することで、内部クラスは_IDを継承できる
        public static final String TABLE_NAME           = "samp_tbl";
        public static final String COLUMN_NAME_TITLE    = "title";
        public static final String COLUMN_NAME_CONTENTS = "contents";
        public static final String COLUMN_NAME_UPDATE   = "up_date";
        public static final String COLUMN_NAME_REFERENCE_COUNT = "reference_count";
        public static final String COLUMN_NAME_TAGS = "tags";
        public static final String COLUMN_NAME_PARENT = "parent_id";
        public static final String COLUMN_NAME_URL = "url";
    }

    public static class CategoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "category_tbl";
        public static final String COLUMN_NAME_CATEGORY_REFERENCE_COUNT = "category_reference_count";
        public static final String COLUMN_NAME_CATEGORY = "category_name";
    }
}