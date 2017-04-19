package dangryan.tasker.db;

import android.provider.BaseColumns;

public class CategoryContract {
    public static final String DB_NAME = "com.dangryan.tasker.db";
    public static final int DB_VERSION = 1;

    public class CategoryEntry implements BaseColumns {
        public static final String TABLE = "category";

        public static final String COL_CATEGORY_NAME = "categoryName";
    }
}