package com.example.youngwind.coolnews.activity;

import android.app.ActionBar;
import android.renderscript.Sampler;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.youngwind.coolnews.R;
import com.example.youngwind.coolnews.adapter.ChannelListAdapter;
import com.example.youngwind.coolnews.adapter.NormalRecyclerViewAdapter;
import com.example.youngwind.coolnews.model.ChannelList;
import com.example.youngwind.coolnews.model.Channels;
import com.example.youngwind.coolnews.model.Newslist;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class Index extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ListView mDrawerList;
    private String apiKey = "2c25fe0184c7cb0b54c813eae914ad7b";
    private SwipeRefreshLayout swipeView;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private boolean loadingMore = false;
    private Newslist.Contentlist[] contentlist;
    private NormalRecyclerViewAdapter adapter;
    private int currentPage = 1;
    private int allPages;
    private ChannelList[] channelLists;
    private String currentChannelId;
    private String currentChannelName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        setTitle("国内热门");


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new NormalRecyclerViewAdapter(this, contentlist);

        getChannelList();

        setSwipeRefresh();

        setScrollUpLoadMore();
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

                channelLists = channels.showapi_res_body.channelList;
                currentChannelId = channelLists[0].getChannelId();
                setDrawerLayout(channels.showapi_res_body.channelList);
                getNewsByChannelId(currentChannelId, "INIT_REQUEST");

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
        client.get("http://apis.baidu.com/showapi_open_bus/channel_news/search_news?channelId=" + channelId + "&page=" + currentPage, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                currentPage++;
                loadingMore = false;

                if (requestType.equals("REFRESH_REQUEST")) {
                    swipeView.setRefreshing(false);
                }
//                Toast.makeText(Index.this, "已是最新", Toast.LENGTH_SHORT).show();
                String response = new String(responseBody);

                Gson gson = new Gson();
                Newslist newslist = gson.fromJson(response, Newslist.class);
                allPages = newslist.showapi_res_body.pagebean.allPages;
                showNewList(newslist.showapi_res_body.pagebean.contentlist, requestType);

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
    private void showNewList(Newslist.Contentlist[] contentlist, String requestType) {
        int length = adapter.getItemCount();

        adapter.insert(contentlist, requestType);

        recyclerView.setAdapter(adapter);

        recyclerView.scrollToPosition(adapter.getItemCount() - 27);
    }

    /**
     * 设置左滑菜单
     *
     * @param channelList 新闻频道列表
     */
    private void setDrawerLayout(ChannelList[] channelList) {
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ChannelListAdapter(Index.this, R.layout.drawer_item, channelList));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    /**
     * 左侧导航栏点击事件绑定
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            currentChannelName = channelLists[i].getName();
            setTitle(currentChannelName);
            currentChannelId = channelLists[i].getChannelId();
            currentPage = 1;
            drawerLayout.closeDrawer(Gravity.LEFT);
            getNewsByChannelId(currentChannelId, "REFRESH_REQUEST");
        }
    }

    private void setSwipeRefresh() {
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);
                getNewsByChannelId(currentChannelId, "REFRESH_REQUEST");
            }
        });
    }

    /**
     * 设置上拉加载更多
     */
    private void setScrollUpLoadMore() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
                int totalItemCount = adapter.getItemCount();
                if (lastVisibleItem >= totalItemCount - 4 && dy > 0) {
                    if (currentPage == allPages) {
                        Toast.makeText(Index.this, "没有更多了...", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!loadingMore) {
                        loadingMore = true;
//                        Toast.makeText(Index.this, "加载中....", Toast.LENGTH_SHORT).show();
                        getNewsByChannelId(currentChannelId, "");
                    }

                }
            }
        });
    }

}
