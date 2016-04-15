package com.xmh.deskcontrol.utils;

import android.content.Context;
import android.util.Log;

import com.xmh.deskcontrol.bean.FileBmobBean;
import com.xmh.deskcontrol.biz.NotificationController;

import java.io.File;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by mengh on 2016/4/14 014.
 */
public class UploadUtil {

    public static void uploadFiles(final Context context, final String[] pathArray, final UploadSuccessCallback callback){
        BmobFile.uploadBatch(context, pathArray, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                LogUtil.e("xmh-upload-array","success");
                //上传完成后保存到bmob数据库
                NotificationController.increaseUploadCount(files.size());
                NotificationController.updateFileCount(pathArray.length-files.size());
                for(BmobFile file:files){
                    FileBmobBean fileBmobBean = new FileBmobBean();
                    fileBmobBean.setSoundFile(file);
                    fileBmobBean.save(context);
                }
                //上传完成后删除文件
                for(String path:pathArray) {
                    FileUtil.deleteFile(path);
                }
                if(callback!=null){
                    callback.onUploadSuccessCallback();
                }
            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {

            }

            @Override
            public void onError(int i, String s) {
                LogUtil.e("xmh-upload-array","error:"+s);
            }
        });
    }

    /**上传video文件*/
    public static void uploadFile(final Context context, final String filePath, final UploadSuccessCallback callback){
        LogUtil.e("xmh-upload","upload");
        final BmobFile bmobFile=new BmobFile(new File(filePath));
        bmobFile.uploadblock(context, new UploadFileListener() {
            @Override
            public void onSuccess() {
                LogUtil.e("xmh-upload-file","success");
                //上传完成后保存到bmob数据库
                FileBmobBean fileBmobBean = new FileBmobBean();
                fileBmobBean.setSoundFile(bmobFile);
                fileBmobBean.save(context);
                //上传完成后删除文件
                FileUtil.deleteFile(filePath);
                if(callback!=null){
                    callback.onUploadSuccessCallback();
                }
                NotificationController.increaseUploadCount();
            }

            @Override
            public void onFailure(int i, String s) {
                LogUtil.e("xmh-upload-file","fail:"+s);

            }
        });
    }

    public interface UploadSuccessCallback{
        void onUploadSuccessCallback();
    }
}
