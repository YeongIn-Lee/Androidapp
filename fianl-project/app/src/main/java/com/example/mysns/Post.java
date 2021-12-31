package com.example.mysns;

import com.google.firebase.Timestamp;

public class Post {
    private String title;
    private String context;
    private String publisher;
    private Timestamp timestamp;
    public Post() {}

    public Post(String title,String context,String publisher,Timestamp timestamp){
        this.title=title;
        this.context=context;
        this.publisher=publisher;
        this.timestamp=timestamp;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getContext() {
        return context;
    }
    public void setContext(String context){
        this.context = context;
    }
    public String getPublisher(){return publisher;}
    public void setPublisher(String publisher){this.publisher=publisher;}
    public Timestamp getTimestamp() { return timestamp; }
    public void setTimestamp(Timestamp timestamp){this.timestamp=timestamp;}
}
