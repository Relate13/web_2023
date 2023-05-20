package com.example.nju_tube.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nju_tube.HttpUtils;
import com.example.nju_tube.NJUTube;
import com.example.nju_tube.R;
import com.example.nju_tube.VideoPlayActivity;
import com.example.nju_tube.ui.RecyclerViewInterface;
import com.example.nju_tube.ui.UserItem;
import com.example.nju_tube.ui.VideoItem;
import com.example.nju_tube.ui.VideoItemAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** 探索 页面 */
public class ExploreFragment extends Fragment implements RecyclerViewInterface {
    /** 视频列表 */
    final List<VideoItem> itemList=new ArrayList<>();
    VideoItemAdapter videoItemAdapter;
    public ExploreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Toast.makeText(getContext(),"Created",Toast.LENGTH_SHORT).show();
        // Inflate the layout for this fragment
        // 填充ui
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        // 获取列表控件
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_explore);
        // 设置布局
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        videoItemAdapter = new VideoItemAdapter(this, itemList, this);
        // 设置Adapter
        recyclerView.setAdapter(videoItemAdapter);

        // 获取生成视频清单
        final Handler handler = new Handler(); // 主线程Handler 用于更新RecyclerView
        Thread videoListThread = new Thread(()->generateVideoList(handler));
        videoListThread.start(); // 另开线程进行网络请求

        return view;
    }

    public void generateVideoList(Handler handler){
        List<VideoItem> newVideos = new ArrayList<>();
        String serverUrl = getString(R.string.server_url);
        String feedUrl = serverUrl+getString(R.string.feed_url);
        feedUrl += "?token="+((NJUTube)(Objects.requireNonNull(getActivity()).getApplication())).getToken();
        try {
            // 通过网络请求获得视频列表json数据
            // TODO: 服务端返回的视频列表可能不全（返回的是最新的若干个视频），因此一次请求可能无法获得所有视频信息
            HttpURLConnection connection = (HttpURLConnection) new URL(feedUrl).openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            InputStream is = connection.getInputStream();
            JSONObject jsonObject = HttpUtils.readInputStream(is);
            connection.disconnect();

            // 解析视频列表
            JSONArray videoList = jsonObject.getJSONArray("video_list");
            for (int i=0; i<videoList.length(); ++i) {
                // 处理视频信息并使其转换为VideoItem对象
                JSONObject rawVideoItem = videoList.getJSONObject(i);
                JSONObject rawUserItem = rawVideoItem.getJSONObject("author");
                UserItem userItem = new UserItem((int) rawUserItem.get("id"), (String) rawUserItem.get("name"));
                String uploadDate = (String) rawVideoItem.get("upload_date");
                String[] splitDate = uploadDate.split("-"); // 服务端返回的日期类似于2023-5-17-22-00
                uploadDate = splitDate[0]+"年"+splitDate[1]+"月"+splitDate[2]+"日"+" "+splitDate[3]+":"+splitDate[4]; // 重新格式化日期字符串

                VideoItem videoItem = new VideoItem((int) rawVideoItem.get("id"), serverUrl+rawVideoItem.get("play_url"),
                        serverUrl+rawVideoItem.get("cover_url"), (String) rawVideoItem.get("title"), userItem,
                        uploadDate, (int) rawVideoItem.get("favorite_count"),
                        (int) rawVideoItem.get("comment_count"), (Boolean) rawVideoItem.get("is_favorite"));
                newVideos.add(videoItem);
            }
            videoItemAdapter.addData(newVideos, handler); // 更新数据到View
        }
        catch (IOException | JSONException ignored) {}
    }

    // 列表的点击事件
    @Override
    public void OnItemClick(int pos) {
        Intent intent = new Intent(getContext(), VideoPlayActivity.class);
        VideoItem curItem = videoItemAdapter.getVideoItem(pos);
        intent.putExtra("VideoID", curItem.getId());
        intent.putExtra("VideoTitle", curItem.getVideoTitle());
        intent.putExtra("VideoURL", curItem.getVideoUrl());
        intent.putExtra("VideoDate", curItem.getUploadDate());
        intent.putExtra("CoverURL", curItem.getCoverUrl());
        intent.putExtra("Author", curItem.getAuthor().getName());
        intent.putExtra("Likes", curItem.getFavoriteCount());
        intent.putExtra("CommentNum", curItem.getCommentCount());
        startActivity(intent);
    }
}