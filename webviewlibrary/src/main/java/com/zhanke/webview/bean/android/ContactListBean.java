package com.zhanke.webview.bean.android;

import java.util.List;

/**
 * Created by zhanke on 2020/3/16.
 * Describe
 */
public class ContactListBean {

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * name : 姓名
         * phones : ["10010","100086"]
         */

        private String name;
        private List<String> phones;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getPhones() {
            return phones;
        }

        public void setPhones(List<String> phones) {
            this.phones = phones;
        }
    }
}
