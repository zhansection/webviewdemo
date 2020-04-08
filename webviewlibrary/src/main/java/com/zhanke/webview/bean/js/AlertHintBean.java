package com.zhanke.webview.bean.js;

/**
 * Created by zhanke on 2020/3/13.
 * Describe
 */
public class AlertHintBean {

    /**
     * title : 标题
     * message : 原生弹框内容
     * confirmButton : 确定
     * cancelButton : 取消
     */

    private String title;
    private String message;
    private String confirmButton;
    private String cancelButton;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getConfirmButton() {
        return confirmButton;
    }

    public void setConfirmButton(String confirmButton) {
        this.confirmButton = confirmButton;
    }

    public String getCancelButton() {
        return cancelButton;
    }

    public void setCancelButton(String cancelButton) {
        this.cancelButton = cancelButton;
    }
}
