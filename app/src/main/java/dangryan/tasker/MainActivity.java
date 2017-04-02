package dangryan.tasker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import dangryan.tasker.db.TaskContract;
import dangryan.tasker.db.TaskDbHelper;

import static dangryan.tasker.R.id.taskNameLabel;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TaskDbHelper mHelper;
    private ListView mTaskListView;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all);

        mHelper = new TaskDbHelper(this);
        mTaskListView = (ListView) findViewById(R.id.task_todo_list);
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

    //Function to go to the "New Task" page
    public void onNewTaskButtonClick(View v) {
        Intent intent = new Intent(this, activity_new_task.class);
        startActivity(intent);
    }


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


    private void updateUI(){
        updateTaskTitle();
        //updateTaskDate();
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
        //updateUI();
    }


    public void editTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(taskNameLabel);
        String taskName = taskTextView.getText().toString();
        SQLiteDatabase db = mHelper.getWritableDatabase();

        String table = TaskContract.TaskEntry.TABLE;
        String[] columns = new String[] { "title", "due", "category", "priority", "notes" };
        String selection = TaskContract.TaskEntry.COL_TASK_TITLE+ "= '" + taskName + "'";
        String[] selectionArgs = null;
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = null;

        Cursor cursor2 = db.query(table, columns, selection,selectionArgs,groupBy,having,orderBy,limit);

        cursor2.moveToFirst();

        String taskTitle = cursor2.getString(cursor2.getColumnIndex("title"));
        String taskDue = cursor2.getString(cursor2.getColumnIndex("due"));
        String taskCategory = cursor2.getString(cursor2.getColumnIndex("category"));
        String taskPriority = cursor2.getString(cursor2.getColumnIndex("priority"));
        String taskNotes = cursor2.getString(cursor2.getColumnIndex("notes"));

        db.close();

        Intent intent = new Intent(MainActivity.this, activity_edit_task.class);

        intent.putExtra("task title", taskTitle);
        intent.putExtra("task due", taskDue);
        intent.putExtra("task category", taskCategory);
        intent.putExtra("task priority", taskPriority);
        intent.putExtra("task notes", taskNotes);

        startActivity(intent);
    }



    public void switchPage(View view){
        Spinner categoryDropdown = (Spinner)findViewById(R.id.categorySpinner);
        String categoryChoice = categoryDropdown.getSelectedItem().toString();
        TextView test = (TextView)findViewById(R.id.choiceView);
        test.setText(categoryChoice);

        if (categoryChoice.equals("All")){
            Toast.makeText(getBaseContext(),"Current page selected", Toast.LENGTH_LONG);
        }
        if (categoryChoice.equals("School")){
            Intent intent = new Intent(MainActivity.this, activity_school.class);
            startActivity(intent);
        }
        if (categoryChoice.equals("Work")){
            Intent intent = new Intent(MainActivity.this, activity_work.class);
            startActivity(intent);

        }
        if (categoryChoice.equals("Personal")){
            Intent intent = new Intent(MainActivity.this, activity_personal.class);
            startActivity(intent);

        }

    }

}
