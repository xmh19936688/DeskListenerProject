package com.xmh.deskcontrol.bean;

import android.os.Build;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by mengh on 2016/4/14 014.
 */
public class FileBmobBean extends BmobObject{

    /**使用设备名初始化该字段*/
    private String deviceName= Build.DEVICE;

    /**文件信息*/
    private BmobFile soundFile;

    /**上传成功后用于下载的文件名（bmob后台生成的文件唯一标识）*/
    private String filenameForDownload;

    //region contructor
    public FileBmobBean() {
    }
    public FileBmobBean(BmobFile soundFile) {
        this.soundFile = soundFile;
    }
    //endregion


    //region get&set
    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public BmobFile getSoundFile() {
        return soundFile;
    }

    public void setSoundFile(BmobFile soundFile) {
        this.soundFile = soundFile;
    }

    public String getFilenameForDownload() {
        return filenameForDownload;
    }

    public void setFilenameForDownload(String filenameForDownload) {
        this.filenameForDownload = filenameForDownload;
    }
    //endregion
}
