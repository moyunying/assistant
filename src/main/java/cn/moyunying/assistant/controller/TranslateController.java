package cn.moyunying.assistant.controller;

import cn.moyunying.assistant.service.TranslateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class TranslateController {

    @Autowired
    private TranslateService translateService;

    @RequestMapping(path = "/pictureTranslate", method = RequestMethod.POST)
    public Map<String, Object> pictureTranslate(@RequestBody Map<String, Object> data) {
        int cc = (int) data.get("cc");
        String base64 = (String) data.get("base64");
        return translateService.pictureTranslate(cc, base64);
    }

    @RequestMapping(path = "/textTranslate", method = RequestMethod.POST)
    public Map<String, Object> textTranslate(@RequestBody Map<String, Object> data) {
        int cc = (int) data.get("cc");
        String text = (String) data.get("text");
        return translateService.textTranslate(cc, text);
    }

    @RequestMapping(path = "/audioTranslate", method = RequestMethod.POST)
    public Map<String, Object> audioTranslate(HttpServletRequest request,
                                              @RequestParam("audio") MultipartFile audio) {
        String cookie = request.getParameter("cookie");
        int cc = Integer.parseInt(request.getParameter("cc"));

        Map<String, Object> map = null;
        try {
            map = translateService.audioTranslate(cookie, cc, audio);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }
}
