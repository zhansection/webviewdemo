package com.zhanke.demo.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.gson.Gson;
import com.zhanke.demo.bean.BaseBean;
import com.zhanke.demo.bean.android.ContactListBean;
import com.zhanke.demo.bean.android.LocationBean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;




/**
 * Created by zhanke on 2020/3/9.
 * Describe
 */
public class Utils {

    // request参数
    public static final int REQ_QR_CODE = 11002; // // 打开扫描界面请求码
    public static final int REQ_PERM_CAMERA = 11003; // 打开摄像头
    public static final int REQ_PERM_EXTERNAL_STORAGE = 11004; // 读写文件
    public static final int MSG_RECEIVED_CODE = 11005;//短信验证码发送信息

    public static final String INTENT_EXTRA_KEY_QR_SCAN = "qr_scan_result";
    // 号码
    public final static String NUM = ContactsContract.CommonDataKinds.Phone.NUMBER;
    // 联系人姓名
    public final static String NAME = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;



    public static String toJson(BaseBean baseBean){
        Gson gson = new Gson();
        return gson.toJson(baseBean);
    }

    /**
     * apk安装
     */
    public static void apkInstall(Context context, File file) {
        if (context == null || !file.exists()) {
            //ToastUtils.showToast("没有找到安装包");
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
           // LogUtils.e("7.0以上，正在安装apk...");
            Uri apkUri = FileProvider.getUriForFile(context, context.getPackageName()+".provider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
           // LogUtils.e("7.0以下，正在安装apk...");
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    public static boolean isGameAvailable(Context context, String packName) {
        List<PackageInfo> pinfo = getIponePack(context);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals(packName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<PackageInfo> getIponePack(Context context) {
        return context.getPackageManager().getInstalledPackages(0);
    }

    /**
     * 实现文本复制功能
     *
     * @param content 复制的文本
     */
    public static void setClipboardText(Context context,String content) {
        if (!TextUtils.isEmpty(content)) {
            // 得到剪贴板管理器
            ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setPrimaryClip(ClipData.newPlainText(null,content.trim()));
            if (cmb.hasPrimaryClip()){
                cmb.getPrimaryClip().getItemAt(0).getText();
            }
        }
    }

    /**
     * 获取复制版内容
     */
    public static String getClipboardText(Context context) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = cmb.getPrimaryClip();
        return clipData == null ? "" :clipData.toString();
    }

    /**
     * 获取versioncode(name)
     */
    public static String getVersionName(Context context) {
        String versionName = "1.0.0";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return versionName;
    }

    /**
     * 获取手机型号
     */
    public static String getPhoneModel(){
        return Build.MODEL;
    }

    /**
     * android的系统版本号
     * @return
     */
    public static String getOsVersion(){
        return "Android:" + Build.VERSION.RELEASE;
    }


    public static int[] getDevice(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
      //  screenHeight = displayMetrics.heightPixels;
        int[] device = {displayMetrics.widthPixels,displayMetrics.heightPixels};
        return device;
    }

    /**
     * 获取手机IMEI
     */
    @SuppressLint("MissingPermission")
    public static String getPhoneIMEI(Context context){
        if (lacksPermission(context, Manifest.permission.READ_PHONE_STATE)){
            return "";
        }else {
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String deviceId = manager.getDeviceId();
            if (deviceId == null){
                deviceId= Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
            return deviceId;
        }

    }

    /**
     * 获取UUID
     */
    @SuppressLint("MissingPermission")
    public static String getUUID(Context context){
        if (lacksPermission(context, Manifest.permission.READ_PHONE_STATE)){
            return "";
        }else {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String tmDevice, tmSerial, androidId;
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = "" + Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
            return deviceUuid.toString();
        }
    }

    /**
     * 权限检测
     * @param mContexts
     * @param permission
     * @return
     */
    public static boolean lacksPermission(Context mContexts, String permission) {
        return ContextCompat.checkSelfPermission(mContexts, permission) ==
                PackageManager.PERMISSION_DENIED;
    }


    public static LocationBean getLocation(Context context){

        if (lacksPermission(context,Manifest.permission.ACCESS_FINE_LOCATION) && lacksPermission(context,Manifest.permission.ACCESS_COARSE_LOCATION)){
            return null;
        }

        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAltitudeRequired(false);//不要求海拔
        criteria.setBearingRequired(false);//不要求方位
        criteria.setCostAllowed(true);// 允许有花费

        //设置位置服务免费
        criteria.setAccuracy(Criteria.ACCURACY_COARSE); //设置水平位置精度
        //getBestProvider 只有允许访问调用活动的位置供应商将被返回

        List<String> providers = lm.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            @SuppressLint("MissingPermission")
            Location l = lm.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        if (bestLocation == null){
            return null;
        }
        LocationBean locationBean = new LocationBean();
        locationBean.setLatitude(String.valueOf(bestLocation.getLatitude()));
        locationBean.setLongitude(String.valueOf(bestLocation.getLongitude()));
        //获取维度信息
        double latitude = bestLocation.getLatitude();
        //获取经度信息
        double longitude = bestLocation.getLongitude();
        Log.e("zz", "定位方式： ----"  + "  维度：" + latitude + "  经度：" + longitude);
        return locationBean;
    }



    /**
     * 保存到内存卡
     *
     * @param context
     * @param bmp
     */
    public static void saveToSystemGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "xgame999");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(file.getAbsolutePath())));
    }


    //获取所有联系人
    public static List<ContactListBean.ListBean> getPhoneContact(Context context){
        List<ContactListBean.ListBean> phoneDtos = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        //联系人提供者的uri
        Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = cr.query(phoneUri,new String[]{NUM,NAME},null,null,null);
        if (cursor != null){
            while (cursor.moveToNext()){
                //ContactListBean.ListBean phoneDto = new PhoneDto(cursor.getString(cursor.getColumnIndex(NAME)),cursor.getString(cursor.getColumnIndex(NUM)));
                ContactListBean.ListBean phoneDto = new ContactListBean.ListBean();
                phoneDto.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                List<String> phone = new ArrayList<>();
                phone.add(cursor.getString(cursor.getColumnIndex(NUM)));
                phoneDto.setPhones(phone);
                phoneDtos.add(phoneDto);
            }
            cursor.close();
        }
        return phoneDtos;
    }

}
