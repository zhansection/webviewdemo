package com.zhanke.demo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhanke.demo.dialog.WaitPorgressDialog;
import com.zhanke.webview.ZkWebview;
import com.zhanke.webview.bean.BaseBean;
import com.zhanke.webview.bean.android.AppInfoBean;
import com.zhanke.webview.bean.android.ButtonConfigBean;
import com.zhanke.webview.bean.android.ClipboardBean;
import com.zhanke.webview.bean.android.ContactListBean;
import com.zhanke.webview.bean.android.DataCacheBean;
import com.zhanke.webview.bean.android.LocationBean;
import com.zhanke.webview.bean.android.SmsInfoBean;
import com.zhanke.webview.bean.js.AlertHintBean;
import com.zhanke.webview.bean.js.JumpBean;
import com.zhanke.webview.bean.js.ToastMsgBean;
import com.zhanke.webview.listener.CallBackFunction;
import com.zhanke.webview.listener.JsHandler;
import com.zhanke.webview.listener.PermissonListener;
import com.zhanke.webview.util.CleanDataUtils;
import com.zhanke.webview.util.MResource;
import com.zhanke.webview.util.Utils;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.functions.Consumer;


public class MainActivity extends AppCompatActivity {

    ZkWebview webView;
    WaitPorgressDialog mWaitPorgressDialog;
    Handler handler;
    RxPermissions rxPermissions;
    private CallBackFunction qrCodeCall;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout flvWeb = findViewById(R.id.flv_web);

        rxPermissions = new RxPermissions(this);

        mWaitPorgressDialog = new WaitPorgressDialog(MainActivity.this);
        handler = new Handler(Looper.myLooper());
        webView=new ZkWebview(MainActivity.this);

        flvWeb.addView(webView);

        //获取设备信息 webview and js interactive project construction
        webView.registerHandler("Common","getDeviceInfo", new JsHandler() {
            @Override
            public void onJsHandler(String responseData, CallBackFunction function) {
                Log.e("zz","getDeviceInfo-->" + responseData);

                BaseBean baseBean = new BaseBean();
                AppInfoBean infoBean = new AppInfoBean();
                infoBean.setAppVersion(Utils.getVersionName(MainActivity.this));
                infoBean.setImei(Utils.getPhoneIMEI(MainActivity.this));
                infoBean.setUuid(Utils.getUUID(MainActivity.this));
                infoBean.setModel(Utils.getPhoneModel());
                infoBean.setOsVersion(Utils.getOsVersion());
                int[] device = Utils.getDevice(MainActivity.this);
                infoBean.setDeviceWidth(String.valueOf(device[0]));
                infoBean.setDeviceHeight(String.valueOf(device[1]));
                baseBean.setContent(infoBean);
                function.onCallBack(Utils.toJson(baseBean));
            }
        });

        //把内容放到剪贴板中
        webView.registerHandler("Common","setClipboard", new JsHandler() {
            @Override
            public void onJsHandler(String responseData, CallBackFunction function) {
                Log.e("zz","setClipboard-->" + responseData);
                Utils.setClipboardText(MainActivity.this,responseData);
                BaseBean baseBean = new BaseBean();
                function.onCallBack(Utils.toJson(baseBean));
            }
        });
        //二维码
        //得到剪贴板内容A Android library for qrcode scanning and generating, depends on zxing library
        webView.registerHandler("Common","getClipboard", new JsHandler() {
            @Override
            public void onJsHandler(String responseData, CallBackFunction function) {
                Log.e("zz","getClipboard-->" + responseData);
                String text =  Utils.getClipboardText(MainActivity.this);
                BaseBean baseBean = new BaseBean();
                ClipboardBean cacheBean = new ClipboardBean();
                cacheBean.setText(text);
                baseBean.setContent(cacheBean);
                function.onCallBack(Utils.toJson(baseBean));
            }
        });

