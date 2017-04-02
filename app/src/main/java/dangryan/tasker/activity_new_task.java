package dangryan.tasker;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import dangryan.tasker.db.TaskContract;
import dangryan.tasker.db.TaskDbHelper;


public class activity_new_task extends AppCompatActivity {

    private TaskDbHelper mHelper;

    EditText titleEdit;
    Spinner categoryEdit;
    EditText dateEdit;
    EditText addInfoEdit;

    RatingBar mRatingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        mHelper = new TaskDbHelper(this);


        titleEdit = (EditText) findViewById(R.id.editTitle);
        categoryEdit = (Spinner) findViewById(R.id.editCategory);
        dateEdit = (EditText) findViewById(R.id.editDate);
        addInfoEdit = (EditText) findViewById(R.id.editAddInfo);

        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
    }

    public void onSaveButtonClick(View view) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        String taskAddInfo = String.valueOf(addInfoEdit.getText());
        String taskDate = String.valueOf(dateEdit.getText());
        String taskCategory = categoryEdit.getSelectedItem().toString();
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