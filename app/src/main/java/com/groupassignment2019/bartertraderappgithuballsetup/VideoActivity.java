package com.groupassignment2019.bartertraderappgithuballsetup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

/**expects url string in extras
 *
 */
public class VideoActivity extends AppCompatActivity {
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoView = findViewById(R.id.videoView);

        Intent startingIntent = getIntent();
        if (!startingIntent.hasExtra("url") || startingIntent.getStringExtra("url") == null){
            Toast.makeText(this, "Sorry No Url Has been Provided", Toast.LENGTH_SHORT).show();
            Log.e("BOLO","Sorry no url has been provided ");
            finish();
        }

        Uri uri = Uri.parse(startingIntent.getStringExtra("url"));
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView); // this will attach to the parent rather than to videoview itself?
        videoView.setVideoURI(uri);
        videoView.setMediaController(mediaController);
    }

    @Override
    protected void onStart() {
        super.onStart();
        videoView.start();
    }
}
