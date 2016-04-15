package com.xmh.deskcontrol.service;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.xmh.deskcontrol.base.BaseService;
import com.xmh.deskcontrol.biz.NotificationController;
import com.xmh.deskcontrol.biz.SoundPicker;

/**
 * Created by xmh19 on 2016/2/5 005.
 */
public class RecordService extends BaseService {

    private SoundPicker mPicker;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("xmh-service","create");
        mPicker=new SoundPicker(RecordService.this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e("xmh-service","bind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("xmh-service","start");
        mPicker.start();
        startForeground(NotificationController.NOTIFICATION_ID, NotificationController.getNotification(this));
        NotificationController.setRecordServiceStarted(true);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e("xmh-service","destory");
        try {
            mPicker.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
        NotificationController.setRecordServiceStarted(false);
    }

}
