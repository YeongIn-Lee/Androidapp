package com.example.mysns;

import com.google.firebase.Timestamp;

public class SubPost {
    private String context;
    private String publisher;
    private Timestamp timestamp;

    public SubPost() {}

    public SubPost(String context,String publisher,Timestamp timestamp){
        this.context=context;
        this.publisher=publisher;
        this.timestamp=timestamp;
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
