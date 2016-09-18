package com.curiosity.mycurriculum.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Curiosity on 2016-9-17.
 * 网络工具类
 */
public class NetWorkUtils {
    private NetWorkUtils() {
    }

    /**
     * 检查网络是否可连接
     * @return 网络是否可连接
     */
    public static boolean isNetWorkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }
}
