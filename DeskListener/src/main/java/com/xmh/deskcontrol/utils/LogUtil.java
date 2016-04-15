package com.xmh.deskcontrol.utils;

import android.util.Log;

import com.xmh.deskcontrol.application.AppConfig;

/**
 * Created by mengh on 2016/4/15 015.
 */
public class LogUtil {

    public static void e(String tag,String text){
        if(AppConfig.isDebug){
            Log.e(tag,text);
        }
    }

}
