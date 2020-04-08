package com.zhanke.webview.util;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhanke on 2020/3/17.
 * Describe
 */
public class SmsObserver extends ContentObserver {

    private Handler mHandler;
    private Context mContext;

    public SmsObserver(Context context,Handler handler) {
        super(handler);
        this.mContext = context;
        this.mHandler = handler;
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        String code;
        if (uri.toString().equals("content://sms/raw"))  ////onChange会执行二次,第二次短信才会入库
        {
            return ;
        }
        Uri inboxUri = Uri.parse("content://sms/inbox");
        Cursor c = mContext.getContentResolver().query(inboxUri, null, null, null, "date desc");
        if (c != null) {
            if (c.moveToFirst()) {
                String address = c.getString(c.getColumnIndex("address"));
                String body = c.getString(c.getColumnIndex("body"));//获取短信内容
                Pattern pattern = Pattern.compile("(\\d{6})");//正则表达式   连续6位数字
                Matcher matcher = pattern.matcher(body);
                if (matcher.find()) {
                    code = matcher.group(0);
                    //设置到粘贴板上面去
//                    ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
//                    cmb.setText(code);
                    mHandler.obtainMessage(Utils.MSG_RECEIVED_CODE, code).sendToTarget();
                }
            }
            c.close();
        }
    }
}
