package com.example.nju_tube.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nju_tube.R;

import java.util.List;

/** 列表适配器，用于把视频信息对象填充进列表中 */

public class VideoItemAdapter extends RecyclerView.Adapter<VideoItemViewHolder> {

    List<VideoItem> items;

    RecyclerViewInterface recyclerViewInterface;

    public VideoItemAdapter(List<VideoItem> items, RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface=recyclerViewInterface;
        this.items=items;
    }

    @NonNull
    @Override
    public VideoItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 填充每个列表项的ui
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item_view,parent,false);
        // 生成对应的列表项对象，并传递对应的接口对象，以便从列表项对象监听点击事件
        return new VideoItemViewHolder(view,recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoItemViewHolder holder, int position) {
        // 设置ui对应位置的文字与图片，暂时不能设置图片
        holder.title.setText(items.get(position).getVideoTitle());
        holder.uploader.setText(items.get(position).getUploader());
        holder.time.setText(items.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
