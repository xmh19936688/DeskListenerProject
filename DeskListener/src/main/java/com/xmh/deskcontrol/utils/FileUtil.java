package com.xmh.deskcontrol.utils;

import android.os.Environment;
import android.util.Log;

import com.xmh.deskcontrol.filter.RecorderFileFilter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mengh on 2016/4/13 013.
 */
public class FileUtil {

    private static final String RECORD_FILE_PATH="/xmh/_rec/";
    private static final String NOMEDIA_FILENAME = ".nomedia";
    private static final String DATE_FORMAT="yyyyMMddHHmmss";

    private FileUtil(){}

    /**根据sd卡状态获取文件路径*/
    public static String getFilePath(){
        if (checkSDCardAvailable()) {
            return getRecordPathOnSD();
        }
        return getRecordPathOnPhone();
    }

    /**根据当前时间获取文件*/
    public static File getFileByTime(){
        Date currentDate = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        String filename=simpleDateFormat.format(currentDate).toString();
        try {
            File soundFile = new File(getFilePath() + filename + ".3gp");
            return soundFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**检查sd卡是否可用*/
    public static boolean checkSDCardAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**sd卡上的目录*/
    public static String getRecordPathOnSD() {
        String path = Environment.getExternalStorageDirectory() + RECORD_FILE_PATH;
        File file = new File(path);
        if(!file.exists())
            file.mkdirs();
        return path;
    }

    /**手机上的目录*/
    private static String getRecordPathOnPhone() {
        String path = Environment.getDataDirectory() + RECORD_FILE_PATH;
        File file=new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        return path;
    }

    /**创建nomedia文件*/
    public static void checkAndCreateNomedia() {
        try {
            File folder = new File(getRecordPathOnPhone());
            if (folder.exists()) {
                File file = new File(getRecordPathOnPhone() + NOMEDIA_FILENAME);
                if (!file.exists()) file.createNewFile();
            }
            folder = new File(getRecordPathOnSD());
            if (folder.exists()) {
                File file = new File(getRecordPathOnSD() + NOMEDIA_FILENAME);
                if (!file.exists()) file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<File> scanRecordFile() {
        Log.e("xmh-file","scan");
        List<File> list=new ArrayList<>();
        File sdFolder = new File(getRecordPathOnSD());
        File phoneFolder = new File(getRecordPathOnPhone());
        File[] files = sdFolder.listFiles(new RecorderFileFilter());
        if(files!=null&&files.length>0) {
            for (File file : files) {
                list.add(file);
            }
        }
        files=phoneFolder.listFiles(new RecorderFileFilter());
        if(files!=null&&files.length>0) {
            for (File file : files) {
                list.add(file);
            }
        }
        return list;
    }
    public static String[] scanRecordFilePath() {
        Log.e("xmh-file","scan");
        List<String> pathList=new ArrayList<>();
        File sdFolder = new File(getRecordPathOnSD());
        File[] list1 = sdFolder.listFiles(new RecorderFileFilter());
        if(list1!=null&&list1.length>0) {
            for (File file : list1) {
                pathList.add(file.getAbsolutePath());
            }
        }
        File phoneFolder = new File(getRecordPathOnPhone());
        File[] list2 = phoneFolder.listFiles(new RecorderFileFilter());
        if(list2!=null&&list2.length>0) {
            for (File file : list2) {
                pathList.add(file.getAbsolutePath());
            }
        }
        return pathList.toArray(new String[pathList.size()]);
    }

    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if(file.exists()){
            file.delete();
        }
    }
}
