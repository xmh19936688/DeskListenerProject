package com.xmh.deskcontrol.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.xmh.deskcontrol.R;
import com.xmh.deskcontrol.utils.SoundPicker;

/**
 * Created by xmh19 on 2016/2/5 005.
 */
public class RecordService extends Service {

    private static final int NOTIFICATION_ID=1001;

    private SoundPicker mPicker;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("xmh-service","create");
        mPicker=new SoundPicker();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("xmh-service","bind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("xmh-service","start");
        mPicker.start();
        initNotification();
        return super.onStartCommand(intent, flags, startId);
    }

    private void initNotification() {
        RemoteViews view = new RemoteViews(getPackageName(), R.layout.layout_notification);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContent(view)
                .setSmallIcon(R.drawable.ic_launcher)//此项为必须
                .setOngoing(true);
        startForeground(NOTIFICATION_ID, builder.build());
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
    }

}
