package com.example.nju_tube.fragment;

import android.os.Handler;

import com.example.nju_tube.ui.UserItem;
import com.example.nju_tube.ui.VideoItem;

import java.util.ArrayList;
import java.util.List;

public class UploadedByMeFragment extends ExploreFragment{
    private final String userName;
    public UploadedByMeFragment(String whoAmI){
        userName=whoAmI;
    }
    @Override
    public void generateVideoList(Handler handler) {
        List<VideoItem> newVideos = new ArrayList<>();
        newVideos.add(new VideoItem(1, "1", "1", "1", new UserItem(1,userName), "1", 1, 1, true));
        newVideos.add(new VideoItem(1, "1", "1", "1", new UserItem(1,userName), "1", 1, 1, true));
        newVideos.add(new VideoItem(1, "1", "1", "1", new UserItem(1,userName), "1", 1, 1, true));
        newVideos.add(new VideoItem(1, "1", "1", "1", new UserItem(1,userName), "1", 1, 1, true));
        videoItemAdapter.addData(newVideos, handler);
    }
}
