package com.xmh.musicnotification;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.widget.RemoteViews;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    /**
     * 控制器通知（应用内通知唯一标识符）
     */
    private static final int NOTIFYCATION_ID_CONTROLLER = 1;

    private NotificationManager mNotificationManager;
    private ServiceReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);

        initReceiver();
    }

    private void initReceiver() {
        receiver = new ServiceReceiver();//----注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ServiceReceiver.NOTIFICATION_ITEM_BUTTON_LAST);
        intentFilter.addAction(ServiceReceiver.NOTIFICATION_ITEM_BUTTON_PLAY);
        intentFilter.addAction(ServiceReceiver.NOTIFICATION_ITEM_BUTTON_NEXT);
        registerReceiver(receiver, intentFilter);
    }

    @OnClick(R.id.btn_show)
    void onShowClick() {

        //region 初始化view
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.music_notification);

        remoteViews.setTextViewText(R.id.title_music_name, "稻香-周杰伦"); //设置textview

        //设置按钮事件 -- 发送广播 --广播接收后进行对应的处理

        Intent buttonPlayIntent = new Intent(ServiceReceiver.NOTIFICATION_ITEM_BUTTON_LAST); //----设置通知栏按钮广播
        PendingIntent pendButtonPlayIntent = PendingIntent.getBroadcast(MainActivity.this, 0, buttonPlayIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.last_music, pendButtonPlayIntent);//----设置对应的按钮ID监控


        Intent buttonPlayIntent1 = new Intent(ServiceReceiver.NOTIFICATION_ITEM_BUTTON_PLAY); //----设置通知栏按钮广播
        PendingIntent pendButtonPlayIntent1 = PendingIntent.getBroadcast(MainActivity.this, 0, buttonPlayIntent1, 0);
        remoteViews.setOnClickPendingIntent(R.id.paly_pause_music, pendButtonPlayIntent1);//----设置对应的按钮ID监控

        Intent buttonPlayIntent2 = new Intent(ServiceReceiver.NOTIFICATION_ITEM_BUTTON_NEXT); //----设置通知栏按钮广播
        PendingIntent pendButtonPlayIntent2 = PendingIntent.getBroadcast(MainActivity.this, 0, buttonPlayIntent2, 0);
        remoteViews.setOnClickPendingIntent(R.id.next_music, pendButtonPlayIntent2);//----设置对应的按钮ID监控
        //endregion

        //创建一个通知
        NotificationCompat.Builder builder = new Builder(MainActivity.this);
        builder.setContent(remoteViews)//设置自定义view
                .setSmallIcon(R.drawable.ic_launcher)//设置icon
                .setOngoing(true)//设置正在进行中的通知，不能被remove
                .setTicker("music is playing");//首次显示通知时在状态栏的提示内容

        //获取通知服务发通知
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //发通知
        mNotificationManager.notify(NOTIFYCATION_ID_CONTROLLER, builder.build());
    }
}
