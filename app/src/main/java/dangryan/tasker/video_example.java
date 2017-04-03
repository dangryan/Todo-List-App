package dangryan.tasker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

public class video_example  extends AppCompatActivity {
    VideoView mVideoView;
    String movieURL = "http://techslides.com/demos/sample-videos/small.mp4";
    MediaController mediaController;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_example);

        mVideoView = (VideoView)findViewById(R.id.videoView);
        mVideoView.setVideoPath(movieURL);

        mediaController = new MediaController(this);
        mediaController.setAnchorView(mVideoView);
        mVideoView.setMediaController(mediaController);

        mVideoView.start();}
}
