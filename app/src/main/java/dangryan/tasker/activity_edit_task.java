package dangryan.tasker;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import dangryan.tasker.db.TaskContract;
import dangryan.tasker.db.TaskDbHelper;


public class activity_edit_task extends AppCompatActivity {

    private TaskDbHelper mHelper;

    EditText titleEdit;
    EditText categoryEdit;
    EditText dateEdit;
    EditText addInfoEdit;

    RatingBar mRatingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        //Intent

        //mHelper = new TaskDbHelper(this);


        titleEdit = (EditText) findViewById(R.id.editTitle_2);
        categoryEdit = (EditText) findViewById(R.id.editCategory_2);
        dateEdit = (EditText) findViewById(R.id.editDate_2);
        addInfoEdit = (EditText) findViewById(R.id.editAddInfo_2);

        mRatingBar = (RatingBar) findViewById(R.id.ratingBar_2);
    }


    public void onSaveButtonClick(View view) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();


        String taskAddInfo = String.valueOf(addInfoEdit.getText());
        String taskDate = String.valueOf(dateEdit.getText());
        String taskCategory = String.valueOf(categoryEdit.getText());
        String taskTitle = String.valueOf(titleEdit.getText());

        int taskRating = mRatingBar.getNumStars();


        values.put(TaskContract.TaskEntry.COL_TASK_TITLE, taskTitle);
        values.put(TaskContract.TaskEntry.COL_TASK_CATEGORY, taskCategory);
        values.put(TaskContract.TaskEntry.COL_TASK_DUE, taskDate);
        values.put(TaskContract.TaskEntry.COL_TASK_NOTES, taskAddInfo);
        values.put(TaskContract.TaskEntry.COL_TASK_PRIORITY, taskRating);


        db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        Toast toast = Toast.makeText(this, "Task Added!", Toast.LENGTH_LONG);
        toast.show();
    }
}