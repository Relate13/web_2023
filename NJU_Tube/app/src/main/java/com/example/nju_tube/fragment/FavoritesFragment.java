package com.example.nju_tube.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.nju_tube.R;
import com.example.nju_tube.ui.UserItem;
import com.example.nju_tube.ui.VideoItem;

import java.util.ArrayList;
import java.util.List;


public class FavoritesFragment extends ExploreFragment {
    private final String userName;
    public FavoritesFragment(String whoAmI){
        userName=whoAmI;
    }
    @Override
    public void generateVideoList(Handler handler) {
        List<VideoItem> newVideos = new ArrayList<>();
        newVideos.add(new VideoItem(1, "1", "1", "1", new UserItem(1,"1"), "1", 1, 1, true));
        newVideos.add(new VideoItem(1, "1", "1", "1", new UserItem(1,"1"), "1", 1, 1, true));
        newVideos.add(new VideoItem(1, "1", "1", "1", new UserItem(1,"1"), "1", 1, 1, true));
        newVideos.add(new VideoItem(1, "1", "1", "1", new UserItem(1,"1"), "1", 1, 1, true));
        videoItemAdapter.addData(newVideos, handler);
    }
}