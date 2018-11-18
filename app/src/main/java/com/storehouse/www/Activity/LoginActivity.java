package com.storehouse.www.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.storehouse.www.Application.PublicUrl;
import com.storehouse.www.R;
import com.storehouse.www.Utils.Datas.PrefUtils;
import com.storehouse.www.Utils.HttpxUtils.HttpxUtils;
import com.storehouse.www.Utils.HttpxUtils.SendCallBack;
import com.storehouse.www.Utils.Json.Account.JsonAccount;
import com.storehouse.www.Utils.Json.Login.LoginRevJson;
import com.storehouse.www.Utils.Json.Login.LoginSendJson;
import com.storehouse.www.Utils.PopMessage.PopMessageUtil;
import com.storehouse.www.Utils.PopMessage.PopWindowMessage;
import com.storehouse.www.Utils.SwitchPage.SwitchUtil;
import com.storehouse.www.Utils.VoiceService.VoiceService;
import com.storehouse.www.Utils.sha256encrypt.SHA256Encrypt;

import org.xutils.common.Callback;

public class LoginActivity extends Activity {
    EditText password_etxt;
    AutoCompleteTextView account_etxt;
    LinearLayout LoginNow_layout, loginPanel;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //-------------标题隐藏------------------//
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        InitUi();
        InitAccountView();
    }

    private void InitUi() {
        password_etxt = (EditText) findViewById(R.id.password_etxt);
        account_etxt = (AutoCompleteTextView) findViewById(R.id.account_etxt);
        LoginNow_layout = (LinearLayout) findViewById(R.id.LoginNow_layout);
        loginPanel = (LinearLayout) findViewById(R.id.loginPanel);

    }

    private void InitAccountView() {
        Gson gson = new Gson();
        String account_str = PrefUtils.getMemoryAccountString("account");
        JsonAccount accountArray = gson.fromJson(account_str, JsonAccount.class);
        String str[] = (String[]) accountArray.jsonaccount.toArray(new String[0]);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, str);
        account_etxt.setAdapter(arrayAdapter);
        account_etxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account_etxt.showDropDown();
            }
        });
    }

    private void SavaAccountMethid(String account) {
        Gson gson = new Gson();
        JsonAccount accountArray = gson.fromJson(PrefUtils.getMemoryAccountString("account"), JsonAccount.class);
        boolean yes = false;                                                        //判断帐号是否存在
        for (int i = 0; i < accountArray.jsonaccount.size(); i++) {
            if (accountArray.jsonaccount.get(i).compareTo(account) == 0) {
                yes = true;
                break;
            }
        }
        if (yes == false) {
            //-----帐号不存在  进行存储
            accountArray.jsonaccount.add(account);
            String AccountJson = gson.toJson(accountArray);
            PrefUtils.setMemoryString("account", AccountJson);
        }
    }

    public void ClickLoginMethod(View view) {
        if (password_etxt.getText().length() != 0) {
            LoginNow_layout.setVisibility(View.VISIBLE);
            loginPanel.setVisibility(View.GONE);
            HttpLoginMethod();
        } else
            PopMessageUtil.showToastShort("输入的信息不能为空!");
    }

    private void HttpLoginMethod() {
        String timestamp = SHA256Encrypt.getTimestamp();
        LoginSendJson loginJson = new LoginSendJson();
        loginJson.setPhone(account_etxt.getText().toString());
        loginJson.setPassword(password_etxt.getText().toString());
        loginJson.setTimestamp(timestamp);
        loginJson.setSecret(SHA256Encrypt.getSecret(account_etxt.getText().toString(), timestamp));

        Gson gson = new Gson();
        String SendJson = gson.toJson(loginJson);
        PopMessageUtil.Log(SendJson);
        HttpxUtils.postHttp(new SendCallBack() {
            @Override
            public void onSuccess(String result) {
                PopMessageUtil.Log("登陆接口返回：" + result);
                Gson gson = new Gson();
                LoginRevJson loginRevJson = gson.fromJson(result, LoginRevJson.class);
                if (loginRevJson.getStatus_code() == 200) {
                    PrefUtils.setMemoryString("UserName", loginRevJson.getUser_info().getUser_name());
                    PrefUtils.setMemoryString("UserId", loginRevJson.getUser_info().getUser_id());
                    PrefUtils.setMemoryString("StoreToken", loginRevJson.getStore_token());

                    String StoreInfo = gson.toJson(loginRevJson.getStore_info());
                    PrefUtils.setMemoryString("ShopInfo", StoreInfo);
                    VoiceService.PlayVoice(0);
                    SavaAccountMethid(account_etxt.getText().toString());
                    SwitchUtil.switchActivity(LoginActivity.this, MainActivity.class).switchToAndFinish();
                } else {
                    VoiceService.PlayVoice(1);
                    PopWindowMessage.PopWinMessage(LoginActivity.this, "接口错误", "登陆请求接口错误：" + result, "error");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                VoiceService.PlayVoice(1);
                PopMessageUtil.Log("服务器异常!" + ex.getMessage());
                PopWindowMessage.PopWinMessage(LoginActivity.this, "服务器错误", "登陆请求异常：" + ex.getMessage(), "error");
                ex.printStackTrace();
            }
            public void onCancelled(Callback.CancelledException cex) { }
            public void onFinished() { }
        }).setUrl(PublicUrl.LoginUrl)
                .addJsonParameter(SendJson)
                .send();
    }
}
