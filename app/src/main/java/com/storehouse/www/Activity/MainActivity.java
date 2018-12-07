package com.storehouse.www.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.storehouse.www.Adapter.GoodsCategoryAdapter;
import com.storehouse.www.Adapter.ProductAdapter;
import com.storehouse.www.Application.PublicUrl;
import com.storehouse.www.Dialog.CommonSingelEditDialog;
import com.storehouse.www.R;
import com.storehouse.www.Utils.Animation.AnimationUtil;
import com.storehouse.www.Utils.Datas.PrefUtils;
import com.storehouse.www.Utils.HttpxUtils.HttpxUtils;
import com.storehouse.www.Utils.HttpxUtils.SendCallBack;
import com.storehouse.www.Utils.Json.GoodsJson.GoodsBarcodeJson;
import com.storehouse.www.Utils.Json.GoodsJson.GoodsJson;
import com.storehouse.www.Utils.Json.Login.LoginRevJson;
import com.storehouse.www.Utils.Json.ProductJson.ProductJson;
import com.storehouse.www.Utils.PopMessage.PopMessageUtil;
import com.storehouse.www.Utils.PopMessage.PopWindowMessage;
import com.storehouse.www.Utils.SwitchPage.SwitchUtil;
import com.storehouse.www.Utils.VoiceService.VoiceService;

