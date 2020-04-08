package com.zhanke.demo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.zhanke.demo.R;


/**
 * <p>
 * 等待提示dialog
 */

public class WaitPorgressDialog extends Dialog {

    public WaitPorgressDialog(Context context) {
        this(context, R.style.WaitDialogStyle);
    }

    public WaitPorgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_wait_dialog);
    }
}
