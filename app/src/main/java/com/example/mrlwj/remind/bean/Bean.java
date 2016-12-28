package com.example.mrlwj.remind.bean;

import java.lang.reflect.Field;

public class Bean{
    public interface State{
        int INIT = 0;
        int FINISHED = 1;
        int DEL = 2;

    }
        public String title;
        public String content;
        public String id;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int state;
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public String createTime;
        public String lastTime;

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

    @Override
    public Bean clone(){
        try{
            Class clazz = this.getClass();
            Field[] fields = clazz.getDeclaredFields();
            Bean ret = (Bean) clazz.newInstance();
            for(Field f :fields){
                f.setAccessible(true);
                Object value = f.get(this);
                f.set(ret,value);
            }
            return ret;
        }catch(Exception e){
            throw new RuntimeException("clone失败");
        }
    }
}