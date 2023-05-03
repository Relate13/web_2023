package com.example.nju_tube;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

/** 视频播放页面 */
public class VideoPlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        VideoView videoView=findViewById(R.id.video_view);
        String videoPath="android.resource://"+getPackageName() + "/" + R.raw.polar_bear;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        MediaController controller = new MediaController(this);
        videoView.setMediaController(controller);
        controller.setAnchorView(videoView);
    }
}