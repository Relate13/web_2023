package com.example.nju_tube.ui;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nju_tube.R;

/**
 * 视频列表项对象
 */

public class CommentItemViewHolder extends RecyclerView.ViewHolder {
    // 需要暴露给外界的ui控件
    final TextView commentContent;
    final TextView commentUserID;
    final TextView commentTimeStamp;

    public CommentItemViewHolder(@NonNull View itemView) {
        super(itemView);
        // 获取各ui控件，以便适配器填充列表时修改
        commentContent = itemView.findViewById(R.id.comment_content);
        commentUserID = itemView.findViewById(R.id.comment_userid);
        commentTimeStamp = itemView.findViewById(R.id.comment_time_stamp);
    }
}
