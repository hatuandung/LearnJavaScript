package com.htd.learnjavascript;

import java.io.Serializable;

public class Chapter implements Serializable {
    private String title;
    private String url;
    private int id;

    public Chapter(String title, String url, int id) {
        this.title = title;
        this.url = url;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
