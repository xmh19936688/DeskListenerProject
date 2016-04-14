package com.xmh.deskcontrol.utils;

import android.content.Context;
import android.util.Log;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
import com.xmh.deskcontrol.bean.FileBmobBean;
import com.xmh.deskcontrol.biz.NotificationController;

import java.io.File;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by mengh on 2016/4/14 014.
 */
public class UploadUtil {

    public static void uploadFiles(Context context, final List<File>list){
        for (final File file:list){
            UploadUtil.uploadFile(context, file.getAbsolutePath(), new UploadUtil.UploadSuccessCallback() {
                @Override
                public void onUploadSuccessCallback() {
                    list.remove(file);
                    NotificationController.updateFileCount(list.size());
                }
            });
        }
    }

    /**上传video文件*/
    public static void uploadFile(final Context context, final String filePath, final UploadSuccessCallback callback){
        Log.e("xmh-upload","upload");
        BmobProFile.getInstance(context).upload(filePath, new UploadListener() {
            @Override
            public void onSuccess(String filenameForDownload, String oldUrl, BmobFile bmobFile) {
                Log.e("xmh-upload","success");
                //上传完成后保存到bmob数据库
                FileBmobBean fileBmobBean = new FileBmobBean();
                fileBmobBean.setSoundFile(bmobFile);
                fileBmobBean.setFilenameForDownload(filenameForDownload);
                fileBmobBean.save(context);
                //上传完成后删除文件
                FileUtil.deleteFile(filePath);
                if(callback!=null){
                    callback.onUploadSuccessCallback();
                }
                NotificationController.updateUploadCount();
            }

            @Override
            public void onProgress(int i) {
                //do nothing
            }

            @Override
            public void onError(int i, String s) {
                //do nothing
            }
        });
    }

    public interface UploadSuccessCallback{
        void onUploadSuccessCallback();
    }
}
