package com.storehouse.www.Application;

public class PublicUrl {
    public static String HomeUrl = "http://47.93.33.134/estore";

    public static String LoginUrl = HomeUrl + "/api/app/login";
    public static String GetProducts = HomeUrl + "/api/app/products";
    public static String GetGoods = HomeUrl + "/api/app/goods";
    public static String GetGoodsForBarcode = HomeUrl + "/api/app/barcodeProduct?";
    public static String UploadBarcode = HomeUrl + "/api/app/products/barcode";
    public static String UploadCheck = HomeUrl + "/api/app/products/check?";
}
