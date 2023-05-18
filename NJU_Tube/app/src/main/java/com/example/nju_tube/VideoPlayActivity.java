package com.example.nju_tube;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

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

        Uri uri = Uri.parse(intent.getStringExtra("VideoURL"));
        videoView.setVideoURI(uri);

        // 视频未播放时展示视频封面
        ImageView coverView = findViewById(R.id.cover_image_view);
        Glide.with(this).load(intent.getStringExtra("CoverURL")).into(coverView);
        videoView.setOnPreparedListener(mp -> mp.setOnInfoListener((mp1, what, extra) -> {
            //播放第一帧时设置图片消失
            if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START)
                coverView.setVisibility(View.GONE);
            return true;
        }));

        MediaController controller = new MediaController(this);
        videoView.setMediaController(controller);
        controller.setAnchorView(videoView);
    }
}
