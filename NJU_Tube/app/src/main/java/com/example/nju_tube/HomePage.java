package com.example.nju_tube;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.nju_tube.fragment.ExploreFragment;
import com.example.nju_tube.fragment.FavoritesFragment;
import com.example.nju_tube.fragment.HomeFragment;
import com.example.nju_tube.fragment.SubscriptionFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class HomePage extends AppCompatActivity {

    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;

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
            }
            else if (itemId == R.id.explore) {
                ExploreFragment exploreFragment = new ExploreFragment();
                SelectedFragment(exploreFragment);
            }
            else if (itemId == R.id.publish) {
                Toast.makeText(getApplicationContext(), "上传视频", Toast.LENGTH_SHORT).show();
            }
            else if (itemId == R.id.favorites) {
                FavoritesFragment favoritesFragment = new FavoritesFragment();
                SelectedFragment(favoritesFragment);
            }
            else if (itemId == R.id.subscription) {
                SubscriptionFragment subscriptionFragment = new SubscriptionFragment();
                SelectedFragment(subscriptionFragment);
            }
            return false;
        });

        bottomNavigationView.setSelectedItemId(R.id.nju_tube_home);
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
        }
        else if (itemId == R.id.account) {
            Toast.makeText(getApplicationContext(), "account", Toast.LENGTH_SHORT).show();
        }
        else {
            return super.onOptionsItemSelected(item);
        }
        return false;
    }
}