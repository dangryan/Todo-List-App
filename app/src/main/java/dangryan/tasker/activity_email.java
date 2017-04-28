package dangryan.tasker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class activity_email extends AppCompatActivity {
    EditText emailEdit;
    Button okayButton;
    String email = "";
    String date;
    String taskTitle;
    String taskBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        emailEdit = (EditText)findViewById(R.id.emailEditText);
        okayButton = (Button)findViewById(R.id.okayButton);

        Intent intent = getIntent();
        date = intent.getStringExtra("date");
        taskTitle = intent.getStringExtra("title");
        taskBody = intent.getStringExtra("body");

    }

    public void  onOkayClick(View view){
        email = emailEdit.getText().toString();

        if (email != ("")){
            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Task scheduled for " + date);
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "The task is: " + taskTitle + "\n\n Task information: " + taskBody);

            emailIntent.setType("message/rfc822");

            try {
                startActivity(Intent.createChooser(emailIntent, "Send email using..."));
            }
            catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "No email clients installed.", Toast.LENGTH_SHORT).show();}
        }
        else{
            Toast.makeText(this,"Please enter an email.", Toast.LENGTH_LONG).show();
        }
    }
}
