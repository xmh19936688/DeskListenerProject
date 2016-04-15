package com.xmh.deskcontrol.biz;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.xmh.deskcontrol.R;
import com.xmh.deskcontrol.service.RecordService;

/**
 * Created by mengh on 2016/4/13 013.
 */
public class NotificationController {

    /**通知id*/
    public static final int NOTIFICATION_ID=1001;

    private static Context mContext;
    /**通知构造器*/
    private static NotificationCompat.Builder mBuilder;
    /**通知内容*/
    private static RemoteViews view;
    /**待上传文件数*/
    private static int fileCount=0;
    /**已上传文件数*/
    private static int uploadCount=0;
    /**RecordService是否正在运行*/
    private static boolean isRecordServiceStarted;
    /**UploadService是否正在运行*/
    private static boolean isUploadServiceStarted;

    private NotificationController(){}

    /**获取共享的通知*/
    public static Notification getNotification(Context context){
        mContext=context;
        if(mBuilder==null){
            initNotification(context);
        }
        return  mBuilder.build();
    }

    /**初始化通知*/
    private static void initNotification(Context context) {
        view = new RemoteViews(context.getPackageName(), R.layout.layout_notification);
        //region set ui layout
        if(isRecordServiceStarted) {
            view.setTextViewText(R.id.tv_going_left, "←");
            view.setTextViewText(R.id.tv_going_right, "→");
        }else {
            view.setTextViewText(R.id.tv_going_left, "-");
            view.setTextViewText(R.id.tv_going_right, "-");
        }
        if(!isUploadServiceStarted) {
            view.setTextViewText(R.id.tv_file_count, "·");
            view.setTextViewText(R.id.tv_upload_count, "·");
        }
        //endregion
        //region set view listener
        Intent intent = new Intent();
        intent.setAction(RecordService.ACTION_STOP_RECORD);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(mContext,0,intent,0);
        view.setOnClickPendingIntent(R.id.iv_controll,pendingIntent);
        //endregion
        mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContent(view)
                .setSmallIcon(R.drawable.ic_launcher)//此项为必须
                .setOngoing(true);
    }

    /**更新待上传文件数*/
    public static void updateFileCount(int count){
        fileCount=count;
        if(view==null){
            return;
        }
        view.setTextViewText(R.id.tv_file_count,""+fileCount);
        updateNotification();
    }

    public static void decreaseFileCount(){
        fileCount--;
        if(view==null){
            return;
        }
        view.setTextViewText(R.id.tv_file_count,""+fileCount);
        updateNotification();
    }

    /**增加已上传文件数*/
    public static void increaseUploadCount(){
        uploadCount++;
        if(view==null){
            return;
        }
        view.setTextViewText(R.id.tv_upload_count,""+uploadCount);
        updateNotification();
    }
    /**增加已上传文件数*/
    public static void increaseUploadCount(int count){
        uploadCount+=count;
        if(view==null){
            return;
        }
        view.setTextViewText(R.id.tv_upload_count,""+uploadCount);
        updateNotification();
    }

    /**更新通知内容*/
    private static void updateNotification() {
        if(mBuilder==null){
            return;
        }
        mBuilder.setContent(view);
        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    /**取消通知*/
    private static void cancleNotification(){
        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(NOTIFICATION_ID);
        mBuilder=null;//置空后，下次再get时将会重新初始化
    }

    /**设置RecordService状态*/
    public static void setRecordServiceStarted(boolean isStarted){
        isRecordServiceStarted=isStarted;
        //如果都已结束，则取消通知
        if(!isRecordServiceStarted&&!isUploadServiceStarted){
            cancleNotification();
            return;
        }
        //更新通知UI
        if(isStarted) {
            view.setTextViewText(R.id.tv_going_left, "←");
            view.setTextViewText(R.id.tv_going_right, "→");
        }else {
            view.setTextViewText(R.id.tv_going_left, "-");
            view.setTextViewText(R.id.tv_going_right, "-");
        }
        updateNotification();
    }

    /**设置UploadService状态*/
    public static void setUploadServiceStarted(boolean isStarted){
        isUploadServiceStarted=isStarted;
        //如果都已结束，则取消通知
        if(!isRecordServiceStarted&&!isUploadServiceStarted){
            cancleNotification();
            return;
        }
        //更新通知UI
        if(!isStarted) {
            view.setTextViewText(R.id.tv_file_count, "√");
            view.setTextViewText(R.id.tv_upload_count, "√");
            updateNotification();
        }
    }

}
