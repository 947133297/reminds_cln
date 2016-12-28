package com.example.mrlwj.remind;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mrlwj.remind.bean.Bean;

import java.util.List;

/**
 * Created by MRLWJ on 2016/11/12.
 */
public class MyAdapter extends BaseAdapter {
    private List<Bean> mDataList;
    public MyAdapter(List<Bean> list){
       mDataList = list;
    }
    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if(convertView==null){
            convertView = View.inflate(parent.getContext(),R.layout.item,null);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        Bean target = mDataList.get(position);
        holder.tvContent.setText(target.content);
        holder.tvTitle.setText(target.title);
        holder.tvDataId.setText("#"+target.id);
        holder.tvCreateTime.setText("创建时间："+target.createTime);
        if(target.state == Bean.State.FINISHED){
            holder.tvLastTime.setText("完成时间："+target.lastTime);
            holder.tvLastTime.setTextColor(Color.RED);
        }else{
            holder.tvLastTime.setText("修改时间："+target.lastTime);
        }
        return convertView;
    }
    public class Holder{
        public Holder(View root){
            tvTitle = (TextView) root.findViewById(R.id.item_title);
            tvContent = (TextView) root.findViewById(R.id.item_content);
            tvDataId = (TextView) root.findViewById(R.id.item_data_id);
            tvCreateTime = (TextView) root.findViewById(R.id.item_create_time);
            tvLastTime = (TextView) root.findViewById(R.id.item_last_time);
        }
        public TextView tvTitle,tvContent,tvDataId, tvCreateTime,tvLastTime;
    }
}
