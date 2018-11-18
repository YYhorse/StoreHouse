package com.storehouse.www.Utils.Json.Login;

import java.util.List;

public class LoginRevJson {
//{"status_code":200,"info":"ok","store_token":"16a4aa45949ec0434b4349b4322f8e9","user_info":{"user_id":"2","user_name":"张三","user_phone":"123456789"},"store_info":[{"store_id":1,"store_name":"店铺1"},{"store_id":3,"store_name":"店铺2"},{"store_id":9,"store_name":"店铺5555"}]}
    private int status_code;
    private String store_token;
    private User_Info user_info;
    private List<Store_Info> store_info;

    public int getStatus_code() { return this.status_code;}
    public String getStore_token() { return this.store_token;}
    public User_Info getUser_info() { return this.user_info;}
    public List<Store_Info> getStore_info() { return this.store_info;}

    public class User_Info{
        //"user_info":{"user_id":"2","user_name":"张三","user_phone":"123456789"}
        //,"store_info":[{"store_id":1,"store_name":"店铺1"},{"store_id":3,"store_name":"店铺2"},{"store_id":9,"store_name":"店铺5555"}]
        private String user_id,user_name,user_phone;

        public String getUser_id() { return this.user_id;}
        public String getUser_name() { return this.user_name;}
        public String getUser_phone() { return this.user_phone;}
    }
    public class Store_Info{
        private int store_id;
        private String store_name;

        public int getStore_id() { return this.store_id;}
        public String getStore_name() { return this.store_name;}
    }

}
