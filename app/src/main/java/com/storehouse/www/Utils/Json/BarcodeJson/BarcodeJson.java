package com.storehouse.www.Utils.Json.BarcodeJson;

public class BarcodeJson {
    private String store_token,product_id,product_barcode,product_start_time,warranty,product_end_time;
    private int store_id;

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public int getStore_id() {
        return store_id;
    }

    public String getProduct_barcode() {
        return product_barcode;
    }

    public String getProduct_end_time() {
        return product_end_time;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getProduct_start_time() {
        return product_start_time;
    }

    public String getStore_token() {
        return store_token;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setProduct_barcode(String product_barcode) {
        this.product_barcode = product_barcode;
    }

    public void setProduct_end_time(String product_end_time) {
        this.product_end_time = product_end_time;
    }

    public void setProduct_start_time(String product_start_time) {
        this.product_start_time = product_start_time;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public void setStore_token(String store_token) {
        this.store_token = store_token;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }
}
