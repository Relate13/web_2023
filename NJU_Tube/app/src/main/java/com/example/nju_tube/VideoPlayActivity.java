package com.example.nju_tube;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.nju_tube.fragment.CommentFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/** 视频播放页面 */
public class VideoPlayActivity extends AppCompatActivity {
    int vid;
    TextView commentCounter;

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
        vid = intent.getIntExtra("VideoID", 0);

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

        // set comment
        commentCounter = findViewById(R.id.comment_counter);
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
                    .setNegativeButton(getString(R.string.comment_cancel), (dialog, id) -> {});
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void sendComment(int vid, String comment, Handler mainHandler) {
        String commentURL = getString(R.string.server_url)+getString(R.string.comment_action);
        try {
            HttpPostMultipart httpPostMultipart = new HttpPostMultipart(commentURL, "utf-8");
            httpPostMultipart.addFormField("token", ((NJUTube) getApplication()).getToken());
            httpPostMultipart.addFormField("video_id", String.valueOf(vid));
            httpPostMultipart.addFormField("action_type", "1");
            httpPostMultipart.addFormField("comment_text", comment);
            JSONObject jsonObject = new JSONObject(httpPostMultipart.finish());
            int statusCode = jsonObject.getJSONObject("Response").getInt("status_code");
            if (statusCode == 0) {
                setCommentFragment(new CommentFragment(vid, commentCounter));
                mainHandler.post(() -> Toast.makeText(this, getString(R.string.comment_success), Toast.LENGTH_SHORT).show());
            }
            else {
                mainHandler.post(() -> Toast.makeText(this, getString(R.string.comment_fail), Toast.LENGTH_SHORT).show());
            }
        }
        catch (IOException | JSONException ignored) {
            mainHandler.post(() -> Toast.makeText(this, getString(R.string.comment_fail), Toast.LENGTH_SHORT).show());
        }
    }

    private void setCommentFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.comment_list_frame, fragment);
        fragmentTransaction.commit();
    }
}
