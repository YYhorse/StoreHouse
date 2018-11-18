package com.storehouse.www.Utils.Json.ProductJson;

import java.util.List;

public class ProductJson {
    //{"status_code":200,"info":"ok","product_info":[{"product_category":"家具","goods_category":[{"goods_category_id":10,"goods_category_name":"桌子"}]},{"product_category":"生鲜","goods_category":[{"goods_category_id":11,"goods_category_name":"大鱼"}]}]}
    private int status_code;
    private List<ProductInfo> product_info;

    public int getStatus_code() { return status_code; }
    public List<ProductInfo> getProduct_info() {  return product_info; }

    public class ProductInfo{
        private String product_category;
        private List<GoodsCategory> goods_category;

        public String getProduct_category() { return this.product_category;}
        public List<GoodsCategory> getGoods_category() { return goods_category; }
    }

    public class GoodsCategory{
        private int goods_category_id;
        private String goods_category_name;

        public int getGoods_category_id() { return goods_category_id; }
        public String getGoods_category_name() { return goods_category_name; }
    }
}
