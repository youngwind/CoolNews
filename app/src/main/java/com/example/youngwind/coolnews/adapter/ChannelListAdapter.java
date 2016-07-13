package com.example.youngwind.coolnews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.youngwind.coolnews.R;
import com.example.youngwind.coolnews.model.ChannelList;


/**
 * Created by youngwind on 16/7/13.
 */
public class ChannelListAdapter extends ArrayAdapter<ChannelList> {

    private int resourceId;

    public ChannelListAdapter(Context context, int textViewResourceId, ChannelList[] objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChannelList channelList = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView name = (TextView) view.findViewById(R.id.name);
        name.setText(channelList.getName());
        return view;
    }
}
