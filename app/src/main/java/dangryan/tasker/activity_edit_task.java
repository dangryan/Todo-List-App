package dangryan.tasker;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import dangryan.tasker.db.TaskContract;
import dangryan.tasker.db.TaskDbHelper;


public class activity_edit_task extends AppCompatActivity {

    private TaskDbHelper mHelper = new TaskDbHelper(this);

    EditText titleEdit;
    Spinner categorySpinner;
    EditText dateEdit;
    EditText addInfoEdit;

    Spinner prioritySpin;
    Bundle extras;
    int taskCategoryPos;
    int taskPriorityPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);


        titleEdit = (EditText) findViewById(R.id.editTitle);
        categorySpinner = (Spinner) findViewById(R.id.editCategory);
        dateEdit = (EditText) findViewById(R.id.editDate);
        addInfoEdit = (EditText) findViewById(R.id.editAddInfo);

        prioritySpin = (Spinner) findViewById(R.id.prioritySpinner);


        Bundle extras = getIntent().getExtras();

        String taskTitle = extras.getString("task title");
        String taskCategory = extras.getString("task category");
        String taskDue = extras.getString("task due");
        String taskPriority = extras.getString("task priority");
        String taskNotes = extras.getString("task notes");



        if (taskCategory.equals("All")){
            taskCategoryPos = 0;
        }
        if (taskCategory.equals("School")){
            taskCategoryPos = 1;
        }
        if (taskCategory.equals("Work")){
            taskCategoryPos = 2;
        }
        if (taskCategory.equals("Personal")){
            taskCategoryPos = 3;
        }



        if (taskPriority.equals("Low")){
            taskPriorityPos = 0;
        }
        if (taskPriority.equals("Medium")){
            taskPriorityPos = 1;
        }
        if (taskPriority.equals("High")){
            taskPriorityPos = 2;
        }


        titleEdit.setText(taskTitle);
        categorySpinner.setSelection(taskCategoryPos);
        dateEdit.setText(taskDue);
        addInfoEdit.setText(taskNotes);
        prioritySpin.setSelection(taskPriorityPos);
    }


    public void onSaveButtonClickEdit(View view) {

        Bundle extras = getIntent().getExtras();
        String taskTitleOld = extras.getString("task title");

        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        String taskAddInfo = String.valueOf(addInfoEdit.getText());
        String taskDate = String.valueOf(dateEdit.getText());
        String taskCategory = String.valueOf(categorySpinner.getSelectedItem().toString());
        String taskTitle = String.valueOf(titleEdit.getText());
        String taskRating = prioritySpin.getSelectedItem().toString();

        values.put(TaskContract.TaskEntry.COL_TASK_TITLE, taskTitle);
        values.put(TaskContract.TaskEntry.COL_TASK_CATEGORY, taskCategory);
        values.put(TaskContract.TaskEntry.COL_TASK_DUE, taskDate);
        values.put(TaskContract.TaskEntry.COL_TASK_NOTES, taskAddInfo);
        values.put(TaskContract.TaskEntry.COL_TASK_PRIORITY, taskRating);


        db.update(
                TaskContract.TaskEntry.TABLE,
                values,
                TaskContract.TaskEntry.COL_TASK_TITLE + "= '" + taskTitleOld + "'",
                null);

        /*db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);*/
        db.close();
        Toast toast = Toast.makeText(this, "Task updated!", Toast.LENGTH_LONG);
        toast.show();
    }
}