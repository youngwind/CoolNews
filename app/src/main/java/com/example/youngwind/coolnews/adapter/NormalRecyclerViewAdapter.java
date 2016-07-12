package com.example.youngwind.coolnews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youngwind.coolnews.R;
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
        ((NormalTextViewHolder) holder).text.setText(contentlist[position].title);
        ((NormalTextViewHolder) holder).layout.setTag(contentlist[position].title);
    }

    public class NormalTextViewHolder extends RecyclerView.ViewHolder {
        private TextView text;
        private LinearLayout layout;

        public NormalTextViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "" + view.getTag(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
