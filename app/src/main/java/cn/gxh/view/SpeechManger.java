package cn.gxh.view;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import java.util.Locale;
import cn.gxh.view.base.Global;

public class SpeechManger implements TextToSpeech.OnInitListener {

    private static SpeechManger mInstance=null;
    private static TextToSpeech  tts;

    public static SpeechManger getInstance(){
        if(mInstance==null){
            synchronized (SpeechManger.class) {
                if(mInstance==null){
                    mInstance=new SpeechManger();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context){
        tts=new TextToSpeech(context,this);
    }

    public void play(String content){
        if(tts!=null){
            //播放语音
            // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
            tts.setPitch(0.5f);
            // 设置语速
            tts.setSpeechRate(1.0f);
            tts.speak(content, TextToSpeech.QUEUE_ADD, null,null);
        }else {
            Global.showToast("语音对象未实例化");
        }
    }

    public void release(){
        if(tts!=null){
            tts.stop();
            tts.shutdown();
        }
    }


    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.ENGLISH);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Global.showToast("数据丢失或不支持");
            }
        }
    }
}
