package com.example.nju_tube.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nju_tube.R;
import com.example.nju_tube.VideoPlayActivity;
import com.example.nju_tube.ui.RecyclerViewInterface;
import com.example.nju_tube.ui.VideoItem;
import com.example.nju_tube.ui.VideoItemAdapter;

import java.util.ArrayList;
import java.util.List;

/** 探索 页面 */
public class ExploreFragment extends Fragment implements RecyclerViewInterface {
    /** 视频列表 */
    List<VideoItem> itemList=new ArrayList<>();
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
        // 获取/生成视频清单
        generateVideoList();
        // 把清单内容填充到列表中
        recyclerView.setAdapter(new VideoItemAdapter(itemList,this));

        return view;
    }

    public void generateVideoList(){
        itemList=new ArrayList<>();
        itemList.add(new VideoItem("AAAA","123","2012.1.1"));
        itemList.add(new VideoItem("BB","321","2022.1.1"));
        itemList.add(new VideoItem("CCCC","daw","2023.1.1"));
        itemList.add(new VideoItem("DDD","awe","2021.2.1"));
        itemList.add(new VideoItem("EEEE","sdfe","2015.1.1"));
        itemList.add(new VideoItem("EEEE","eerder","2015.1.1"));
        itemList.add(new VideoItem("EEEE","eercadaer","2015.1.1"));
        itemList.add(new VideoItem("EEEE","ee23123rer","2015.1.1"));
    }

    // 列表的点击事件
    @Override
    public void OnItemClick(int pos) {
        Intent intent = new Intent(getContext(), VideoPlayActivity.class);
        startActivity(intent);
        Toast.makeText(getContext(),itemList.get(pos).getVideoTitle(),Toast.LENGTH_SHORT).show();
    }
}