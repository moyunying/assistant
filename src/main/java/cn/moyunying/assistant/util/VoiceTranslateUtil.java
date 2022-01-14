package cn.moyunying.assistant.util;

import java.util.ArrayList;

import cn.moyunying.assistant.util.DebugLog;
import com.iflytek.cloud.speech.*;

public class VoiceTranslateUtil {

    private static final String APPID = "f0b9a7d9";

    private static final String path = "D:\\audio\\";

    public static String Synthesize(String text, int cc) {
        SpeechUtility.createUtility("appid=" + APPID);

        SpeechSynthesizer speechSynthesizer = SpeechSynthesizer
                .createSynthesizer();

        if(cc == 0){
            // 设置发音人
            speechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, "x2_zhongcun");
            //设置语速
            speechSynthesizer.setParameter(SpeechConstant.SPEED,"5");
            //设置音调
            speechSynthesizer.setParameter(SpeechConstant.PITCH,"5");
            //设置音量
            speechSynthesizer.setParameter(SpeechConstant.VOLUME,"100");
        }

        else if(cc == 1){
            // 设置发音人
            speechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, "aisjiuxu");
            //设置语速
            speechSynthesizer.setParameter(SpeechConstant.SPEED,"0");
            //设置音调
            speechSynthesizer.setParameter(SpeechConstant.PITCH,"0");
            //设置音量
            speechSynthesizer.setParameter(SpeechConstant.VOLUME,"100");
        }

        String sourceFilename =  System.currentTimeMillis()+".pcm";
        System.out.println(sourceFilename);
        // 设置合成音频保存位置（可自定义保存位置），默认不保存
        speechSynthesizer.synthesizeToUri(text, path + sourceFilename,
                synthesizeToUriListener);

        return sourceFilename;

    }

    /**
     *
     * 合成监听器
     */
    static SynthesizeToUriListener synthesizeToUriListener = new SynthesizeToUriListener() {

        public void onBufferProgress(int progress) {
            DebugLog.Log("*************合成进度*************" + progress);
        }

        public void onSynthesizeCompleted(String uri, SpeechError error) {
            if (error == null) {
                DebugLog.Log("*************合成成功*************");
                DebugLog.Log("合成音频生成路径：" + uri);
            } else
                DebugLog.Log("*************" + error.getErrorCode()
                        + "*************");
        }


        @Override
        public void onEvent(int eventType, int arg1, int arg2, int arg3, Object obj1, Object obj2) {
            if (SpeechEvent.EVENT_TTS_BUFFER == eventType) {
                DebugLog.Log("onEvent: type=" + eventType
                        + ", arg1=" + arg1
                        + ", arg2=" + arg2
                        + ", arg3=" + arg3
                        + ", obj2=" + (String) obj2);
                ArrayList<?> bufs = null;
                if (obj1 instanceof ArrayList<?>) {
                    bufs = (ArrayList<?>) obj1;
                } else {
                    DebugLog.Log("onEvent error obj1 is not ArrayList !");
                }//end of if-else instance of ArrayList

                if (null != bufs) {
                    for (final Object obj : bufs) {
                        if (obj instanceof byte[]) {
                            final byte[] buf = (byte[]) obj;
                            DebugLog.Log("onEvent buf length: " + buf.length);
                        } else {
                            DebugLog.Log("onEvent error element is not byte[] !");
                        }
                    }//end of for
                }//end of if bufs not null
            }//end of if tts buffer event
        }

    };
}