package com.example.youngwind.coolnews.model;

/**
 * Created by youngwind on 16/7/13.
 */
public class Channels {
    public int showapi_res_code;
    public String showapi_res_error;
    public Showapi_res_body showapi_res_body;

    public class Showapi_res_body {
        public int totalNum;
        public int ret_code;
        public ChannelList[] channelList;
    }

//    public class ChannelList {
//        public String channelId;
//        public String name;
//
//
//    }
}
