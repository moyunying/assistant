package cn.moyunying.assistant.service;

import cn.moyunying.assistant.dao.UserMapper;
import cn.moyunying.assistant.entity.LoginTicket;
import cn.moyunying.assistant.entity.User;
import cn.moyunying.assistant.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class TranslateService implements AssistantConstant {

    private static final String path = "D:\\audio\\";

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

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

        if (text.isEmpty()) {
            map.put("code", 1);
            map.put("msg", "没有文本！");
            return map;
        }

        String dst;
        if (cc == 0) {
            dst = TranslateUtil.textTranslate(text, ZH, JP);
        } else {
            dst = TranslateUtil.textTranslate(text, JP, ZH);
        }

        if (dst != null) {
            map.put("dst", dst);
            map.put("code", 0);
            map.put("msg1", "文本翻译成功！");
        }else {
            map.put("code", 1);
            map.put("msg", "文本翻译失败！");
        }

        return map;
    }

    public Map<String, Object> audioTranslate(String cookie, int cc, MultipartFile audio) throws Exception {
        Map<String, Object> map = new HashMap<>();

        LoginTicket loginTicket = userService.isOnline(cookie);
        if (loginTicket == null) {
            map.put("code", 1);
            map.put("msg", "没有登录！");
            return map;
        }

        User user = userMapper.selectById(loginTicket.getUserId());
        if (user.getType() == 0) {
            map.put("code", 1);
            map.put("msg", "普通用户无法使用此功能！");
            return map;
        }

        //保存文件到本地
        String sourceFileName = FileUtil.saveToLocal(audio);
        // mp3转pcm
        String targetFileName = FileUtil.mp3ToPcm(sourceFileName);

        Map<String, Object> result = null;
        if (cc == 0) {
            result = AudioUtil.audioTranslate(targetFileName, CN, JA);
        } else {
            result = AudioUtil.audioTranslate(targetFileName, JA, CN);
        }

        sourceFileName = VoiceTranslateUtil.Synthesize((String) result.get("dst"), cc);
        targetFileName = FileUtil.pcmToMp3(sourceFileName);

        if (result != null) {
            map.put("dst", result.get("dst"));
            map.put("src", result.get("src"));
            map.put("fileName", targetFileName);
            map.put("code", 0);
            map.put("msg", "语音翻译成功！");
        } else {
            map.put("code", 1);
            map.put("msg", "语音翻译失败！");
        }

        return map;
    }
}
