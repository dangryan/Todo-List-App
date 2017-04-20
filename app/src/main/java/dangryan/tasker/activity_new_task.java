package dangryan.tasker;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dangryan.tasker.alarms.MyBroadcastReceiver;
import dangryan.tasker.alarms.Receiver;
import dangryan.tasker.db.CategoryContract;
import dangryan.tasker.db.TaskContract;
import dangryan.tasker.db.TaskDbHelper;


public class activity_new_task extends AppCompatActivity {

    private TaskDbHelper mHelper;

    EditText titleEdit;
    EditText dateEdit;
    EditText addInfoEdit;
    Spinner prioritySpin;
    Spinner categorySpin;
    Button pickDate;
    Calendar mCalendar = Calendar.getInstance();
    String myFormat = "MM/dd/yy";
    SimpleDateFormat mDateFormat = new SimpleDateFormat(myFormat);
    String dateChosen = mDateFormat.format(mCalendar.getTime());
    private List<String> categorySpinnerArray = new ArrayList<>();
    AlarmManager am;
    PendingIntent pendingIntent;

    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            updateLabel();
            dateChosen = mDateFormat.format(mCalendar.getTime());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        mHelper = new TaskDbHelper(this);

        titleEdit = (EditText) findViewById(R.id.editTitle);
        categorySpin = (Spinner) findViewById(R.id.categorySelectSpinner);
        dateEdit = (EditText) findViewById(R.id.dateEditText);
        addInfoEdit = (EditText) findViewById(R.id.editAddInfo);
        pickDate = (Button) findViewById(R.id.editDateButton);
        prioritySpin = (Spinner) findViewById(R.id.prioritySpinner);

        setCategorySpinner();

        updateLabel();

        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(activity_new_task.this, date, mCalendar
                        .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat mDateFormat = new SimpleDateFormat(myFormat);

        dateEdit.setText(mDateFormat.format(mCalendar.getTime()));
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
        cursor1.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, categorySpinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorySpin.setAdapter(adapter);
    }

    public void onSaveButtonClick(View view) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        String taskAddInfo = String.valueOf(addInfoEdit.getText());
        String taskDate = String.valueOf(dateEdit.getText());

        String taskCategory = "";

        if (categorySpin.getSelectedItem() != null) {
           taskCategory = categorySpin.getSelectedItem().toString();
        }

        String taskRating = prioritySpin.getSelectedItem().toString();
        final String taskTitle = String.valueOf(titleEdit.getText());


        if (taskTitle.matches("") || taskCategory.matches("")) {
            Toast.makeText(this, "Please enter the required fields.", Toast.LENGTH_LONG).show();
        } else {
            values.put(TaskContract.TaskEntry.COL_TASK_TITLE, taskTitle);
            values.put(TaskContract.TaskEntry.COL_TASK_CATEGORY, taskCategory);
            values.put(TaskContract.TaskEntry.COL_TASK_DUE, dateChosen);
            values.put(TaskContract.TaskEntry.COL_TASK_NOTES, taskAddInfo);
            values.put(TaskContract.TaskEntry.COL_TASK_PRIORITY, taskRating);
            values.put(TaskContract.TaskEntry.COL_TASK_DONE, "False");


            db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                    null,
                    values,
                    SQLiteDatabase.CONFLICT_REPLACE);

            db.close();

            //toast creation
            Toast toast = Toast.makeText(this, "Task Added!", Toast.LENGTH_LONG);
            toast.show();

            finish();
        }
    }
}