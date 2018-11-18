package com.storehouse.www.Utils.VoiceService;

import android.app.Service;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.storehouse.www.Application.BaseApplication;
import com.storehouse.www.R;

import java.util.HashMap;

/**
 * 静态全局控制类
 * Created by yy on 2017/8/24.
 * 人工语音服务  +  音效控制
 * 任何地方使用方法
 */
public class VoiceService {
    //******************音效***************************//
    public static AudioManager am;
    public static SoundPool soundPool;
    public static HashMap musicId;                                                                         //定义一个HashMap用于存放音频流的ID

    private static Handler UiHandler = new Handler(Looper.getMainLooper());

    /***********************************************************************************************
     * * 函数名称: void SystemAudioSet()
     * * 功能说明：音效加载
     * * 最大音量15
     **********************************************************************************************/
    public static void SystemAudioSet() {
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 5);
        musicId = new HashMap();
        musicId.put(0, soundPool.load(BaseApplication.getInstance(), R.raw.success, 1));
        musicId.put(1, soundPool.load(BaseApplication.getInstance(), R.raw.fail, 1));
        musicId.put(2, soundPool.load(BaseApplication.getInstance(), R.raw.key, 1));
        musicId.put(3, soundPool.load(BaseApplication.getInstance(), R.raw.clean, 1));
    }

    /***********************************************************************************************
     * * 函数名称:  void PlayVoice(final int id)
     * * 功能说明： 播放特效声音
     **********************************************************************************************/
    public static void PlayVoice(final int id) {
        UiHandler.post(new Runnable() {
            @Override
            public void run() {
                soundPool.play((Integer) musicId.get(id), 1, 1, 0, 0, 1);
            }
        });
    }
}
