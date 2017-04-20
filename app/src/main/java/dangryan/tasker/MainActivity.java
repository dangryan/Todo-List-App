package dangryan.tasker;

import android.content.ContentValues;
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
import java.util.List;

import dangryan.tasker.db.CategoryContract;
import dangryan.tasker.db.CategoryDbHelper;
import dangryan.tasker.db.TaskContract;
import dangryan.tasker.db.TaskDbHelper;

import static dangryan.tasker.R.id.taskNameLabel;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TaskDbHelper mHelper;
    private CategoryDbHelper mHelper2;
    private ListView mTaskListView;
    private ArrayAdapter<String> mAdapter;
    private List<String> categorySpinnerArray = new ArrayList<>();
    Spinner categorySpin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all);

        mHelper = new TaskDbHelper(this);
        mHelper2 = new CategoryDbHelper(this);

        mTaskListView = (ListView) findViewById(R.id.task_todo_list);
        SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
                TaskContract.TaskEntry.COL_TASK_DONE + "= 'False'" + " AND " + TaskContract.TaskEntry.COL_TASK_TITLE + " IS NOT NULL ORDER BY due ASC",
                null,
                null,
                null,
                null);

        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            Log.d(TAG, "Task: " + cursor.getString(idx));
        }
        cursor.close();
        updateUI();
    }

    @Override
    protected void onResume(){
        super.onResume();
        updateUI();
    }

    public void setCategorySpinner(){
        categorySpinnerArray.clear();

        SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cursor1 = db.query(TaskContract.CategoryEntry.TABLE,
                new String[]{CategoryContract.CategoryEntry._ID, CategoryContract.CategoryEntry.COL_CATEGORY_NAME},
                null,
                null,
                null,
                null,
                null);

        categorySpinnerArray.add("All");

        while (cursor1.moveToNext()){
            int idx1 = cursor1.getColumnIndex(TaskContract.CategoryEntry.COL_CATEGORY_NAME);
            categorySpinnerArray.add(cursor1.getString(idx1));
        }
        //categorySpinnerArray.add("Photo Example");
        //categorySpinnerArray.add("Video Example");
        categorySpinnerArray.add("Maps Example");

        categorySpinnerArray.add("Completed");


        db.close();
        cursor1.close();

        categorySpin = (Spinner)findViewById(R.id.categorySelectSpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, categorySpinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorySpin.setAdapter(adapter);
    }

    public void onNewTaskButtonClick(View v) {
        Intent intent = new Intent(this, activity_new_task.class);
        startActivity(intent);
    }

    public void onNewCategoryButtonClick(View v) {
        Intent intent = new Intent(this, activity_new_category.class);
        startActivity(intent);
    }

    private void updateTaskTitle() {
        //section to update the Title on the Checkbox
        ArrayList<String> taskNameList = new ArrayList<>();

        SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cursor = db.query(
                TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry.COL_TASK_TITLE, TaskContract.TaskEntry.COL_TASK_DUE},
                TaskContract.TaskEntry.COL_TASK_DONE + "= 'False'" + " AND " + TaskContract.TaskEntry.COL_TASK_TITLE + " IS NOT NULL ORDER BY due ASC",
                null,
                null,
                null,
                null);


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
        setCategorySpinner();
    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(taskNameLabel);
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
        updateUI();
    }

    public void editTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(taskNameLabel);
        String taskName = taskTextView.getText().toString();
        SQLiteDatabase db = mHelper.getWritableDatabase();

        String table = TaskContract.TaskEntry.TABLE;
        String[] columns = new String[] {TaskContract.TaskEntry._ID, "title", "due", "category", "priority", "notes" };
        String selection = TaskContract.TaskEntry.COL_TASK_TITLE+ "= '" + taskName + "'";


        Cursor cursor2 = db.query(table, columns, selection,null,null,null,null,null);

        cursor2.moveToFirst();

        String tid = cursor2.getString(cursor2.getColumnIndex(TaskContract.TaskEntry._ID));
        String taskTitle = cursor2.getString(cursor2.getColumnIndex("title"));
        String taskDue = cursor2.getString(cursor2.getColumnIndex("due"));
        String taskCategory = cursor2.getString(cursor2.getColumnIndex("category"));
        String taskPriority = cursor2.getString(cursor2.getColumnIndex("priority"));
        String taskNotes = cursor2.getString(cursor2.getColumnIndex("notes"));

        db.close();
        cursor2.close();

        Intent intent = new Intent(MainActivity.this, activity_edit_task.class);

        intent.putExtra("tid", tid);
        intent.putExtra("task title", taskTitle);
        intent.putExtra("task due", taskDue);
        intent.putExtra("task category", taskCategory);
        intent.putExtra("task priority", taskPriority);
        intent.putExtra("task notes", taskNotes);

        startActivity(intent);
    }

    public void switchPage(View view){
        Spinner categoryDropdown = (Spinner)findViewById(R.id.categorySelectSpinner);
        String categoryChoice = categoryDropdown.getSelectedItem().toString();


        if (categoryChoice.equals("All")){
            Toast.makeText(getBaseContext(),"Current page selected", Toast.LENGTH_LONG).show();
        }
        else if (categoryChoice.equals("Photo Example")){
            Intent intent = new Intent(MainActivity.this, photo_example.class);
            startActivity(intent);
        }
        else if (categoryChoice.equals("Video Example")){
            Intent intent = new Intent(MainActivity.this, video_example.class);
            startActivity(intent);
        }
        else if (categoryChoice.equals("Maps Example")){
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intent);
        }
        else if (categoryChoice.equals("Completed")){
            Intent intent = new Intent(MainActivity.this, activity_completed.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(MainActivity.this, activity_category.class);
            intent.putExtra("categoryName" , categoryChoice);
            startActivity(intent);
        }

    }

}
