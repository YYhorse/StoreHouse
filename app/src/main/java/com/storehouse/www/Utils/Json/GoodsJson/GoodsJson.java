package com.storehouse.www.Utils.Json.GoodsJson;

import java.util.List;

public class GoodsJson {
    private int status_code;
    private List<Product_Info> product_info;

    public int getStatus_code() {
        return status_code;
    }

    public List<Product_Info> getProduct_info() { return product_info; }

    public class Product_Info{
        private int product_id,product_quantity;
        private String product_name;
        private float product_price;

        public int getProduct_id() {
            return product_id;
        }
        public String getProduct_name() {
            return product_name;
        }
        public float getProduct_price() {
            return product_price;
        }
        public int getProduct_quantity() {
            return product_quantity;
        }
    }
}
