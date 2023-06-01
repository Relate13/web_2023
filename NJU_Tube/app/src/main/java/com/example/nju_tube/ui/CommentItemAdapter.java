package com.example.nju_tube.ui;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nju_tube.R;

import java.util.List;

/**
 * 列表适配器，用于把视频信息对象填充进列表中
 */

public class CommentItemAdapter extends RecyclerView.Adapter<CommentItemViewHolder> {

    final List<CommentItem> items;

    public CommentItemAdapter(List<CommentItem> items) {
        this.items = items;
    }

    public void addData(List<CommentItem> newItems, Handler mainThreadHandler) {
        mainThreadHandler.post(() -> {
            int oldItemsSize = this.items.size();
            this.items.addAll(newItems);
            this.notifyItemRangeInserted(oldItemsSize, newItems.size());
        });
    }

    public CommentItem getCommentItem(int pos) {
        return items.get(pos);
    }

    @NonNull
    @Override
    public CommentItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 填充每个列表项的ui
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item_view, parent, false);
        // 生成对应的列表项对象，并传递对应的接口对象，以便从列表项对象监听点击事件
        return new CommentItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentItemViewHolder holder, int position) {
        // 设置ui对应位置的文字与图片
        holder.commentContent.setText(items.get(position).getCommentContent());
        holder.commentTimeStamp.setText(items.get(position).getCommentTimeStamp());
        holder.commentUserID.setText(items.get(position).getCommentUserName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
