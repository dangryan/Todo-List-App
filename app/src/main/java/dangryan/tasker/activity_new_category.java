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

import dangryan.tasker.db.CategoryContract;
import dangryan.tasker.db.CategoryDbHelper;
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
        mHelper = new TaskDbHelper(this);
        String newCategory = categoryEdit.getText().toString();

        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TaskContract.CategoryEntry.COL_CATEGORY_NAME, newCategory);

        db.insertWithOnConflict(TaskContract.CategoryEntry.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);

        db.close();
        Toast toast = Toast.makeText(this, "Category Added!", Toast.LENGTH_LONG);
        toast.show();
        finish();
    }
}