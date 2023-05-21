package com.example.nju_tube.fragment;

import android.os.Handler;
import android.widget.Toast;

import com.example.nju_tube.HttpUtils;
import com.example.nju_tube.NJUTube;
import com.example.nju_tube.R;
import com.example.nju_tube.ui.UserItem;
import com.example.nju_tube.ui.VideoItem;

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


public class FavoritesFragment extends ExploreFragment {
    @Override
    public void generateVideoList(Handler handler) {
        List<VideoItem> newVideos = new ArrayList<>();
        String serverUrl = getString(R.string.server_url);
        String likeListUrl = serverUrl+getString(R.string.my_favorite);
        likeListUrl += "?token="+((NJUTube)(Objects.requireNonNull(getActivity()).getApplication())).getToken();
        likeListUrl += "&user_id="+((NJUTube)(Objects.requireNonNull(getActivity()).getApplication())).getUserId();
        try {
            // 通过网络请求获得视频列表json数据
            HttpURLConnection connection = (HttpURLConnection) new URL(likeListUrl).openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            InputStream is = connection.getInputStream();
            JSONObject jsonObject = HttpUtils.readInputStream(is);
            connection.disconnect();

            int statusCode = jsonObject.getJSONObject("response").getInt("status_code");
            if (statusCode != 0) {
                handler.post(() -> Toast.makeText(getContext(), getString(R.string.something_wrong), Toast.LENGTH_SHORT).show());
                return;
            }
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
}