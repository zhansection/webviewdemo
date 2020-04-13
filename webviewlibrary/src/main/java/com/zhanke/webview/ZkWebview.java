package com.zhanke.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zhanke.webview.listener.CallBackFunction;
import com.zhanke.webview.listener.JsHandler;
import com.zhanke.webview.util.MResource;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhanke on 2020/3/10.
 * Describe
 */
public class ZkWebview extends WebView {

    private JsMethodInterface jsMethodInterface;

    public ZkWebview(Context context) {
        super(context);
        init();
        initJs();
    }

    public ZkWebview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        initJs();
    }

    public ZkWebview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initJs();
    }

    private void initJs(){
        jsMethodInterface = new JsMethodInterface();
        addJavascriptInterface(jsMethodInterface, "AndroidH5");
    }


    @SuppressLint("SetJavaScriptEnabled")
    private void init(){
        WebSettings webSetting = getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(false);
        webSetting.setSupportMultipleWindows(true);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);

        //设置Web视图
        setWebViewClient(new WebViewClient() {
            /**
             * 防止加载网页时调起系统浏览器
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript:" + view.getContext().getString(MResource.getString(view.getContext(),"js_format")));
                super.onPageFinished(view, url);
            }
        });
        setWebChromeClient(new WebChromeClient());
    }

    private  void callJs(final String common, final String action, final String data){
        post(new Runnable() {
            @Override
            public void run() {
                String b =  "javascript:callbacks(\""+common+"\",\"" + action +"\"," + data +")";
                loadUrl("javascript:callbacks(\""+common+"\",\"" + action +"\"," + data +")");
            }
        });
    }

    public void registerHandler(String common, String method, JsHandler jsHandler){
        jsMethodInterface.addJsHandlerMap(common + method,jsHandler);
    }


    class JsMethodInterface{


        Map<String, JsHandler> jsHandlerMap = new HashMap<>();

        public JsMethodInterface() {

        }

        @JavascriptInterface
        public void toAndroidMethod(final String common, final String action, final String data) {
            if (jsHandlerMap.containsKey(common + action)){
                JsHandler jsHandler = jsHandlerMap.get(common + action);
                if (jsHandler != null) {
                    jsHandler.onJsHandler(data, new CallBackFunction() {
                        @Override
                        public void onCallBack(String callData) {
                            callJs(common, action, callData);
                        }
                    });
                }
            }else {
                Log.i("ZkWebview","没有找到配置的方法");
            }
        }

        private void addJsHandlerMap(String action, JsHandler jsHandler){
            if (jsHandlerMap.containsKey(action)) {
                return;
            }
            jsHandlerMap.put(action,jsHandler);
        }

    }


}
