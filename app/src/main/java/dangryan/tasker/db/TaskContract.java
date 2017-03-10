package dangryan.tasker.db;

import android.provider.BaseColumns;

public class TaskContract {
    public static final String DB_NAME = "com.dangryan.tasker.db";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE = "tasks";

        public static final String COL_TASK_TITLE = "title";

        public static final String COL_TASK_CATEGORY = "category";
        public static final String COL_TASK_DUE = "due";
        public static final String COL_TASK_PRIORITY = "priority";
        public static final String COL_TASK_NOTES = "notes";
    }
}