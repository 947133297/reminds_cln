package com.example.mrlwj.remind.bean;

public class Bean{
        public String title;
        public String content;
        public String id;
        public String time;
        public Bean(String title, String content) {
            this.title = title;
            this.content = content;
        }
    public Bean(){}
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}