package com.example.mysns;

import java.io.Serializable;

public class BlogData implements Serializable {
    private String title;
    private String content;
    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        String title1 = title.replace("<b>","");
        String title2 = title1.replace("</b>","");
        this.title = title2;

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        String content1 = content.replace("<b>","");
        String content2 = content1.replace("</b>","");
        this.content = content2;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
