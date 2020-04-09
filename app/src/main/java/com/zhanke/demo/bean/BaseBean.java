package com.zhanke.demo.bean;

/**
 * Created by zhanke on 2020/3/9.
 * Describe
 */
public class BaseBean {

    /**
     * error : 0
     * msg : success
     * content : 多类型的bean
     */

    private int error = 0;
    private String msg = "success";
    private Object content;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public void setFail(){
        error = -1;
        msg = "fail";
    }
}
