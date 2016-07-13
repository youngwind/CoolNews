package com.example.youngwind.coolnews.model;

/**
 * Created by youngwind on 16/7/12.
 */
public class Newslist {
    public int showapi_res_code;
    public String showapi_res_error;
    public Showapi_res_body showapi_res_body;

    public class Showapi_res_body {
        public int ret_code;
        public Pagebean pagebean;
    }

    public class Pagebean {
        public int allPages;
        public int currentPage;
        public Contentlist[] contentlist;
    }

    public class Contentlist {
        public String title;
        public String link;
        public String pubDate;
    }


}
