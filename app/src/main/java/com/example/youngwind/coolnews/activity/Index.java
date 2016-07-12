package com.example.youngwind.coolnews.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.youngwind.coolnews.R;
import com.example.youngwind.coolnews.model.Newslist;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class Index extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        asynhttpGet("http://apis.baidu.com/showapi_open_bus/channel_news/search_news?channelId=5572a109b3cdc86cf39001db");
    }

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
                Log.d("response", String.valueOf(newslist.showapi_res_body.pagebean.allPages));
                Log.d("response", String.valueOf(newslist.showapi_res_body.pagebean.contentlist[0].title));
                Log.d("response", String.valueOf(newslist.showapi_res_body.pagebean.contentlist[0].link));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("fai", new String(responseBody));
            }
        });
    }
}
