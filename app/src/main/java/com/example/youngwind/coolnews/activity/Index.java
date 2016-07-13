package com.example.youngwind.coolnews.activity;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

    private String[] titles = {"我", "和", "你"};
    private ListView mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        getChannelList();

        asynhttpGet("http://apis.baidu.com/showapi_open_bus/channel_news/search_news?channelId=5572a109b3cdc86cf39001db");
    }

    /**
     * 请求所有新闻频道
     */
    private void getChannelList() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("apikey", "2c25fe0184c7cb0b54c813eae914ad7b");
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
            }
        });
    }

    /**
     * 请求某个频道的新闻信息
     *
     * @param url
     */
    private void asynhttpGet(String url) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("apiKey", "2c25fe0184c7cb0b54c813eae914ad7b");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);

                Gson gson = new Gson();
                Newslist newslist = gson.fromJson(response, Newslist.class);
                Log.d("response", String.valueOf(newslist.showapi_res_code));
                Log.d("response", String.valueOf(newslist.showapi_res_body.pagebean.contentlist[0].title));
                Log.d("response", String.valueOf(newslist.showapi_res_body.pagebean.contentlist[0].link));
                showNewList(newslist);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("fai", new String(responseBody));
            }
        });
    }

    private void showNewList(Newslist newslist) {

        final Newslist.Contentlist[] contentlist = newslist.showapi_res_body.pagebean.contentlist;

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        NormalRecyclerViewAdapter adapter = new NormalRecyclerViewAdapter(this, contentlist);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 设置左滑菜单栏
     */
    private void setDrawerLayout(Channels channels) {
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
//        mDrawerList.setAdapter(new ArrayAdapter<String>(Index.this, R.layout.drawer_item, R.id.text, titles));
//        mDrawerList.setAdapter(new ArrayAdapter<Channels.ChannelList>(Index.this,R.layout.drawer_item,R.id.text,channels.showapi_res_body.channelList.name));
        mDrawerList.setAdapter(new ChannelListAdapter(Index.this, R.layout.drawer_item, channels.showapi_res_body.channelList));
    }

}
