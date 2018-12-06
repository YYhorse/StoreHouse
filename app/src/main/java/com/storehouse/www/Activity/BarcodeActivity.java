package com.storehouse.www.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.storehouse.www.Application.PublicUrl;
import com.storehouse.www.Dialog.CalendarPickerDialog;
import com.storehouse.www.R;
import com.storehouse.www.Utils.Datas.PrefUtils;
import com.storehouse.www.Utils.HttpxUtils.HttpxUtils;
import com.storehouse.www.Utils.HttpxUtils.SendCallBack;
import com.storehouse.www.Utils.Json.BarcodeJson.UploadBarcode;
import com.storehouse.www.Utils.PopMessage.PopMessageUtil;
import com.storehouse.www.Utils.PopMessage.PopWindowMessage;
import com.storehouse.www.Utils.SwitchPage.SwitchUtil;
import com.storehouse.www.Utils.VoiceService.VoiceService;

import org.xutils.common.Callback;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BarcodeActivity extends Activity {
    private TextView Name_txt, StartTime_txt, EndTime_txt;
    private EditText Warranty_etxt, Count_etxt;
    private String Product_Id, Barcode;
    private boolean isUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        initUi();
        getSwitchIntent();
    }

    private void initUi() {
        Name_txt = (TextView) findViewById(R.id.Barcode_Name_txt);
        StartTime_txt = (TextView) findViewById(R.id.Barcode_starttime_txt);
        EndTime_txt = (TextView) findViewById(R.id.Barcode_endtime_txt);
        Warranty_etxt = (EditText) findViewById(R.id.Barcode_warranty_etxt);
        Count_etxt = (EditText) findViewById(R.id.Barcode_count_etxt);
    }

    private void getSwitchIntent() {
        Intent Extraintent = this.getIntent();
        Name_txt.setText(Extraintent.getStringExtra("ProductName"));
        Product_Id = Extraintent.getStringExtra("ProductId");
    }

    /***********************************************************************************************
     * * 功能说明：选择生产日期
     **********************************************************************************************/
    public void ClickStartDataMethod(View view) {
        CalendarPickerDialog.TurnoverChooseCalendar(StartTime_txt, BarcodeActivity.this);
    }

    /***********************************************************************************************
     * * 功能说明：选择截至日期
     **********************************************************************************************/
    public void ClickEndDataMethod(View view) {
        CalendarPickerDialog.TurnoverChooseCalendar(EndTime_txt, BarcodeActivity.this);
    }

    /***********************************************************************************************
     * * 功能说明：扫码识别条码
     **********************************************************************************************/
    public void ClickScanBarcodeMethod(View view) {
        if(Count_etxt.getText().length()!=0) {
            SwitchUtil.switchActivity(BarcodeActivity.this, ScanPayDialog.class).addString("key","barcode").switchToForResult(1);
        }
        else
            PopMessageUtil.showToastShort("商品数量不能为空!");
    }

    public void ClickBarcodeBackMethod(View view) {
        if(isUpdate)
            SwitchUtil.switchActivity(BarcodeActivity.this,MainActivity.class).switchToFinishWithValue(RESULT_OK);
        else
            SwitchUtil.FinishActivity(BarcodeActivity.this);
    }

    //------------处理返回值------------//
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 1) {
            //二维码支付识别
            if (resultCode == RESULT_OK) {
                Barcode = intent.getStringExtra("BARCODE");
                PopMessageUtil.Log("识别到条码：" + Barcode);
                ShowDialogMessage("提醒", "是否添加条码:" + Barcode, 0);
            }
        }
    }

    private void UploadBarcode() {
        PopMessageUtil.Loading(BarcodeActivity.this, "上传条码中");
        UploadBarcode uploadBarcode = new UploadBarcode();
        uploadBarcode.setBarcode(Barcode);
        uploadBarcode.setCount(Integer.parseInt(Count_etxt.getText().toString()));
        uploadBarcode.setProduct_end_time(EndTime_txt.getText().toString());
        uploadBarcode.setProduct_id(Product_Id);
        uploadBarcode.setProduct_start_time(StartTime_txt.getText().toString());
        uploadBarcode.setStore_id(Integer.parseInt(PrefUtils.getMemoryString("ShopId")));
        uploadBarcode.setUser_name(PrefUtils.getMemoryString("UserName"));
        uploadBarcode.setWarranty(Warranty_etxt.getText().toString());

        Gson gson = new Gson();
        String SendJson = gson.toJson(uploadBarcode);
        PopMessageUtil.Log(SendJson);
        HttpxUtils.postHttp(new SendCallBack() {
            @Override
            public void onSuccess(String result) {
                PopMessageUtil.CloseLoading();
                PopMessageUtil.Log("上传条码接口返回：" + result);
                if (result.compareTo("{\"status_code\":200,\"info\":\"ok\"}") == 0) {
                    isUpdate = true;
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
                PopWindowMessage.PopWinMessage(BarcodeActivity.this, "服务器错误", "上传条码请求异常：" + ex.getMessage(), "error");
                ex.printStackTrace();
            }

            public void onCancelled(Callback.CancelledException cex) {}
            public void onFinished() {}
        }).setUrl(PublicUrl.UploadBarcode)
                .addBodyParameter("store_token", PrefUtils.getMemoryString("StoreToken"))
                .addJsonParameter(SendJson)
                .send();
    }

    private SweetAlertDialog sweetAlertDialog;

    private void ShowDialogMessage(String title, String context, int info_type) {
        sweetAlertDialog = new SweetAlertDialog(BarcodeActivity.this, info_type);
        sweetAlertDialog
                .setTitleText(title)
                .setContentText(context)
                .setConfirmText("确认")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sw) {
                        sweetAlertDialog.cancel();
                        sweetAlertDialog = null;
                        UploadBarcode();
                    }
                })
                .changeAlertType(info_type);
        sweetAlertDialog.setCanceledOnTouchOutside(true);
        sweetAlertDialog.show();
    }
}
