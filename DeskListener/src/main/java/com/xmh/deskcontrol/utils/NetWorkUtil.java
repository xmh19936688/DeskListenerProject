package com.xmh.deskcontrol.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by mengh on 2016/4/14 014.
 */
public class NetWorkUtil {

    /**
     * 只是判断WIFI
     *
     * @param context 上下文
     * @return 是否打开Wifi
     */
    public static boolean isWiFi(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING)
            return true;
        return false;
    }

}
