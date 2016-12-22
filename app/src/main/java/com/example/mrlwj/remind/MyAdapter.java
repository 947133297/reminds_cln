package com.example.mrlwj.remind;

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
        holder.tvContent.setText(mDataList.get(position).content);
        holder.tvTitle.setText(mDataList.get(position).title);
        holder.tvDataId.setText("#"+mDataList.get(position).id);
        holder.tvTime.setText(mDataList.get(position).time);
        return convertView;
    }
    class Holder{
        public Holder(View root){
            tvTitle = (TextView) root.findViewById(R.id.item_title);
            tvContent = (TextView) root.findViewById(R.id.item_content);
            tvDataId = (TextView) root.findViewById(R.id.item_data_id);
            tvTime = (TextView) root.findViewById(R.id.item_time);
        }
        TextView tvTitle,tvContent,tvDataId,tvTime;
    }
}
