package dangryan.tasker;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dangryan.tasker.db.TaskContract;
import dangryan.tasker.db.TaskDbHelper;


public class activity_new_category extends AppCompatActivity {
    public List<String> categories = new ArrayList<>();
    EditText categoryEdit;
    private TaskDbHelper mHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);

        categoryEdit = (EditText)findViewById(R.id.categoryEditText);
    }

    public void onNewCategorySaveButtonClick(View view) {
        String newCategory = categoryEdit.getText().toString();
        mHelper = new TaskDbHelper(this);
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TaskContract.TaskEntry.COL_TASK_TITLE, "null");
        values.put(TaskContract.TaskEntry.COL_TASK_CATEGORY, newCategory);
        values.put(TaskContract.TaskEntry.COL_TASK_DUE, "null");
        values.put(TaskContract.TaskEntry.COL_TASK_NOTES, "null");
        values.put(TaskContract.TaskEntry.COL_TASK_PRIORITY, "null");
        values.put(TaskContract.TaskEntry.COL_TASK_DONE, "null");


        db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);

        db.close();
        Toast toast = Toast.makeText(this, "Category Added!", Toast.LENGTH_LONG);
        toast.show();
    }
}



    /*public void setCategorySpinner(){
        categorySpinnerArray.clear();

        SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cursor1 = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_CATEGORY},
                TaskContract.TaskEntry.COL_TASK_DONE + "= 'False'",
                null,
                null,
                null,
                null);

        while (cursor1.moveToNext()){
            int idx1 = cursor1.getColumnIndex(TaskContract.TaskEntry.COL_TASK_CATEGORY);
            categorySpinnerArray.add(cursor1.getString(idx1));
        }
        db.close();
        updateUI();
        categorySpin = (Spinner)findViewById(R.id.categorySpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, categorySpinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorySpin.setAdapter(adapter);
    }*/