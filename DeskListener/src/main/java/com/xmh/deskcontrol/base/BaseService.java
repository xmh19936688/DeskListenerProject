package com.xmh.deskcontrol.base;

import android.app.Service;

import com.xmh.deskcontrol.application.AppConfig;

import cn.bmob.v3.Bmob;

/**
 * Created by mengh on 2016/4/13 013.
 */
public abstract class BaseService extends Service{
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, AppConfig.Bmob_APPID);
    }

}
