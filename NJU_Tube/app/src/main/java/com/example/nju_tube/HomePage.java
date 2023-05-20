package com.example.nju_tube;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.nju_tube.fragment.ExploreFragment;
import com.example.nju_tube.fragment.FavoritesFragment;
import com.example.nju_tube.fragment.HomeFragment;
import com.example.nju_tube.fragment.SubscriptionFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class HomePage extends AppCompatActivity {

    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private final int PICK_VIDEO_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //获取工具栏控件
        toolbar = findViewById(R.id.toolbar);
        //设置为程序的ActionBar
        setSupportActionBar(toolbar);
        //隐藏程序名称
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        //获取底部导航栏
        bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        //获取Frame显示区域布局
        frameLayout = findViewById(R.id.frame_layout);
        //为底部导航栏设置监听事件
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nju_tube_home) {
                HomeFragment homeFragment = new HomeFragment();
                SelectedFragment(homeFragment);
            } else if (itemId == R.id.explore) {
                ExploreFragment exploreFragment = new ExploreFragment();
                SelectedFragment(exploreFragment);
            } else if (itemId == R.id.publish) {
                askPermission();
                Intent videoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(videoIntent, PICK_VIDEO_REQUEST);
            } else if (itemId == R.id.favorites) {
                FavoritesFragment favoritesFragment = new FavoritesFragment();
                SelectedFragment(favoritesFragment);
            } else if (itemId == R.id.subscription) {
                SubscriptionFragment subscriptionFragment = new SubscriptionFragment();
                SelectedFragment(subscriptionFragment);
            }
            return false;
        });

        bottomNavigationView.setSelectedItemId(R.id.nju_tube_home);
    }

    private void askPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_MEDIA_VIDEO)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_MEDIA_VIDEO)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_MEDIA_VIDEO},
                            PICK_VIDEO_REQUEST);

                    // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
        } else {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            PICK_VIDEO_REQUEST);

                    // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
        }
    }

    private void uploadVideo(File videoFile, String title, Handler mainHandler) {
        String uploadURL = getString(R.string.server_url) + getString(R.string.upload_url);
        try {
            HttpPostMultipart httpPostMultipart = new HttpPostMultipart(uploadURL, "utf-8");
            httpPostMultipart.addFormField("token", ((NJUTube) getApplication()).getToken());
            httpPostMultipart.addFilePart("data", videoFile);
            httpPostMultipart.addFormField("title", title);
            JSONObject jsonObject = new JSONObject(httpPostMultipart.finish());
            int statusCode = jsonObject.getInt("status_code");
            if (statusCode != 0) {
                mainHandler.post(() -> Toast.makeText(this, getString(R.string.upload_fail), Toast.LENGTH_SHORT).show());
            } else {
                mainHandler.post(() -> Toast.makeText(this, getString(R.string.upload_success), Toast.LENGTH_SHORT).show());
            }
        } catch (IOException | JSONException e) {
            mainHandler.post(() -> Toast.makeText(this, getString(R.string.something_wrong), Toast.LENGTH_SHORT).show());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == PICK_VIDEO_REQUEST) {
                final EditText input = new EditText(this);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.make_title_hint))
                        .setView(input)
                        .setPositiveButton(getString(R.string.take_it), (dialog, id) -> {
                            String title = input.getText().toString();
                            if (title.isBlank()) {
                                Toast.makeText(this, getString(R.string.no_blank), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Handler mainHandler = new Handler();
                            ContentResolver cr = getContentResolver();
                            Uri videoUri = data.getData();
                            String path = HttpUtils.getFilePathFromContentUri(videoUri, cr);
                            File videoFile = new File(path);
                            Thread uploadThread = new Thread(() ->
                                    uploadVideo(videoFile, title, mainHandler));
                            uploadThread.start();
                        })
                        .setNegativeButton(getString(R.string.cancel_upload_btn), (dialog, id) -> Toast.makeText(this, getString(R.string.cancel_upload_toast), Toast.LENGTH_SHORT).show());
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        } else {
            Toast.makeText(this, getString(R.string.cancel_select_hint), Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // 切换 Fragment 辅助函数
    private void SelectedFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

    }

    // 设置工具栏格式
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // 设置工具栏监听事件
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.notification) {
            Toast.makeText(getApplicationContext(), "notification", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.account) {
            Toast.makeText(getApplicationContext(), "account", Toast.LENGTH_SHORT).show();
        } else {
            return super.onOptionsItemSelected(item);
        }
        return false;
    }
}