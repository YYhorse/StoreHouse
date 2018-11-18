package com.storehouse.www.Application;

import android.app.Application;

import com.storehouse.www.Utils.VoiceService.VoiceService;

import org.xutils.x;

public class BaseApplication extends Application {
    public static BaseApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //===========语音加载==============//
        VoiceService.SystemAudioSet();                                                              //系统声音最大声
        //===============网络请求==================//
        x.Ext.init(this);
    }
    public static BaseApplication getInstance(){
        return instance;
    }
}
