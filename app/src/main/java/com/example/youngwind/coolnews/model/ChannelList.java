package com.example.youngwind.coolnews.model;

/**
 * Created by youngwind on 16/7/13.
 */
public class ChannelList {
    private String name;
    private String channelId;

    public ChannelList(String name, String channelId) {
        this.name = name;
        this.channelId = channelId;
    }

    public  String getName() {
        return name;
    }

    public String getChannelId() {
        return channelId;
    }
}
