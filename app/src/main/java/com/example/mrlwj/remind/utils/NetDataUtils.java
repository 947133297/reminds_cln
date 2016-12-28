package com.example.mrlwj.remind.utils;

import android.text.TextUtils;

import com.example.mrlwj.remind.R;
import com.example.mrlwj.remind.activity.MainActivity;
import com.example.mrlwj.remind.bean.Bean;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by MRLWJ on 2016/11/12.
 */
public class NetDataUtils {

    public static final int ADD = 0;
    public static final int GETALL = 1;
    public static final int DEL = 3;
    public static final int UPDATE = 4;
    public static final int CHANGE = 5;

    private static  RequestQueue requestQueue = NoHttp.newRequestQueue();
    public static void send(Bean bean,ResCallBack callBack){
        if(TextUtils.isEmpty(bean.title)||TextUtils.isEmpty(bean.content)){
            callBack.handle(false,"不能为空",bean);
            return;
        }
        String url = MainActivity.context.getString(R.string.send_url);
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
        request.add("title",bean.title);
        request.add("content",bean.content);
        requestQueue.add(ADD, request, new MyListener(callBack,bean));
    }
    public static void update(Bean bean,ResCallBack callBack){
        if(TextUtils.isEmpty(bean.content)){
            callBack.handle(false,"内容不能为空",bean);
            return;
        }
        String url = MainActivity.context.getString(R.string.update_url);
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
        request.add("id",bean.id);
        request.add("content",bean.content);
        requestQueue.add(UPDATE, request, new MyListener(callBack,bean));
    }
    public static void changeState(Bean bean,ResCallBack callBack){
        String url = MainActivity.context.getString(R.string.change_url);
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
        request.add("id",bean.id);
        request.add("state",bean.state);
        requestQueue.add(CHANGE, request, new MyListener(callBack,bean));
    }
    public static void getJson(ResCallBack callBack){
        String url = MainActivity.context.getString(R.string.json_url);
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        requestQueue.add(GETALL, request, new MyListener(callBack, null));
    }
    public interface ResCallBack{
        void handle(boolean success,String msg,Bean bean);
    }
    public static String getCurTime(){
        Date date=new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }
}
class MyListener implements OnResponseListener<String>{

    NetDataUtils.ResCallBack mCallBack;
    Bean mBean;
    public MyListener(NetDataUtils.ResCallBack callBack, Bean bean){
        mCallBack = callBack;
        mBean = bean;
    }
    @Override
    public void onStart(int i) {

    }

    @Override
    public void onSucceed(int i, Response<String> response) {
        String resp = response.get();
        switch(i){
            case NetDataUtils.ADD:
                if(TextUtils.isDigitsOnly(resp)){
                    mBean.id = resp;
                    mBean.lastTime = mBean.createTime = NetDataUtils.getCurTime();
                    mCallBack.handle(true,resp,mBean);
                }else{
                    mCallBack.handle(false,resp,mBean);
                }
                break;
            case NetDataUtils.GETALL:
                mCallBack.handle(true,resp,null);
                break;
            case NetDataUtils.DEL:
                mCallBack.handle(resp.equals("1"),resp,mBean);
                break;
            case NetDataUtils.UPDATE:
                mCallBack.handle(TextUtils.isDigitsOnly(resp),resp,mBean);
                break;
            case NetDataUtils.CHANGE:
                mCallBack.handle(TextUtils.isDigitsOnly(resp),resp,mBean);
                break;
        }
    }

    @Override
    public void onFailed(int i, Response<String> response) {
        mCallBack.handle(false,"发送失败",mBean);
    }

    @Override
    public void onFinish(int i) {}
}
