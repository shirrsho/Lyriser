package com.example.swaralipi;

public class File {
    String name;
    String url;
    String postId;//Post id
    String id;//File id
    String fileId;

    File(){
    }
    File(String id, String fileId, String postId, String name, String url) {
        this.id = id;
        this.fileId = fileId;
        this.postId = postId;
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return this.name;
    }

    public String getUrl() {
        return this.url;
    }

    public String getPostId(){
        return this.postId;
    } // Post id

    public String getId(){
        return this.id;
    } // File id
}
