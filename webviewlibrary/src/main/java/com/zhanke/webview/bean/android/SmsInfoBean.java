package com.zhanke.webview.bean.android;

import java.util.List;

/**
 * Created by zhanke on 2020/3/18.
 * Describe
 */
public class SmsInfoBean {


    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * phone : 13568896210
         * msg : xxx
         * sent_at : 1584511110
         */

        private String phone;
        private String msg;
        private String sent_at;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getSent_at() {
            return sent_at;
        }

        public void setSent_at(String sent_at) {
            this.sent_at = sent_at;
        }
    }
}
