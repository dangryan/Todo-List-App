package dangryan.tasker;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class photo_example extends AppCompatActivity {
    private static Button mCaptureButton;
    private static TextView mErrorView;
    private static ImageView mImage;
    private static final int MEDIA_TYPE_IMAGE = 1;
    private static final int IMAGE_CAPTURE = 102;
    private static Uri fileUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_example);

        mCaptureButton = (Button)findViewById(R.id.captureButton);
        mErrorView = (TextView)findViewById(R.id.textView);
        mImage = (ImageView)findViewById(R.id.imageView);

        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            mCaptureButton.setEnabled(true);
        }
        else{
            mCaptureButton.setEnabled(false);
        }

        mErrorView.setText("state: " + Environment.getExternalStorageState());
    }

    private static  Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {
        File myDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");


        if (!myDir.exists()) {
            if (!myDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        String myTimeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File myMediaFile;
        if(type == MEDIA_TYPE_IMAGE){
            myMediaFile = new File(myDir.getPath() + File.separator + "IMG_" + myTimeStamp + ".jpg");
        }
        else{
            return null;
        }
        return myMediaFile;
    }



    public void onCaptureClick(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        mErrorView.setText(mErrorView.getText() + "\nfileURI: " + fileUri.getPath());

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        startActivityForResult(intent,IMAGE_CAPTURE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == IMAGE_CAPTURE){
            if(resultCode == RESULT_OK){
                mImage.setImageURI(fileUri);
                Toast.makeText(this, "Image returned", Toast.LENGTH_LONG).show();
            }
            else if (resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Capture Canceled", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this, "Failed to capture image", Toast.LENGTH_LONG).show();

            }
        }
    }

}
