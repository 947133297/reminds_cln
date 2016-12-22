package com.example.mrlwj.remind.utils;

import android.widget.Toast;

import com.example.mrlwj.remind.activity.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by MRLWJ on 2016/11/12.
 */
public class UiUtils {
    public static void showToast(String msg){
        Toast.makeText(MainActivity.context,msg,Toast.LENGTH_LONG).show();
    }
}

