package com.storehouse.www.Utils.Json.BarcodeJson;

/**
 * Created by yy on 2018/11/28.
 */
public class UploadBarcode {
    private String product_id,barcode,product_start_time,product_end_time,user_name,warranty;
    private int count,store_id;

    public void setProduct_id(String product_id) {this.product_id = product_id;}
    public void setBarcode(String barcode) {this.barcode = barcode;}
    public void setCount(int count) {this.count = count;}
    public void setProduct_start_time(String product_start_time) {this.product_start_time = product_start_time;}
    public void setProduct_end_time(String product_end_time) {this.product_end_time = product_end_time;}
    public void setStore_id(int store_id) {this.store_id = store_id;}
    public void setUser_name(String user_name) {this.user_name = user_name;}
    public void setWarranty(String warranty) {this.warranty = warranty;}
}
/*
{
  "barcode": "string",
  "count": 0,
  "product_end_time": "string",
  "product_id": "string",
  "product_start_time": "string",
  "store_id": 0,
  "user_name": "string",
  "warranty": "string"
}
 */
