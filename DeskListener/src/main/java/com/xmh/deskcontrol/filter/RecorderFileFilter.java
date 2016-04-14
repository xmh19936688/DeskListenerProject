package com.xmh.deskcontrol.filter;

import com.xmh.deskcontrol.utils.FileUtil;
import com.xmh.deskcontrol.biz.SoundPicker;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by mengh on 2016/4/14 014.
 */
public class RecorderFileFilter implements FileFilter {

    @Override
    public boolean accept(File file) {
        //如果目录则pass
        if (file.isDirectory())
            return false;
        String fileName = file.getName();
        //如果文件正在使用则pass
        if(fileName.equals(SoundPicker.getCurrentSoundFileName()))
            return false;
        //如果文件名长度不符则pass
        if (fileName.length() != FileUtil.getFileByTime().getName().length())
            return false;
        //如果文件不足1k，直接删除
        if(file.length()<1024){
            file.delete();
            return false;
        }
        //TODO 用正则表达式过滤
        return true;
    }
}
