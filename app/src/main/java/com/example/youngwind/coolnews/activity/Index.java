package com.example.youngwind.coolnews.activity;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.youngwind.coolnews.R;
import com.example.youngwind.coolnews.adapter.ChannelListAdapter;
import com.example.youngwind.coolnews.adapter.NormalRecyclerViewAdapter;
import com.example.youngwind.coolnews.model.Channels;
import com.example.youngwind.coolnews.model.Newslist;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class Index extends AppCompatActivity {

    private ListView mDrawerList;
    private String apiKey = "2c25fe0184c7cb0b54c813eae914ad7b";
    private SwipeRefreshLayout swipeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe);

        getChannelList();

        getNewsByChannelId("5572a109b3cdc86cf39001db", "INIT_REQUEST");

        setSwipeRefresh();
    }

    /**
     * 请求所有新闻频道
     */
    private void getChannelList() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("apikey", apiKey);
        client.get("http://apis.baidu.com/showapi_open_bus/channel_news/channel_news", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Gson gson = new Gson();
                Channels channels = gson.fromJson(response, Channels.class);
                setDrawerLayout(channels);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("fail", new String(responseBody));
                Toast.makeText(Index.this, "获取频道失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 请求某个频道的新闻信息
     *
     * @param channelId   频道ID
     * @param requestType 请求类型, INIT_REQUEST:程序初始化请求, REFRESH_REQUEST:刷新请求
     */
    private void getNewsByChannelId(String channelId, final String requestType) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("apiKey", apiKey);
        client.get("http://apis.baidu.com/showapi_open_bus/channel_news/search_news?channelId=" + channelId, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (requestType.equals("REFRESH_REQUEST")) {
                    swipeView.setRefreshing(false);
                }
                Toast.makeText(Index.this, "已是最新", Toast.LENGTH_SHORT).show();
                String response = new String(responseBody);

                Gson gson = new Gson();
                Newslist newslist = gson.fromJson(response, Newslist.class);
                showNewList(newslist.showapi_res_body.pagebean.contentlist);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("fail", new String(responseBody));
                Toast.makeText(Index.this, "获取新闻失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 显示新闻列表
     *
     * @param contentlist 新闻信息列表
     */
    private void showNewList(Newslist.Contentlist[] contentlist) {

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        NormalRecyclerViewAdapter adapter = new NormalRecyclerViewAdapter(this, contentlist);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 设置左滑菜单
     *
     * @param channels 新闻频道列表
     */
    private void setDrawerLayout(Channels channels) {
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ChannelListAdapter(Index.this, R.layout.drawer_item, channels.showapi_res_body.channelList));
    }

    private void setSwipeRefresh() {
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);
                getNewsByChannelId("5572a109b3cdc86cf39001db", "REFRESH_REQUEST");
            }
        });
    }

}
