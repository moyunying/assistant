package cn.moyunying.assistant.util;


import com.iflytek.cloud.speech.*;

import java.util.ArrayList;

public class VoiceTranslateUtil {
    private void Synthesize() {
        SpeechSynthesizer speechSynthesizer = SpeechSynthesizer
                .createSynthesizer();
        // 设置发音人
        speechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");

        //启用合成音频流事件，不需要时，不用设置此参数
        speechSynthesizer.setParameter(SpeechConstant.TTS_BUFFER_EVENT, "1");
        // 设置合成音频保存位置（可自定义保存位置），默认不保存
        speechSynthesizer.synthesizeToUri("廖阿姨牛逼 ", "./tts_test.pcm",
                synthesizeToUriListener);
    }

    /**
     * 合成监听器
     */
    SynthesizeToUriListener synthesizeToUriListener = new SynthesizeToUriListener() {

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
            waitupLoop();
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

        private void waitupLoop() {
            synchronized (this) {
                VoiceTranslateUtil.this.notify();
            }
        }
    };
}