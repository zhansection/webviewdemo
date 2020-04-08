# ZkWebView

## Setup

To use this library your `minSdkVersion` must be >= 17.

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.zhansection:zwebview:1.0.2'
}
```

## Usage

To prevent WebView memory leak, use dynamic addition

Create a `ZkWebView` instance :

```
FrameLayout flvWeb = findViewById(R.id.flv_web);
ZkWebview webView=new ZkWebview(MainActivity.this);
flvWeb.addView(webView);
//注册
webView.registerHandler("UI","toast", new JsHandler() {
            @Override
            public void onJsHandler(String responseData, CallBackFunction function) {
            	Log.i("z","接收到的数据（可以是json字符串）-->" + responseData);
            	function.onCallBack("回调的参数");
            }
        });
```

Destroy webview

```
@Override
protected void onDestroy() {
    if( webView!=null) {
        ViewParent parent = webView.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(webView);
        }
        webView.stopLoading();
        // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
        webView.getSettings().setJavaScriptEnabled(false);
        webView.clearHistory();
        webView.clearView();
        webView.removeAllViews();
        webView.destroy();
    }
    super.onDestroy();

}
```

