package dangryan.tasker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import static dangryan.tasker.R.layout.activity_all;
import static dangryan.tasker.R.layout.activity_personal;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all);
    }

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
