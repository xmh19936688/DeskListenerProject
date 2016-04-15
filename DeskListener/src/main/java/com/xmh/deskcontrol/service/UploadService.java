package com.xmh.deskcontrol.service;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.xmh.deskcontrol.base.BaseService;
import com.xmh.deskcontrol.biz.NotificationController;
import com.xmh.deskcontrol.utils.FileUtil;
import com.xmh.deskcontrol.utils.NetWorkUtil;
import com.xmh.deskcontrol.utils.UploadUtil;

public class UploadService extends BaseService {

    /**service是否destory*/
    private boolean isServiceDestoryed =false;
    /**文件上传线程*/
    private Thread uploadThread;

    /**初始化文件上传线程*/
    private void initThread(){
        uploadThread=new Thread(new Runnable() {

            @Override
            public void run() {
                String[] pathArray = FileUtil.scanRecordFilePath();
                if(pathArray!=null&&pathArray.length>0) {
                    NotificationController.updateFileCount(pathArray.length);
                    LogUtil.e("xmh-thread","size:"+pathArray.length);
                    UploadUtil.uploadFiles(UploadService.this, pathArray, new UploadUtil.UploadSuccessCallback() {
                        @Override
                        public void onUploadSuccessCallback() {
                            UploadService.this.stopSelf();
                        }
                    });
                }else {
                    //没有数据则直接关闭service
                    UploadService.this.stopSelf();
                }
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.e("xmh-service-upload","start");
        startForeground(NotificationController.NOTIFICATION_ID,NotificationController.getNotification(this));
        if(uploadThread!=null&&uploadThread.isAlive()){
            //如果线程正在运行则忽略
        }else if(NetWorkUtil.isWiFi(UploadService.this)) {
            //如果是wifi链接状态初始化线程并启动
            initThread();
            uploadThread.start();
        }else {
            //没网直接关闭
            stopSelf();
        }
        NotificationController.setUploadServiceStarted(true);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        LogUtil.e("xmh-service-upload","stop");
        isServiceDestoryed =true;
        NotificationController.setUploadServiceStarted(false);
        super.onDestroy();
    }
}
