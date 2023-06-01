package com.example.nju_tube.ui;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nju_tube.R;

import java.util.List;

/**
 * 列表适配器，用于把视频信息对象填充进列表中
 */

public class VideoItemAdapter extends RecyclerView.Adapter<VideoItemViewHolder> {

    final List<VideoItem> items;

    final RecyclerViewInterface recyclerViewInterface;

    final Fragment fragment;

    public VideoItemAdapter(Fragment fragment, List<VideoItem> items, RecyclerViewInterface recyclerViewInterface) {
        this.fragment = fragment;
        this.recyclerViewInterface = recyclerViewInterface;
        this.items = items;
    }

    public void addData(List<VideoItem> newItems, Handler mainThreadHandler) {
        mainThreadHandler.post(() -> {
            int oldItemsSize = this.items.size();
            this.items.addAll(newItems);
            this.notifyItemRangeInserted(oldItemsSize, newItems.size());
        });
    }

    public VideoItem getVideoItem(int pos) {
        return items.get(pos);
    }

    @NonNull
    @Override
    public VideoItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 填充每个列表项的ui
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item_view, parent, false);
        // 生成对应的列表项对象，并传递对应的接口对象，以便从列表项对象监听点击事件
        return new VideoItemViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoItemViewHolder holder, int position) {
        // 设置ui对应位置的文字与图片
        Glide.with(this.fragment).load(items.get(position).getCoverUrl()).into(holder.thumbnail);
        holder.title.setText(items.get(position).getVideoTitle());
        holder.uploader.setText(items.get(position).getAuthor().getName());
        holder.time.setText(items.get(position).getUploadDate());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
