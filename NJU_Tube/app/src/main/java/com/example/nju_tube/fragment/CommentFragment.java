package com.example.nju_tube.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nju_tube.HttpUtils;
import com.example.nju_tube.R;
import com.example.nju_tube.ui.CommentItem;
import com.example.nju_tube.ui.CommentItemAdapter;
import com.example.nju_tube.ui.RecyclerViewInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/** 探索 页面 */
public class CommentFragment extends Fragment implements RecyclerViewInterface {
    /** 视频列表 */
    final List<CommentItem> itemList=new ArrayList<>();
    final int videoId;
    CommentItemAdapter commentItemAdapter;
    final TextView commentCounter;
    public CommentFragment(int videoId, TextView commentCounter) {
        this.videoId = videoId;
        this.commentCounter = commentCounter;
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

        final Handler handler = new Handler(); // 主线程Handler 用于更新RecyclerView
        Thread commentListThread = new Thread(()->generateCommentList(handler, commentCounter));
        commentListThread.start(); // 另开线程进行网络请求

        return view;
    }

    public void generateCommentList(Handler handler, TextView commentCounter){
        List<CommentItem> comments = new ArrayList<>();
        String serverUrl = getString(R.string.server_url);
        String commentUrl = serverUrl+getString(R.string.comment_list);
        commentUrl += "?video_id="+videoId;
        try {
            // 通过网络请求获得视频列表json数据
            HttpURLConnection connection = (HttpURLConnection) new URL(commentUrl).openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            InputStream is = connection.getInputStream();
            JSONObject jsonObject = HttpUtils.readInputStream(is);
            connection.disconnect();

            int statusCode = jsonObject.getJSONObject("response").getInt("status_code");
            if (statusCode != 0) {
                throw new IOException();
            }

            if (!jsonObject.has("comment_list")) {
                return;
            }

            JSONArray commentList = jsonObject.getJSONArray("comment_list");
            for (int i=0; i<commentList.length(); ++i) {
                JSONObject rawCommentItem = commentList.getJSONObject(i);
                JSONObject rawUserItem = rawCommentItem.getJSONObject("user");
                String commentDate = (String) rawCommentItem.get("create_date");
                String[] splitDate = commentDate.split("-"); // 服务端返回的日期类似于2023-5-17-22-00
                commentDate = splitDate[0]+"年"+splitDate[1]+"月"+splitDate[2]+"日"+" "+splitDate[3]+":"+splitDate[4]; // 重新格式化日期字符串

                CommentItem commentItem = new CommentItem(rawCommentItem.getInt("id"), rawCommentItem.getString("content"),
                        rawUserItem.getString("name"), commentDate, rawUserItem.getInt("id"));
                comments.add(commentItem);
            }
            commentItemAdapter.addData(comments, handler); // 更新数据到View
            handler.post(() -> {
                String commentHint = "评论（"+commentItemAdapter.getItemCount()+"条）";
                commentCounter.setText(commentHint);
            });
        }
        catch (IOException | JSONException ignored) {
            handler.post(() -> Toast.makeText(getContext(), getString(R.string.cannot_get_comment), Toast.LENGTH_SHORT).show());
        }
    }

    // 列表的点击事件
    @Override
    public void OnItemClick(int pos) {

    }
}