import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private TextView store_name,user_name;
    //----产品种类和控件
    private Spinner product_categroy;
    private ArrayAdapter<String> productcategroy_adapter;
    public List<ProductJson.ProductInfo> productInfos;
    //----商品种类和控件
    public List<ProductJson.GoodsCategory> goodsCategories;
    private GoodsCategoryAdapter goodsCategoryAdapter;
    private ListView goodsCategoryListview;
    public int selectGoodsCategory = 0;
    //----商品和控件
    public List<GoodsJson.Product_Info> productList;
    private ProductAdapter productAdapter;
    private ListView productListview;
    //----盘点商品
    private GoodsBarcodeJson goodsBarcodeJson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUi();
        ShowAndChooseShop();
    }

    private void initUi() {
        store_name = (TextView) findViewById(R.id.store_name);
        user_name  = (TextView) findViewById(R.id.user_name);
        product_categroy = (Spinner) findViewById(R.id.ProductCategroy_spinner);
        user_name.setText(PrefUtils.getMemoryString("UserName"));
        //----商品种类
        goodsCategoryListview = (ListView) findViewById(R.id.GoodsCategory_Listview);
        goodsCategoryListview.setLayoutAnimation(AnimationUtil.getAnimationController());               //添加切换动画
        goodsCategories = new ArrayList<ProductJson.GoodsCategory>();
        goodsCategoryAdapter = new GoodsCategoryAdapter(this, goodsCategories);
        goodsCategoryListview.setAdapter(goodsCategoryAdapter);
        //----商品信息
        productListview = (ListView) findViewById(R.id.Goods_Listview);
        productListview.setLayoutAnimation(AnimationUtil.getAnimationController());
        productList = new ArrayList<GoodsJson.Product_Info>();
        productAdapter = new ProductAdapter(this, productList);
        productListview.setAdapter(productAdapter);
        //----产品种类响应
        product_categroy.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UpdataGoodsCategoryShow(position);
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void ShowAndChooseShop(){
        String shopInfo = PrefUtils.getMemoryString("ShopInfo");
        Gson gson = new Gson();
        final List<LoginRevJson.Store_Info> ShopInfo= gson.fromJson(shopInfo,new TypeToken<List<LoginRevJson.Store_Info>>(){}.getType());
        String[] shopName = new String[ShopInfo.size()];
        for(int i=0;i<ShopInfo.size();i++)
         shopName[i] = ShopInfo.get(i).getStore_name();
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("请选择店铺")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setCancelable(false)
                .setSingleChoiceItems(shopName, 0,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                PrefUtils.setMemoryString("ShopName", ShopInfo.get(which).getStore_name());
                                PrefUtils.setMemoryString("ShopId", String.valueOf(ShopInfo.get(which).getStore_id()));
                                store_name.setText(PrefUtils.getMemoryString("ShopName"));
                                GetGoodsCategoryList();
                                dialog.dismiss();
                            }
                        }
                );
        builder.show();
    }

    /***********************************************************************************************
     * * 功能说明：更新产品种类
     **********************************************************************************************/
    private void GetGoodsCategoryList(){
        PopMessageUtil.Loading(MainActivity.this, "种类更新");
        HttpxUtils.getHttp(new SendCallBack() {
            @Override
            public void onSuccess(String result) {
                PopMessageUtil.Log("店铺信息接口返回：" + result);
                Gson gson = new Gson();
                ProductJson productJson = gson.fromJson(result,ProductJson.class);
                if(productJson.getStatus_code() == 200){
                    //获取店铺产品种类列表
                    productInfos = productJson.getProduct_info();
                    List<String> data_list = new ArrayList<String>();
                    for(int i=0;i<productInfos.size();i++)
                        data_list.add(productInfos.get(i).getProduct_category());
                    //适配器
                    productcategroy_adapter= new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, data_list);
                    //设置样式
                    productcategroy_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //加载适配器
                    product_categroy.setAdapter(productcategroy_adapter);
                }
                else{
                    PopMessageUtil.CloseLoading();
                    PopMessageUtil.showToastLong("种类接口错误"+productJson.getStatus_code());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                PopMessageUtil.CloseLoading();
                VoiceService.PlayVoice(1);
                PopMessageUtil.Log("服务器异常!" + ex.getMessage());
                PopWindowMessage.PopWinMessage(MainActivity.this, "服务器错误", "登陆请求异常：" + ex.getMessage(), "error");
                ex.printStackTrace();
            }
            public void onCancelled(Callback.CancelledException cex) { }
            public void onFinished() { }
        }).setUrl(PublicUrl.GetProducts)
                .addQueryStringParameter("store_token",PrefUtils.getMemoryString("StoreToken"))
                .send();
    }

    /***********************************************************************************************
     * * 功能说明：更新产品种类
     **********************************************************************************************/
    private void UpdataGoodsCategoryShow(int postion){
        List<ProductJson.GoodsCategory> goodsCategories = productInfos.get(postion).getGoods_category();
        goodsCategoryAdapter.UpdataGoodsCategoryList(goodsCategories);
        selectGoodsCategory = 0;
        UpdataGoodsShow(goodsCategories.get(selectGoodsCategory).getGoods_category_id());
    }

    /***********************************************************************************************
     * * 功能说明：更新商品信息
     **********************************************************************************************/
    public void UpdataGoodsShow(int goodsCategoryId){
        PopMessageUtil.Loading(MainActivity.this,"获取商品列表");
        HttpxUtils.getHttp(new SendCallBack() {
            @Override
            public void onSuccess(String result) {
                PopMessageUtil.CloseLoading();
                PopMessageUtil.Log("商品信息接口返回：" + result);
                Gson gson = new Gson();
                GoodsJson goodsJson = gson.fromJson(result,GoodsJson.class);
                if(goodsJson.getStatus_code() == 200){
                    //获取商品列表
                    productList = goodsJson.getProduct_info();
                    productAdapter.UpdataGoodsList(productList);
                }
                else
                    PopMessageUtil.showToastLong("获取商品列表接口错误"+goodsJson.getStatus_code());
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                PopMessageUtil.CloseLoading();
                VoiceService.PlayVoice(1);
                PopMessageUtil.Log("服务器异常!" + ex.getMessage());
                PopWindowMessage.PopWinMessage(MainActivity.this, "服务器错误", "登陆请求异常：" + ex.getMessage(), "error");
                ex.printStackTrace();
            }
            public void onCancelled(Callback.CancelledException cex) { }
            public void onFinished() { }
        }).setUrl(PublicUrl.GetGoods)
                .addQueryStringParameter("store_token",PrefUtils.getMemoryString("StoreToken"))
                .addQueryStringParameter("store_id",PrefUtils.getMemoryString("ShopId"))
                .addQueryStringParameter("goods_category_id",String.valueOf(goodsCategoryId))
                .send();
    }

    /***********************************************************************************************
     * * 功能说明：库存盘点
     **********************************************************************************************/
    public void ClickCheckMethod(View view){
        SwitchUtil.switchActivity(MainActivity.this, ScanPayDialog.class).addString("key", "check").switchToForResult(2);
    }

    /***********************************************************************************************
     * * 功能说明：库存盘点 获取条码对应商品信息
     **********************************************************************************************/
    private void GetGoodsInfoForBarcode(String barcode){
        PopMessageUtil.Loading(MainActivity.this, "商品条码");
        PopMessageUtil.Log("条码：" + barcode);
        HttpxUtils.getHttp(new SendCallBack() {
            @Override
            public void onSuccess(String result) {
                PopMessageUtil.CloseLoading();
                PopMessageUtil.Log("商品条码信息接口返回：" + result);
                Gson gson = new Gson();
                goodsBarcodeJson = gson.fromJson(result, GoodsBarcodeJson.class);
                if (goodsBarcodeJson.getStatus_code() == 200) {
                    //获取商品条码信息成功
                    PopMessageUtil.showToastShort(goodsBarcodeJson.getProduct_name());
                    SwitchUtil.switchActivity(MainActivity.this, CommonSingelEditDialog.class)
                            .addString("Type","CheckBarcode")
                            .switchToForResult(3);
                } else
                    PopMessageUtil.showToastLong("获取商品条码信息错误" + goodsBarcodeJson.getStatus_code());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                PopMessageUtil.CloseLoading();
                VoiceService.PlayVoice(1);
                PopMessageUtil.Log("服务器异常!" + ex.getMessage());
                PopWindowMessage.PopWinMessage(MainActivity.this, "服务器错误", "商品条码请求异常：" + ex.getMessage(), "error");
                ex.printStackTrace();
            }
            public void onCancelled(Callback.CancelledException cex) { }
            public void onFinished() { }
        }).setUrl(PublicUrl.GetGoodsForBarcode)
                .addQueryStringParameter("store_token", PrefUtils.getMemoryString("StoreToken"))
                .addQueryStringParameter("product_barcode", barcode)
                .send();
    }

    /***********************************************************************************************
     * * 功能说明：库存盘点 上传盘点数目
     **********************************************************************************************/
    private void UploadCheckNumberMethod(String checkNumber){
        PopMessageUtil.Loading(MainActivity.this, "上传盘点数中");
        HttpxUtils.postHttp(new SendCallBack() {
            @Override
            public void onSuccess(String result) {
                PopMessageUtil.CloseLoading();
                PopMessageUtil.Log("上传盘点数接口返回：" + result);
                if (result.compareTo("{\"status_code\":200,\"info\":\"ok\"}") == 0) {
                    VoiceService.PlayVoice(0);
                    PopMessageUtil.showToastShort("上传成功!");
                } else if (result.compareTo("{\"status_code\":300,\"info\":\"error\"}") == 0) {
                    VoiceService.PlayVoice(1);
                    PopMessageUtil.showToastShort("请勿重复添加!");
                } else {
                    VoiceService.PlayVoice(1);
                    PopMessageUtil.showToastLong("上传失败!" + result);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                PopMessageUtil.CloseLoading();
                VoiceService.PlayVoice(1);
                PopMessageUtil.Log("服务器异常!" + ex.getMessage());
                PopWindowMessage.PopWinMessage(MainActivity.this, "服务器错误", "上传盘点数接口异常：" + ex.getMessage(), "error");
                ex.printStackTrace();
            }

            public void onCancelled(Callback.CancelledException cex) {}
            public void onFinished() {}
        }).setUrl(PublicUrl.UploadCheck)
                .addBodyParameter("store_token", PrefUtils.getMemoryString("StoreToken"))
                .addBodyParameter("store_id",PrefUtils.getMemoryString("ShopId"))
                .addBodyParameter("product_id",goodsBarcodeJson.getProduct_id())
                .addBodyParameter("count",checkNumber)
                .send();
    }

    /***********************************************************************************************
     * * 功能说明：处理返回数据
     **********************************************************************************************/
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 1) {
            //商品管理返回
            if (resultCode == RESULT_OK) {
                UpdataGoodsCategoryShow(0);
            }
        }
        else if(requestCode == 2){
            //盘点二维码返回
            if (resultCode == RESULT_OK)
                GetGoodsInfoForBarcode(intent.getStringExtra("BARCODE"));
        }
        else if(requestCode == 3){
            //盘点数返回
            if(resultCode == RESULT_OK)
                UploadCheckNumberMethod(intent.getStringExtra("Context"));
        }
    }
}

