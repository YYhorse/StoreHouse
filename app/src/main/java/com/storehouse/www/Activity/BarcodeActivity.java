package com.storehouse.www.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.storehouse.www.Dialog.CalendarPickerDialog;
import com.storehouse.www.R;
import com.storehouse.www.Utils.SwitchPage.SwitchUtil;

public class BarcodeActivity extends Activity{
    private TextView Name_txt,StartTime_txt,EndTime_txt;
    private EditText Warranty_etxt;
    private String Product_Id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        initUi();
        getSwitchIntent();
    }

    private void initUi(){
        Name_txt = (TextView) findViewById(R.id.Barcode_Name_txt);
        StartTime_txt = (TextView) findViewById(R.id.Barcode_starttime_txt);
        EndTime_txt   = (TextView) findViewById(R.id.Barcode_endtime_txt);
        Warranty_etxt = (EditText) findViewById(R.id.Barcode_warranty_etxt);
    }

    private void getSwitchIntent(){
        Intent Extraintent = this.getIntent();
        Name_txt.setText(Extraintent.getStringExtra("ProductName"));
        Product_Id = Extraintent.getStringExtra("ProductId");
    }

    /***********************************************************************************************
     * * 功能说明：选择生产日期
     **********************************************************************************************/
    public void ClickStartDataMethod(View view){
        CalendarPickerDialog.TurnoverChooseCalendar(StartTime_txt,BarcodeActivity.this);
    }

    /***********************************************************************************************
     * * 功能说明：选择截至日期
     **********************************************************************************************/
    public void ClickEndDataMethod(View view){
        CalendarPickerDialog.TurnoverChooseCalendar(EndTime_txt,BarcodeActivity.this);
    }

    /***********************************************************************************************
     * * 功能说明：扫码识别条码
     **********************************************************************************************/
    public void ClickScanBarcodeMethod(View view){

    }

    public void ClickBarcodeBackMethod(View view){
        SwitchUtil.FinishActivity(BarcodeActivity.this);
    }

}
