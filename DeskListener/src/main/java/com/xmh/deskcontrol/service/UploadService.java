package com.xmh.deskcontrol.service;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.xmh.deskcontrol.base.BaseService;
import com.xmh.deskcontrol.biz.NotificationController;
import com.xmh.deskcontrol.utils.FileUtil;
import com.xmh.deskcontrol.utils.NetWorkUtil;
import com.xmh.deskcontrol.utils.UploadUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UploadService extends BaseService {

    /**service是否destory*/
    private boolean isServiceDestoryed =false;
    /**文件上传线程*/
    private Thread uploadThread;

    /**初始化文件上传线程*/
    private void initThread(){
        uploadThread=new Thread(new Runnable() {

            /**需要被上传的文件列表*/
            List<File> list=new ArrayList<>();

            @Override
            public void run() {
                //TODO 如果desk已经移除，则更新剩余file数，upload完成后不再stop本service不再继续run
                //死循环，只要service未被destory就一直循环
                while (!isServiceDestoryed){
                    Log.e("xmh-thread","run");
                    // 如果list为空则扫描并上传，每上传一个就从list中remove，如果为空则重新扫描
                    if(list.size()==0){
                        Log.e("xmh-thread","size:"+list.size());
                        List<File> files = FileUtil.scanRecordFile();
                        if(files!=null&&!files.isEmpty()) {
                            list.addAll(files);
                            UploadUtil.uploadFiles(UploadService.this, list);
                        }
                    }
                }
                //TODO 检查本thread的调用关系，将不应在线程中进行的操作以回调形式放到UI线程中
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
        Log.e("xmh-service-upload","start");
        startForeground(NotificationController.NOTIFICATION_ID,NotificationController.getNotification(this));
        //如果线程正在运行则忽略，否则初始化线程并启动
        if(uploadThread!=null&&uploadThread.isAlive()){
            //do nothing
        }else if(NetWorkUtil.isWiFi(UploadService.this)) {
            //如果是wifi链接状态才执行
            initThread();
            uploadThread.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        isServiceDestoryed =true;
        super.onDestroy();
    }
}
