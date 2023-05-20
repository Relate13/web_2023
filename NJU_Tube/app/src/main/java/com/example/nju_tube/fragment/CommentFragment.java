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
import com.example.nju_tube.ui.CommentItem;
import com.example.nju_tube.ui.CommentItemAdapter;
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
public class CommentFragment extends Fragment implements RecyclerViewInterface {
    /** 视频列表 */
    final List<CommentItem> itemList=new ArrayList<>();
    CommentItemAdapter commentItemAdapter;
    public CommentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Toast.makeText(getContext(),"Created",Toast.LENGTH_SHORT).show();
        // Inflate the layout for this fragment
        // 填充ui
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        // 获取列表控件
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_comment);
        // 设置布局
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        commentItemAdapter = new CommentItemAdapter(this, itemList, this);
        // 设置Adapter
        recyclerView.setAdapter(commentItemAdapter);

        // 获取生成视频清单
        final Handler handler = new Handler(); // 主线程Handler 用于更新RecyclerView
        Thread videoListThread = new Thread(()->generateVideoList(handler));
        videoListThread.start(); // 另开线程进行网络请求

        return view;
    }

    public void generateVideoList(Handler handler){
        List<CommentItem> comments = new ArrayList<>();
        comments.add(new CommentItem("感觉不如原神","Relate13","2023.5.20"));

        commentItemAdapter.addData(comments, handler);
    }

    // 列表的点击事件
    @Override
    public void OnItemClick(int pos) {

    }
}
