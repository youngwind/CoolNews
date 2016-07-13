package com.example.youngwind.coolnews.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.youngwind.coolnews.R;
import com.example.youngwind.coolnews.activity.ItemDetail;
import com.example.youngwind.coolnews.model.Newslist;

/**
 * Created by youngwind on 16/7/12.
 */
public class NormalRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final Context context;
    private Newslist.Contentlist[] contentlist;

    public NormalRecyclerViewAdapter(Context context, Newslist.Contentlist[] contentlist) {
        this.context = context;
        this.contentlist = contentlist;
        mLayoutInflater = LayoutInflater.from(context);

    }


    @Override
    public int getItemCount() {
        return contentlist.length;
    }

    // 导入布局文件
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalTextViewHolder(mLayoutInflater.inflate(R.layout.item, parent, false));
    }

    // 绑定数据
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((NormalTextViewHolder) holder).title.setText(contentlist[position].title);
        ((NormalTextViewHolder) holder).pubDate.setText(contentlist[position].pubDate);
        ((NormalTextViewHolder) holder).layout.setTag(contentlist[position].link);
    }

    public class NormalTextViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView pubDate;
        private LinearLayout layout;

        public NormalTextViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            pubDate = (TextView) itemView.findViewById(R.id.pubDate);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String link = view.getTag().toString();
                    Activity CurrentActivity = (Activity) view.getContext();
                    Intent intent = new Intent(CurrentActivity, ItemDetail.class);
                    Bundle b = new Bundle();
                    b.putString("link", link);
                    intent.putExtras(b);

                    CurrentActivity.startActivity(intent);
                }
            });

        }
    }
}
