package dangryan.tasker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dangryan.tasker.db.TaskContract;
import dangryan.tasker.db.TaskDbHelper;


public class activity_new_task extends AppCompatActivity {

    private TaskDbHelper mHelper;

    EditText titleEdit;
    EditText dateEdit;
    EditText addInfoEdit;
    Spinner prioritySpin;
    Spinner categorySpin;
    private List<String> categorySpinnerArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        mHelper = new TaskDbHelper(this);


        titleEdit = (EditText) findViewById(R.id.editTitle);
        categorySpin = (Spinner) findViewById(R.id.editCategory);
        dateEdit = (EditText) findViewById(R.id.editDate);
        addInfoEdit = (EditText) findViewById(R.id.editAddInfo);

        prioritySpin = (Spinner) findViewById(R.id.prioritySpinner);
        //setCategorySpinner();
    }

    /*public void setCategorySpinner(){
        categorySpinnerArray.clear();

        SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cursor1 = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_CATEGORY},
                null,
                null,
                null,
                null,
                null);

        while (cursor1.moveToNext()){
            int idx1 = cursor1.getColumnIndex(TaskContract.TaskEntry.COL_TASK_CATEGORY);
            categorySpinnerArray.add(cursor1.getString(idx1));
        }

        db.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, categorySpinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorySpin.setAdapter(adapter);
    }*/

    public void onSaveButtonClick(View view) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        String taskAddInfo = String.valueOf(addInfoEdit.getText());
        String taskDate = String.valueOf(dateEdit.getText());
        String taskCategory = categorySpin.getSelectedItem().toString();
        String taskTitle = String.valueOf(titleEdit.getText());

        String taskRating = prioritySpin.getSelectedItem().toString();


        values.put(TaskContract.TaskEntry.COL_TASK_TITLE, taskTitle);
        values.put(TaskContract.TaskEntry.COL_TASK_CATEGORY, taskCategory);
        values.put(TaskContract.TaskEntry.COL_TASK_DUE, taskDate);
        values.put(TaskContract.TaskEntry.COL_TASK_NOTES, taskAddInfo);
        values.put(TaskContract.TaskEntry.COL_TASK_PRIORITY, taskRating);
        values.put(TaskContract.TaskEntry.COL_TASK_DONE, "False");


        db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);

        db.close();
        Toast toast = Toast.makeText(this, "Task Added!", Toast.LENGTH_LONG);
        toast.show();
    }
}