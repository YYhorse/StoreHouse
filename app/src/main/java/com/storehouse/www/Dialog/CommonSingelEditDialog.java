package com.storehouse.www.Dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.storehouse.www.Activity.MainActivity;
import com.storehouse.www.R;
import com.storehouse.www.Utils.PopMessage.PopMessageUtil;
import com.storehouse.www.Utils.SwitchPage.SwitchUtil;

/**
 * Created by yy on 2017/10/10.
 */
public class CommonSingelEditDialog extends Activity {
    String type, context;
    EditText input_etxt;
    RelativeLayout back_layout, finish_layout;
    TextView title_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_commonsingeledit);
        initUi();
        DealIntentValue();
    }

    private void initUi() {
        input_etxt = (EditText) findViewById(R.id.CommonSingelEdit_input_etxt);
        back_layout = (RelativeLayout) findViewById(R.id.CommonSingelEdit_Back_layout);
        finish_layout = (RelativeLayout) findViewById(R.id.CommonSingelEdit_Finish_layout);
        title_txt = (TextView) findViewById(R.id.CommonSingelEdit_Title_txt);

        back_layout.setOnClickListener(new ClickBackMethod());
        finish_layout.setOnClickListener(new ClickFinishMethod());
    }

    private void DealIntentValue() {
        Intent intent = this.getIntent();
        type = intent.getStringExtra("Type");
        context = intent.getStringExtra("Context");
        input_etxt.setInputType(InputType.TYPE_CLASS_TEXT);
        if (type.compareTo("CheckBarcode") == 0)
            title_txt.setText("请输入盘点数量");
        input_etxt.setText(context);
    }

    class ClickBackMethod implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            SwitchUtil.switchActivity(CommonSingelEditDialog.this,MainActivity.class).switchToFinishWithValue(RESULT_CANCELED);
        }
    }

    class ClickFinishMethod implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(input_etxt.getText().length()!=0){
                SwitchUtil.switchActivity(CommonSingelEditDialog.this, MainActivity.class)
                        .addString("Context",input_etxt.getText().toString())
                        .switchToFinishWithValue(RESULT_OK);
            }
            else
                PopMessageUtil.showToastShort("请输入盘点数");

        }
    }
}
