package com.xmh.deskcontrol.biz;

import android.app.Notification;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.xmh.deskcontrol.R;

/**
 * Created by mengh on 2016/4/13 013.
 */
public class NotificationController {

    public static final int NOTIFICATION_ID=1001;

    private static NotificationCompat.Builder mBuilder;

    public static Notification getNotification(Context context){
        if(mBuilder==null){
            initNotification(context);
        }
        return  mBuilder.build();
    }

    private static void initNotification(Context context) {
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.layout_notification);
        mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContent(view)
                .setSmallIcon(R.drawable.ic_launcher)//此项为必须
                .setOngoing(true);
    }
}
