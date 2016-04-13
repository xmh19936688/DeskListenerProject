package com.xmh.deskcontrol.utils;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.File;

/**
 * Created by mengh on 2016/4/13 013.
 */
public class SoundPicker {

    /**每次录音持续时长*/
    private static final int TIME_PER_RECORD=30 * 1000;

    private MediaRecorder mRecorder;
    private ControlThread mThread;

    /**录音是否开启*/
    private boolean isStarted =false;
    /**当前录音文件*/
    private File currentSoundFile;

    /**录音*/
    private void record() {
        resetRecorder();
        //录音
        try {
            // 创建保存录音的音频文件
            currentSoundFile = FileUtil.getFileByTime();
            // 开始录音
            prepareRecorder();
            mRecorder.setOutputFile(currentSoundFile.getAbsolutePath());
            mRecorder.prepare();
            mRecorder.start();
            Log.e("xmh-picker","record");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void resetRecorder() {
        //如果正在录音则终止
        if(mRecorder!=null){
            // 停止录音
            mRecorder.stop();
            // 释放资源
            mRecorder.release();
            mRecorder = null;
            Log.e("xmh-picker","reset");
        }
    }

    private void prepareRecorder() {
        mRecorder = new MediaRecorder();
        // 设置录音的声音来源(必须在输出格式之前)
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // 设置录制的声音的输出格式（必须在设置声音编码格式之前设置）
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        // 设置声音编码的格式
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        Log.e("xmh-picker","prepare");
    }

    /**开始录音*/
    public void start(){
        isStarted =true;
        if(mThread==null){
            mThread=new ControlThread();
        }
        mThread.start();
        Log.e("xmh-picker","start");
    }

    /**停止录音*/
    public void stop() {
        isStarted =false;
        resetRecorder();
        Log.e("xmh-picker","stop");
    }

    /**控制线程*/
    class ControlThread extends Thread{
        @Override
        public void run() {
            super.run();
            Log.e("xmh-picker","run");
            try {
                while (isStarted) {
                    Thread.sleep(TIME_PER_RECORD);
                    record();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
