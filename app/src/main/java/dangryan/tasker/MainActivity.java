package dangryan.tasker;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import dangryan.tasker.db.TaskContract;
import dangryan.tasker.db.TaskDbHelper;

import static dangryan.tasker.R.id.checkBox;
import static dangryan.tasker.R.id.taskDateView;
import static dangryan.tasker.R.id.taskNameLabel;
import static dangryan.tasker.R.layout.activity_all;
import static dangryan.tasker.R.layout.activity_personal;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TaskDbHelper mHelper;
    private ListView mTaskListView;
    private ArrayAdapter<String> mAdapter;
    private ArrayAdapter<String> mAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all);

        mHelper = new TaskDbHelper(this);
        mTaskListView = (ListView) findViewById(R.id.task_todo);
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            Log.d(TAG, "Task: " + cursor.getString(idx));
        }
        cursor.close();
        db.close();
        updateUI();
    }


    @Override
    protected void onResume(){
        super.onResume();
        updateUI();
    }
/*
    //Function to go to the "Personal" page
    public void onPersonalButtonClick(View v) {
        Intent intent = new Intent(this, activity_personal.class);
        startActivity(intent);
    }

    //Function to go to the "Classes" page
    public void onClassesButtonClick(View v) {
        Intent intent = new Intent(this, activity_classes.class);
        startActivity(intent);
    }
    */

    //Function to go to the "New Task" page
    public void onNewTaskButtonClick(View v) {
        Intent intent = new Intent(this, activity_new_task.class);
        startActivity(intent);
    }

    /*
        //Function to go to the "New Category" page
         public void onNewCategoryButtonClick(View v) {
            Intent intent = new Intent(this, activity_new_category.class);
            startActivity(intent);
        }
    /*/
    private void updateTaskTitle() {
        //section to update the Title on the Checkbox
        ArrayList<String> taskNameList = new ArrayList<>();

        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry.COL_TASK_TITLE, TaskContract.TaskEntry.COL_TASK_DUE},
                null, null, null, null, null);


        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            taskNameList.add(cursor.getString(idx));
        }

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(this,
                    R.layout.task_todo,
                    //adds data to TextView
                    R.id.taskNameLabel,
                    taskNameList);
            mTaskListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(taskNameList);
            mAdapter.notifyDataSetChanged();
        }
        cursor.close();
        db.close();
    }


    private void updateTaskDate() {
        //section to update the Title on the Checkbox
        ArrayList<String> taskDateList = new ArrayList<>();


        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor2 = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry.COL_TASK_TITLE, TaskContract.TaskEntry.COL_TASK_DUE},
                null, null, null, null, null);


        while (cursor2.moveToNext()) {
            int idx = cursor2.getColumnIndex(TaskContract.TaskEntry.COL_TASK_DUE);
            taskDateList.add(cursor2.getString(idx));
        }

        if (mAdapter2 == null) {
            mAdapter2 = new ArrayAdapter<>(this,
                    R.layout.task_todo,
                    //adds data to TextView
                    R.id.taskDateView,
                    taskDateList);
            mTaskListView.setAdapter(mAdapter2);
        } else {
            mAdapter2.clear();
            mAdapter2.addAll(taskDateList);
            mAdapter2.notifyDataSetChanged();
        }
        cursor2.close();
        db.close();
    }

    private void updateUI(){
        updateTaskDate();
        updateTaskTitle();
    }


    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(taskNameLabel);
        String task = taskTextView.getText().toString();
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(TaskContract.TaskEntry.TABLE,
                TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
                new String[]{task});
        db.close();
        updateUI();
    }


    public void editTask(View view) {
        Intent intent = new Intent(MainActivity.this, activity_edit_task.class);
        //intent.putExtra("",);
        startActivity(intent);
    }

}
