package com.xmh.deskcontrol.widget;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.xmh.deskcontrol.service.RecordService;
import com.xmh.deskcontrol.service.UploadService;
import com.xmh.deskcontrol.utils.FileUtil;

public class DeskWidget extends AppWidgetProvider{

	//向桌面上添加第一个控件时调用
	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		Log.e("xmh-desk","enable");
		FileUtil.checkAndCreateNomedia();
		context.startService(new Intent(context, RecordService.class));
		context.startService(new Intent(context, UploadService.class));
	}

	//最后一个控件从桌面被删除
	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		Log.e("xmh-desk","disable");
		context.stopService(new Intent(context, RecordService.class));
		context.stopService(new Intent(context, UploadService.class));
	}
}
