package com.example.mrlwj.remind.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.example.mrlwj.remind.MyAdapter;
import com.example.mrlwj.remind.R;
import com.example.mrlwj.remind.bean.Bean;
import com.example.mrlwj.remind.utils.NetDataUtils;
import com.example.mrlwj.remind.utils.UiUtils;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainActivity extends ActionBarActivity {
    public static MainActivity context;
    private ListView mlistView;
    private LinkedList<Bean> dataList = new LinkedList<Bean>();
    private MyAdapter myAdapter ;
    public Map<String,Bean> bakMap = new HashMap<String,Bean>();
    public Map<String,Bean> dataMap = new HashMap<String,Bean>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NoHttp.initialize(this);
        Logger.setDebug(true);// 开启NoHttp的调试模式, 配置后可看到请求过程、日志和错误信息。
        Logger.setTag("NoHttpSample");
        setContentView(R.layout.activity_main);
        context = this;
        mlistView = (ListView) findViewById(R.id.lv);
        myAdapter = new MyAdapter(dataList);
        mlistView.setAdapter(myAdapter);
        mlistView.setOnItemLongClickListener(new LongClickListener());

        NetDataUtils.getJson(new NetDataUtils.ResCallBack() {
            @Override
            public void handle(boolean success, String msg, Bean bean) {
                if(!success){
                    return;
                }
                List<Bean> beans = JSON.parseArray(msg,Bean.class);
                if(beans==null||beans.size()==0){
                    return;
                }
                for(Bean item : beans){
                    dataMap.put(item.id,item);
                }
                dataList.addAll(beans);
                myAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            showInputDlg();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 显示输入对话框
    private void showInputDlg(){
        final View dlgLayout = View.inflate(this, R.layout.dlg, null);
        AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(this);
        dlgBuilder.setView(dlgLayout);
        dlgBuilder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText edTitle = (EditText) dlgLayout.findViewById(R.id.dlg_title);
                EditText edContent = (EditText) dlgLayout.findViewById(R.id.dlg_content);
                String title = edTitle.getText().toString().trim();
                String content = edContent.getText().toString().trim();
                NetDataUtils.send(new Bean(title,content),new MyAddCallBack());
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }
    private void showEditDlg(int index){
        final View dlgLayout = View.inflate(this,R.layout.dlg,null);
        final EditText edTitle = (EditText) dlgLayout.findViewById(R.id.dlg_title);
        final EditText edContent = (EditText) dlgLayout.findViewById(R.id.dlg_content);
        final Bean target = dataList.get(index);

        edTitle.setText(target.title);
        edTitle.setFocusable(false);
        edContent.setText(target.content);

        AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(this);
        dlgBuilder.setView(dlgLayout);
        dlgBuilder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bean bak = target.clone();
                bakMap.put(target.getId(), bak); //保存更新后的数据，不马上应用到界面上
                String content = edContent.getText().toString().trim();
                bak.setContent(content);
                NetDataUtils.update(bak,new MyEditCallBack());
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    class MyAddCallBack implements NetDataUtils.ResCallBack{
        @Override
        public void handle(boolean success,String msg,Bean bean) {
            if(!success){
                UiUtils.showToast(msg);
                return;
            }
            dataList.add(bean);
            dataMap.put(bean.id,bean);
            myAdapter.notifyDataSetChanged();
        }
    }
    class MyEditCallBack implements NetDataUtils.ResCallBack{
        @Override
        public void handle(boolean success,String msg,Bean bak) {
            if(!success){
                UiUtils.showToast(msg);
                return;
            }
            UiUtils.showToast("更新成功");
            Bean target = dataMap.get(bak.id);
            target.setContent(bak.content);
            target.lastTime =  NetDataUtils.getCurTime();
            bakMap.remove(bak.getId());
            myAdapter.notifyDataSetChanged();
        }
    }
    class LongClickListener implements AdapterView.OnItemLongClickListener{
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            showOptionDlg(position);
            return false;
        }
    }
    private final String[] ops = new String[]{"移除","编辑","标记为完成"};
    public static final int DEL = 0;
    public static final int EDIT = 1;
    public static final int FINISH = 2;

    private void showOptionDlg(final int index){
       new AlertDialog.Builder(this)
        .setItems(ops, new OnDlgItemClickListener(index))
        .show();
    }

    class OnDlgItemClickListener implements DialogInterface.OnClickListener{
        private int index;

        public OnDlgItemClickListener(int index) {
            this.index = index;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch(which){
                case DEL:
                    NetDataUtils.delItem(dataList.get(index), new NetDataUtils.ResCallBack() {
                        @Override
                        public void handle(boolean success, String msg, Bean bean) {
                            if(success){
                                dataList.remove(index);
                                myAdapter.notifyDataSetChanged();
                            }
                            UiUtils.showToast(success?"刪除成功":"刪除失敗");
                        }
                    });
                    break;
                case EDIT:
                    showEditDlg(index);
                    break;
                case FINISH:
                    break;
            }
        }
    }
}
