package com.zhanke.webview.util;

import android.content.Context;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;

/**
 * Created by zhanke on 2020/4/13.
 * Describe
 */
public class X5Web {

    public static void init(Context context){
        //x5内核初始化接口
        QbSdk.initX5Environment(context,  new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.i("X5Web", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
            }
        });
    }
}
