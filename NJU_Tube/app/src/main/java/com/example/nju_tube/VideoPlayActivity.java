package com.example.nju_tube;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

/** 视频播放页面 */
public class VideoPlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        VideoView videoView = findViewById(R.id.video_view);

        Intent intent = getIntent();
        TextView videoTitle = findViewById(R.id.video_info_title);
        videoTitle.setText(intent.getStringExtra("VideoTitle"));
        TextView videoDate = findViewById(R.id.video_info_date);
        videoDate.setText(intent.getStringExtra("VideoDate"));
        TextView videoAuthor = findViewById(R.id.video_info_uploader);
        videoAuthor.setText(intent.getStringExtra("Author"));

        Toast.makeText(this, intent.getStringExtra("VideoURL"), Toast.LENGTH_LONG).show();

        String videoPath="android.resource://"+getPackageName() + "/" + R.raw.polar_bear;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        MediaController controller = new MediaController(this);
        videoView.setMediaController(controller);
        controller.setAnchorView(videoView);
    }
}