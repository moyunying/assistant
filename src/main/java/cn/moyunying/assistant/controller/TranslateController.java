package cn.moyunying.assistant.controller;

import cn.moyunying.assistant.service.TranslateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
}
