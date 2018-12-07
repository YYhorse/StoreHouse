package com.storehouse.www.Utils.Json.CheckJson;

/**
 * Created by yy on 2018/12/7.
 */
public class CheckJson {
    private String store_token,product_id ;
    private int store_id,count;

    public String getProduct_id() {return product_id;}
    public int getCount() {return count;}
    public int getStore_id() {return store_id;}
    public String getStore_token() {return store_token;}
    public void setCount(int count) {this.count = count;}
    public void setProduct_id(String product_id) {this.product_id = product_id;}
    public void setStore_id(int store_id) {this.store_id = store_id;}
    public void setStore_token(String store_token) {this.store_token = store_token;}
}
