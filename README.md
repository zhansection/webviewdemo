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
    implementation 'com.github.zhansection:zwebview:1.0.3'
}
```

## Usage

Android To prevent WebView memory leak, use dynamic addition

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

Js instructions for use:

**Call method：**

```
appservice(component, action, params, callback)
```

**Parameter description：**

```
"component" : "组件名称",
"action"    : "方法名称",
"params"    : {具体参数列表json对象}
"callback"  : 回调
```

**callback  The unified format of the returned result JSON is as follows:**

```json
{
    "error":0, //错误码，0为成功
    "msg":"success", //错误信息
    "content":{ //返回的结果，json格式
    }
}
```

**Call example：**

```javascript
// 获取设备信息
appservice('common', 'getDeviceInfo', null, function(res) {
    if (res && res.error==0) {
        consolo.log('设备id', res.data.deviceId)
    }
})
```