        //获取应用缓存
        webView.registerHandler("Common","getCacheSize", new JsHandler() {
            @Override
            public void onJsHandler(String responseData, CallBackFunction function) {
                Log.e("zz","getCacheSize-->" + responseData);
                String size = CleanDataUtils.getTotalCacheSize(MainActivity.this);
                BaseBean baseBean = new BaseBean();
                DataCacheBean cacheBean = new DataCacheBean();
                cacheBean.setSize(size);
                baseBean.setContent(cacheBean);
                function.onCallBack(Utils.toJson(baseBean));
            }
        });

        //清除应用缓存
        webView.registerHandler("Common","clearCache", new JsHandler() {
            @Override
            public void onJsHandler(String responseData, CallBackFunction function) {
                Log.e("zz","clearCache-->" + responseData);
                CleanDataUtils.clearAllCache(MainActivity.this);
                BaseBean baseBean = new BaseBean();
                function.onCallBack(Utils.toJson(baseBean));
            }
        });

        //打开外部浏览器
        webView.registerHandler("Common","jumpBrowser", new JsHandler() {
            @Override
            public void onJsHandler(String responseData, CallBackFunction function) {
                Log.e("zz","jumpBrowser-->" + responseData);
                Gson gson = new Gson();
                JumpBean jumpBean = gson.fromJson(responseData,JumpBean.class);
                Intent intent= new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(jumpBean.getUrl());
                intent.setData(content_url);
                startActivity(intent);
                BaseBean baseBean = new BaseBean();
                function.onCallBack(Utils.toJson(baseBean));
            }
        });

        //截屏并保存图片
        webView.registerHandler("Common","saveImg", new JsHandler() {
            @Override
            public void onJsHandler(String responseData, CallBackFunction function) {
                Log.e("zz","saveImg-->" + responseData);
                captureScreenWindow();
                BaseBean baseBean = new BaseBean();
                function.onCallBack(Utils.toJson(baseBean));
            }
        });

        //获取联系人列表
        webView.registerHandler("Common","getContactList", new JsHandler() {
            @Override
            public void onJsHandler(String responseData, CallBackFunction function) {
                //TODO 权限没有适配
                Log.e("zz","getContactList-->" + responseData);
                BaseBean baseBean = new BaseBean();
                ContactListBean contactListBean = new ContactListBean();
                contactListBean.setList(Utils.getPhoneContact(MainActivity.this));
                baseBean.setContent(contactListBean);
                function.onCallBack(Utils.toJson(baseBean));
            }
        });

        //获取短信验证码
        webView.registerHandler("Common","getSms", new JsHandler() {
            @Override
            public void onJsHandler(String responseData, CallBackFunction function) {
                //TODO 权限没有适配
                Log.e("zz","getSMSCode-->" + responseData);
                getsmsPermisson(function);
            }
        });

        //显示加载等待框
        webView.registerHandler("UI","showLoading", new JsHandler() {
            @Override
            public void onJsHandler(String responseData, CallBackFunction function) {
                Log.e("zz","showLoading-->" + responseData);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mWaitPorgressDialog.show();
                    }
                });

