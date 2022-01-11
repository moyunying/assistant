package cn.moyunying.assistant.service;

import cn.moyunying.assistant.util.AssistantConstant;
import cn.moyunying.assistant.util.TranslateUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TranslateService implements AssistantConstant {

    public Map<String, Object> pictureTranslate(int cc, String base64) {
        Map<String, Object> map;

        if (cc == 0) {
            map = TranslateUtil.pictureTranslate(base64, ZH, JP);
        } else {
            map = TranslateUtil.pictureTranslate(base64, JP, ZH);
        }

        if (map != null) {
            map.put("code", 0);
            map.put("msg", "图片翻译成功！");
        } else {
            map = new HashMap<>();
            map.put("code", 1);
            map.put("msg", "图片翻译失败！");
        }

        return map;
    }

    public Map<String, Object> textTranslate(int cc, String text) {
        Map<String, Object> map = new HashMap<>();

        String dst;
        if (cc == 0) {
            dst = TranslateUtil.textTranslate(text, ZH, JP);
        } else {
            dst = TranslateUtil.textTranslate(text, JP, ZH);
        }

        if (dst != null) {
            map.put("dst", dst);
            map.put("code", 0);
            map.put("msg", "文本翻译成功！");
        } else {
            map.put("code", 1);
            map.put("msg", "文本翻译失败！");
        }

        return map;
    }
}
