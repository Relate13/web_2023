package com.example.nju_tube;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.StyledPlayerView;

/** 视频播放页面 */
public class VideoPlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        StyledPlayerView videoView = findViewById(R.id.video_player);
        Player videoPlayer = new ExoPlayer.Builder(this).build();
        videoView.setPlayer(videoPlayer);
        videoView.setBackgroundColor(Color.BLACK);

        Intent intent = getIntent();
        TextView videoTitle = findViewById(R.id.video_info_title);
        videoTitle.setText(intent.getStringExtra("VideoTitle"));
        TextView videoDate = findViewById(R.id.video_info_date);
        videoDate.setText(intent.getStringExtra("VideoDate"));
        TextView videoAuthor = findViewById(R.id.video_info_uploader);
        videoAuthor.setText(intent.getStringExtra("Author"));

        Uri uri = Uri.parse(intent.getStringExtra("VideoURL"));
        videoPlayer.addMediaItem(MediaItem.fromUri(uri));
        videoPlayer.prepare();
    }

}
