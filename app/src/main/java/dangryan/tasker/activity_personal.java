package dangryan.tasker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class activity_personal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
    }

    //Function to go to the "Classes" page
    public void onClassesButtonClick(View v) {
        Intent intent = new Intent(this, activity_classes.class);
        startActivity(intent);
    }

    //Function to go to the "All" page
    public void onAllButtonClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //Function to go to the "New Task" page
    public void onNewTaskButtonClick(View v) {
        Intent intent = new Intent(this, activity_new_task.class);
        startActivity(intent);
    }

    //Function to go to the "New Category" page
    public void onNewCategoryButtonClick(View v) {
        Intent intent = new Intent(this, activity_new_category.class);
        startActivity(intent);
    }

}
