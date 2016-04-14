package com.xmh.deskcontrol.biz;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.xmh.deskcontrol.R;

/**
 * Created by mengh on 2016/4/13 013.
 */
public class NotificationController {

    public static final int NOTIFICATION_ID=1001;

    private static Context mContext;
    private static NotificationCompat.Builder mBuilder;
    private static RemoteViews view;
    private static int fileCount=0;
    private static int uploadCount=0;

    private NotificationController(){}

    /**获取共享的通知*/
    public static Notification getNotification(Context context){
        mContext=context;
        if(mBuilder==null){
            initNotification(context);
        }
        return  mBuilder.build();
    }

    private static void initNotification(Context context) {
        view = new RemoteViews(context.getPackageName(), R.layout.layout_notification);
        mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContent(view)
                .setSmallIcon(R.drawable.ic_launcher)//此项为必须
                .setOngoing(true);
    }

    public static void updateFileCount(int count){
        fileCount=count;
        if(view==null){
            return;
        }
        view.setTextViewText(R.id.tv_file_count,""+fileCount);
        updateNotification();
    }

    public static void updateUploadCount(){
        uploadCount++;
        if(view==null){
            return;
        }
        view.setTextViewText(R.id.tv_upload_count,""+uploadCount);
        updateNotification();
    }

    private static void updateNotification() {
        mBuilder.setContent(view);
        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

}
