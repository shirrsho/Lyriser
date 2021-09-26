package com.example.swaralipi;

import java.util.ArrayList;

public class Post {

    String id, title, data, matchId;

    public Post(){
    }

    public Post(String id, String matchId, String title, String data){
        this.id = id;
        this.title = title;
        this.data = data;
        this.matchId = matchId;
    }

    public String getTitle() {
        return title;
    }

    public String getData() {
        return data;
    }

    public String getmatchId(){
        return matchId;
    }

}
