package cn.moyunying.assistant.service;

import cn.moyunying.assistant.dao.UserMapper;
import cn.moyunying.assistant.entity.LoginTicket;
import cn.moyunying.assistant.entity.User;
import cn.moyunying.assistant.util.AssistantConstant;
import cn.moyunying.assistant.util.AudioUtil;
import cn.moyunying.assistant.util.FileUtil;
import cn.moyunying.assistant.util.TranslateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TranslateService implements AssistantConstant {

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

    public Map<String, Object> audioTranslate(String cookie, int cc, MultipartFile audio) throws Exception {
        Map<String, Object> map = new HashMap<>();

        LoginTicket loginTicket = userService.isOnline(cookie);
        if (loginTicket == null) {
            map.put("code", 1);
            map.put("msg", "没有登录！");
            return map;
        }

        User user = userMapper.selectById(loginTicket.getUserId());
        if (user.getType() != 1) {
            map.put("code", 1);
            map.put("msg", "不是会员无法使用此功能！");
            return map;
        }

        //保存文件到本地
        String fileName = FileUtil.saveToLocal(audio);

        String result = null;
        if (cc == 0) {
            result = AudioUtil.audioTranslate(fileName, CN, JA);
        } else {
            try {
                result = AudioUtil.audioTranslate(fileName, JA, CN);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (result != null) {
            map.put("result", result);
            map.put("code", 0);
            map.put("msg", "语音翻译成功！");
        } else {
            map.put("code", 1);
            map.put("msg", "语音翻译失败！");
        }

        return map;
    }
}