                BaseBean baseBean = new BaseBean();
                function.onCallBack(Utils.toJson(baseBean));
            }
        });

        //隐藏加载等待框
        webView.registerHandler("UI","hideLoading", new JsHandler() {
            @Override
            public void onJsHandler(String responseData, CallBackFunction function) {
                Log.e("zz","hideLoading-->" + responseData);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mWaitPorgressDialog != null && mWaitPorgressDialog.isShowing()) {
                            mWaitPorgressDialog.dismiss();
                        }
                    }
                });

                BaseBean baseBean = new BaseBean();
                function.onCallBack(Utils.toJson(baseBean));
            }
        });

        //显示toast
        webView.registerHandler("UI","toast", new JsHandler() {
            @Override
            public void onJsHandler(String responseData, CallBackFunction function) {
                Log.e("zz","toast-->" + responseData);
                Gson gson = new Gson();
                ToastMsgBean msgBean = gson.fromJson(responseData,ToastMsgBean.class);
                Toast.makeText(MainActivity.this,msgBean.getMessage(),Toast.LENGTH_SHORT).show();
                BaseBean baseBean = new BaseBean();
                function.onCallBack(Utils.toJson(baseBean));
            }
        });

        //显示确认的弹框
        webView.registerHandler("UI","alert", new JsHandler() {
            @Override
            public void onJsHandler(String responseData, CallBackFunction function) {
                Log.e("zz","alert-->" + responseData);
                Gson gson = new Gson();
                AlertHintBean msgBean = gson.fromJson(responseData,AlertHintBean.class);
                alertHintShow(msgBean,function);
            }
        });

        //显示确认的弹框
        webView.registerHandler("UI","confirm", new JsHandler() {
            @Override
            public void onJsHandler(String responseData, CallBackFunction function) {
                Log.e("zz","confirm-->" + responseData);
                Gson gson = new Gson();
                AlertHintBean msgBean = gson.fromJson(responseData,AlertHintBean.class);
                alertShow(msgBean,function);
            }
        });

        //扫二维码功能
        webView.registerHandler("ScanCode","gotoScanCode", new JsHandler() {
            @Override
            public void onJsHandler(String responseData, CallBackFunction function) {
                Log.e("zz","gotoScanCode-->" + responseData);
                startQrCode(function);
            }
        });


        //获取经纬度
        webView.registerHandler("Location","getLocation", new JsHandler() {
            @Override
            public void onJsHandler(String responseData, CallBackFunction function) {
                Log.e("zz","getLocation-->" + responseData);
                locationMethod(function);
            }
        });

        //安装app
        webView.registerHandler("App","appInstalled", new JsHandler() {
            @Override
            public void onJsHandler(String responseData, CallBackFunction function) {

            }
        });
        //打开app
        webView.registerHandler("App","openApp", new JsHandler() {
            @Override
            public void onJsHandler(String responseData, CallBackFunction function) {

            }
        });
        //退出
        webView.registerHandler("App","exit", new JsHandler() {
            @Override
            public void onJsHandler(String responseData, CallBackFunction function) {
                Log.e("zz","exit-->" + responseData);
                finish();
                System.exit(0);
            }
        });

        String url =  getResources().getString(MResource.getString(this,"ServiceUrl"));
        if (TextUtils.isEmpty(url)) {
            webView.loadUrl("http://t.h.xgame999.com/appservice.html");
        } else {
            webView.loadUrl(url);
        }


        rxPermissions.request(Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe();


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK ){
            if (webView.canGoBack()){
                Log.e("zz","webview关闭");
                webView.goBack();
                return true;
            }
        }
        Log.e("zz","页面关闭了");
        return super.onKeyDown(keyCode, event);
    }


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


    private void alertShow(AlertHintBean hintBean, final CallBackFunction function){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(hintBean.getTitle());
        builder.setMessage(hintBean.getMessage());
        builder.setPositiveButton(hintBean.getConfirmButton(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BaseBean baseBean = new BaseBean();
                ButtonConfigBean configBean = new ButtonConfigBean();
                configBean.setIndex("1");
                baseBean.setContent(configBean);
                function.onCallBack(Utils.toJson(baseBean));
            }
        });
        builder.setNegativeButton(hintBean.getCancelButton(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BaseBean baseBean = new BaseBean();
                ButtonConfigBean configBean = new ButtonConfigBean();
                configBean.setIndex("0");
                baseBean.setContent(configBean);
                function.onCallBack(Utils.toJson(baseBean));
            }
        });
        builder.show();
    }

    private void alertHintShow(AlertHintBean hintBean, final CallBackFunction function){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(hintBean.getTitle());
        builder.setMessage(hintBean.getMessage());
        builder.setPositiveButton(hintBean.getConfirmButton(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BaseBean baseBean = new BaseBean();
                function.onCallBack(Utils.toJson(baseBean));
            }
        });
        builder.show();
    }

    /**
     * 截取全屏
     * @return
     */
    public void captureScreenWindow() {
        View view = getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache(true);
        Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false); // clear drawing cache
        Utils.saveToSystemGallery(this,bmp);
    }

    // 开始扫码
    private void startQrCode(final CallBackFunction function) {

        requestPms(new PermissonListener() {
            @Override
            public void onPermisson(boolean aboolean) {
                final BaseBean baseBean = new BaseBean();
                if (aboolean){
                    setQrCodeCall(new CallBackFunction() {
                        @Override
                        public void onCallBack(String callData) {
                            DataCacheBean dataCacheBean = new DataCacheBean();
                            dataCacheBean.setResult(callData);
                            baseBean.setContent(dataCacheBean);
                            function.onCallBack(Utils.toJson(baseBean));
                        }
                    });
//                    Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
//                    startActivityForResult(intent, Utils.REQ_QR_CODE);
                } else {
                    baseBean.setFail();
                    function.onCallBack(Utils.toJson(baseBean));
                }
            }
        },Manifest.permission.CAMERA);
    }


    private void requestPms(final PermissonListener listener, String... permissions){
        rxPermissions.request(permissions)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (listener != null){
                            listener.onPermisson(aBoolean);
                        }
                    }
                });
    }

    private void locationMethod(final CallBackFunction function){
        requestPms(new PermissonListener() {
            @Override
            public void onPermisson(boolean aboolean) {
                BaseBean baseBean = new BaseBean();
                if (aboolean){
                    LocationBean locationBean = Utils.getLocation(MainActivity.this);
                    if (locationBean != null){
                        baseBean.setContent(locationBean);
                    } else {
                        baseBean.setFail();
                    }
                } else {
                    baseBean.setFail();
                    baseBean.setMsg("应用相机权限未获取到，无法启动该功能！");
                }
                function.onCallBack(Utils.toJson(baseBean));
            }
        }, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        Log.e("zz","扫描返回requestCode:" + requestCode + "----resultCode:" + resultCode);
        if (requestCode == Utils.REQ_QR_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString(Utils.INTENT_EXTRA_KEY_QR_SCAN);;
            if (qrCodeCall != null){
                qrCodeCall.onCallBack(scanResult);
            }
        }
    }


    private void setQrCodeCall(CallBackFunction qrCodeCall){
        this.qrCodeCall = qrCodeCall;
    }



    private void getsmsPermisson(final CallBackFunction function){
        requestPms(new PermissonListener() {
            @Override
            public void onPermisson(boolean aboolean) {
                if (aboolean){
                    getsmsObserver(function);
                }else {
                    BaseBean baseBean = new BaseBean();
                    baseBean.setFail();
                    baseBean.setMsg("应用权限未获取到，无法读取短信！");
                    function.onCallBack(Utils.toJson(baseBean));
                }
            }
        },Manifest.permission.READ_SMS);
    }

    private void getsmsObserver(CallBackFunction function){
        BaseBean baseBean = new BaseBean();
        SmsInfoBean cacheBean = new SmsInfoBean();
        List<SmsInfoBean.ListBean> smsList = new ArrayList<>();
        Uri inboxUri = Uri.parse("content://sms/inbox");
        Cursor c = getContentResolver().query(inboxUri, null, null, null, "date desc");
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String address = c.getString(c.getColumnIndex("address"));//短信地址
                    String body = c.getString(c.getColumnIndex("body"));//获取短信内容
                    long data = c.getLong(c.getColumnIndex("date"));//短信时间
                    SmsInfoBean.ListBean bean = new SmsInfoBean.ListBean();
                    bean.setPhone(address);
                    bean.setMsg(body);
                    bean.setSent_at(String.valueOf(data));
                    smsList.add(bean);
                }while (smsList.size()<=10 && c.moveToNext());
                cacheBean.setList(smsList);
                baseBean.setContent(cacheBean);
            } else {
                baseBean.setFail();
                baseBean.setMsg("没有查询到数据");
            }
            if (!c.isClosed()) {
                c.close();
                c = null;
            }
        }
        function.onCallBack(Utils.toJson(baseBean));
    }

}
