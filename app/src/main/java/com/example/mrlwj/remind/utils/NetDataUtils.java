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
    public static void getJson(ResCallBack callBack){
        String url = MainActivity.context.getString(R.string.json_url);
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        requestQueue.add(GETALL, request, new MyListener(callBack, null));
    }
    public static void delItem(Bean bean,ResCallBack callBack){
        String url = MainActivity.context.getString(R.string.del_url);
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        request.add("id",bean.id);
        requestQueue.add(DEL, request, new MyListener(callBack,bean));
    }
    public interface ResCallBack{
        void handle(boolean success,String msg,Bean bean);
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
        switch(i){
            case NetDataUtils.ADD:
                String resp = response.get();
                if(TextUtils.isDigitsOnly(resp)){
                    mBean.id = resp;
                    Date date=new Date();
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String time=format.format(date);
                    mBean.time = time;
                    mCallBack.handle(true,resp,mBean);
                }else{
                    mCallBack.handle(false,resp,mBean);
                }
                break;
            case NetDataUtils.GETALL:
                mCallBack.handle(true,response.get(),null);
                break;
            case NetDataUtils.DEL:
                mCallBack.handle(response.get().equals("1"),response.get(),mBean);
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
