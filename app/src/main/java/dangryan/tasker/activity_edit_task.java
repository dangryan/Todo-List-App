package dangryan.tasker;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dangryan.tasker.db.CategoryContract;
import dangryan.tasker.db.TaskContract;
import dangryan.tasker.db.TaskDbHelper;

import static dangryan.tasker.R.id.editTitle;


public class activity_edit_task extends AppCompatActivity {

    private TaskDbHelper mHelper = new TaskDbHelper(this);

    EditText titleEdit;
    Spinner categorySpinner;
    EditText dateEdit;
    EditText addInfoEdit;

    private List<String> categorySpinnerArray = new ArrayList<>();

    Spinner prioritySpin;
    Bundle extras;
    int taskCategoryPos;
    int taskPriorityPos;
    Button pickDate;
    Calendar mCalendar = Calendar.getInstance();
    String myFormat = "MM/dd/yy";
    SimpleDateFormat mDateFormat = new SimpleDateFormat(myFormat);

    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        titleEdit = (EditText) findViewById(R.id.editTitle);
        categorySpinner = (Spinner) findViewById(R.id.categorySelectSpinner);
        dateEdit = (EditText) findViewById(R.id.dateEditText);
        addInfoEdit = (EditText) findViewById(R.id.editAddInfo);
        prioritySpin = (Spinner) findViewById(R.id.prioritySpinner);

        Bundle extras = getIntent().getExtras();

        String taskTitle = extras.getString("task title");
        String taskCategory = extras.getString("task category");
        String taskDue = extras.getString("task due");
        String taskPriority = extras.getString("task priority");
        String taskNotes = extras.getString("task notes");


       /* if (taskCategory.equals("All")){
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
        }*/


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
        dateEdit.setText(taskDue);
        addInfoEdit.setText(taskNotes);
        prioritySpin.setSelection(taskPriorityPos);
        dateEdit = (EditText) findViewById(R.id.dateEditText);
        pickDate = (Button) findViewById(R.id.editDateButton);


        mCalendar = Calendar.getInstance();

        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(activity_edit_task.this, date,
                        mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        setCategorySpinner();

        int i;
        for(i=0; i < categorySpinnerArray.size(); i++) {
            if(taskCategory.equals(categorySpinnerArray.get(i))) {
                categorySpinner.setSelection(i);
            }
        }
    }

    public void setCategorySpinner() {
        categorySpinnerArray.clear();

        SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cursor1 = db.query(TaskContract.CategoryEntry.TABLE,
                new String[]{CategoryContract.CategoryEntry._ID, CategoryContract.CategoryEntry.COL_CATEGORY_NAME},
                null,
                null,
                null,
                null,
                null);

        while (cursor1.moveToNext()) {
            int idx1 = cursor1.getColumnIndex(TaskContract.CategoryEntry.COL_CATEGORY_NAME);
            categorySpinnerArray.add(cursor1.getString(idx1));
        }

        db.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, categorySpinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorySpinner.setAdapter(adapter);
    }

    private void updateLabel(){
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        dateEdit.setText(sdf.format(mCalendar.getTime()));
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

    public void deleteTaskFromEdit(View view) {
        TextView taskTextView = (TextView)findViewById(editTitle);
        String task = taskTextView.getText().toString();
        SQLiteDatabase db = mHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskContract.TaskEntry.COL_TASK_DONE, "True");


        db.update(
                TaskContract.TaskEntry.TABLE,
                contentValues,
                TaskContract.TaskEntry.COL_TASK_TITLE + "= ?",
                new String[]{task});

        db.close();
        finish();
    }
}