package com.zhanke.demo;

import android.app.Application;

import com.zhanke.webview.util.X5Web;

/**
 * Created by zhanke on 2020/4/13.
 * Describe
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        X5Web.init(getApplicationContext());
    }
}
