package com.xmh.deskcontrol.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.xmh.deskcontrol.base.BaseService;
import com.xmh.deskcontrol.biz.NotificationController;
import com.xmh.deskcontrol.biz.SoundPicker;
import com.xmh.deskcontrol.utils.LogUtil;

/**
 * Created by xmh19 on 2016/2/5 005.
 */
public class RecordService extends BaseService {

    public static final String ACTION_STOP_RECORD="com.xmh.desklistener.ACTION_STOP_RECORD";

    private SoundPicker mPicker;

    /**广播监听，用于停止服务*/
    private BroadcastReceiver mReceiver=new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            //收到停止服务的广播则停止服务
            if(intent.getAction().equals(ACTION_STOP_RECORD)){
                RecordService.this.stopSelf();
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.e("xmh-service","create");
        mPicker=new SoundPicker(RecordService.this);
        registerReceiver(mReceiver,new IntentFilter(ACTION_STOP_RECORD));
    }

    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.e("xmh-service","bind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.e("xmh-service","start");
        mPicker.start();
        startForeground(NotificationController.NOTIFICATION_ID, NotificationController.getNotification(this));
        NotificationController.setRecordServiceStarted(true);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        LogUtil.e("xmh-service","destory");
        try {
            mPicker.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        NotificationController.setRecordServiceStarted(false);
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

}
