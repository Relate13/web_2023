package com.example.nju_tube;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.nju_tube.fragment.CommentFragment;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * 视频播放页面
 */
public class VideoPlayActivity extends AppCompatActivity {
    int vid;
    TextView commentCounter;
    ExoPlayer videoPlayer;
    Uri videoURL;
    boolean likedThisVideo;

    FloatingActionButton likeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video_play);
        Intent intent = getIntent();
        initVideoInfo(intent);

        videoURL = Uri.parse(intent.getStringExtra("VideoURL"));

        vid = intent.getIntExtra("VideoID", 0);
        commentCounter = findViewById(R.id.comment_counter);
        initComment();
    }

    @Override
    public void onStart() {
        super.onStart();
        initPlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        videoPlayer.release();
        videoPlayer = null;
    }

    private void initVideoInfo(Intent intent) {
        TextView videoTitle = findViewById(R.id.video_info_title);
        videoTitle.setText(intent.getStringExtra("VideoTitle"));
        TextView videoDate = findViewById(R.id.video_info_date);
        videoDate.setText(intent.getStringExtra("VideoDate"));
        TextView videoAuthor = findViewById(R.id.video_info_uploader);
        videoAuthor.setText(intent.getStringExtra("Author"));

        likeButton = findViewById(R.id.like);

        likedThisVideo = intent.getBooleanExtra("likedThisVideo", false);
        // init like status here
        updateLikeStatusFromServer();

        likeButton.setOnClickListener(v -> {
            if (likedThisVideo) {
                likeButton.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                unlikeVideo();
                likedThisVideo = false;
            } else {
                likeButton.setImageResource(R.drawable.ic_baseline_favorite_24);
                likeVideo();
                likedThisVideo = true;
            }
        });
    }

    private void updateLikeButton() {
        if (likedThisVideo)
            likeButton.setImageResource(R.drawable.ic_baseline_favorite_24);
        else
            likeButton.setImageResource(R.drawable.ic_baseline_favorite_border_24);
    }

    private void initPlayer() {
        StyledPlayerView videoView = findViewById(R.id.video_player_view);
        videoPlayer = new ExoPlayer.Builder(this).build();
        videoView.setPlayer(videoPlayer);
        videoView.setBackgroundColor(Color.BLACK);
        videoView.setControllerAutoShow(false);
        videoPlayer.addMediaItem(MediaItem.fromUri(videoURL));
        videoPlayer.prepare();
    }

    private void initComment() {
        setCommentFragment(new CommentFragment(vid, commentCounter));

        FloatingActionButton commentBtn = findViewById(R.id.new_comment);
        commentBtn.setOnClickListener(v -> {
            final EditText input = new EditText(this);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.comment_title))
                    .setView(input)
                    .setPositiveButton(getString(R.string.comment_sure), (dialog, id) -> {
                        String comment = input.getText().toString();
                        if (comment.isBlank()) {
                            Toast.makeText(this, getString(R.string.no_blank_comment), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Handler mainHandler = new Handler();
                        Thread commentThread = new Thread(() ->
                                sendComment(vid, comment, mainHandler));
                        commentThread.start();
                    })
                    .setNegativeButton(getString(R.string.comment_cancel), (dialog, id) -> {
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void sendComment(int vid, String comment, Handler mainHandler) {
        String commentURL = ((NJUTube) getApplication()).getServerURL() + getString(R.string.comment_action);
        try {
            HttpPostMultipart httpPostMultipart = new HttpPostMultipart(commentURL, "utf-8");
            httpPostMultipart.addFormField("token", ((NJUTube) getApplication()).getToken());
            httpPostMultipart.addFormField("video_id", String.valueOf(vid));
            httpPostMultipart.addFormField("action_type", "1");
            httpPostMultipart.addFormField("comment_text", comment);
            JSONObject jsonObject = new JSONObject(httpPostMultipart.finish());
            int statusCode = jsonObject.getJSONObject("response").getInt("status_code");
            if (statusCode == 0) {
                setCommentFragment(new CommentFragment(vid, commentCounter));
                mainHandler.post(() -> Toast.makeText(this, getString(R.string.comment_success), Toast.LENGTH_SHORT).show());
            } else {
                mainHandler.post(() -> Toast.makeText(this, getString(R.string.comment_fail), Toast.LENGTH_SHORT).show());
            }
        } catch (IOException | JSONException ignored) {
            mainHandler.post(() -> Toast.makeText(this, getString(R.string.comment_fail), Toast.LENGTH_SHORT).show());
        }
    }

    private void setCommentFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.comment_list_frame, fragment);
        fragmentTransaction.commit();
    }

    private void unlikeVideo() {
        Thread unlikeThread = new Thread(() -> sendLike(false));
        unlikeThread.start();
        Toast.makeText(this, "取消点赞", Toast.LENGTH_SHORT).show();
    }

    private void likeVideo() {
        Thread likeThread = new Thread(() -> sendLike(true));
        likeThread.start();
        Toast.makeText(this, "赞了这个视频", Toast.LENGTH_SHORT).show();
    }

    private void sendLike(boolean doLike) {
        String likeURL = ((NJUTube) getApplication()).getServerURL() + getString(R.string.like_action);
        try {
            HttpPostMultipart httpPostMultipart = new HttpPostMultipart(likeURL, "utf-8");
            httpPostMultipart.addFormField("token", ((NJUTube) getApplication()).getToken());
            httpPostMultipart.addFormField("video_id", String.valueOf(vid));
            httpPostMultipart.addFormField("action_type", doLike ? "1" : "2");
            httpPostMultipart.finish();
        } catch (IOException ignored) {
        }
    }

    private void updateLikeStatusFromServer() {
        Handler mainHandler = new Handler();
        Thread updateLikeThread = new Thread(() -> {
            String likeURL = ((NJUTube) getApplication()).getServerURL() + getString(R.string.like_action);
            try {
                HttpPostMultipart httpPostMultipart = new HttpPostMultipart(likeURL, "utf-8");
                httpPostMultipart.addFormField("token", ((NJUTube) getApplication()).getToken());
                httpPostMultipart.addFormField("video_id", String.valueOf(vid));
                httpPostMultipart.addFormField("action_type", "3");
                JSONObject jsonObject = new JSONObject(httpPostMultipart.finish());
                if (jsonObject.has("status_code")) {
                    if (jsonObject.getInt("status_code") == 1) {
                        // is liked
                        mainHandler.post(() -> {
                            likedThisVideo = true;
                            updateLikeButton();
                        });
                    } else {
                        mainHandler.post(() -> {
                            likedThisVideo = false;
                            updateLikeButton();
                        });
                    }
                }
            } catch (IOException | JSONException ignored) {
            }
        });
        updateLikeThread.start();
    }
}
