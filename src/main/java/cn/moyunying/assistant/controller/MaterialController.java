package cn.moyunying.assistant.controller;

import cn.moyunying.assistant.service.MaterialService;
import cn.moyunying.assistant.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @RequestMapping(path = "/getMaterial", method = RequestMethod.GET)
    public Map<String , Object> getScenic(@RequestParam String keyword,
                                            @RequestParam int page){
        return materialService.getMaterial(keyword, page);
    }

    @RequestMapping(path = "/shareMaterial", method = RequestMethod.POST)
    public Map<String, Object> shareMaterial(@RequestBody Map<String, Object> data) {
        String cookie = (String) data.get("cookie");
        String title = (String) data.get("title");
        String content = (String) data.get("content");
        String keyword = (String) data.get("keyword");
        return materialService.shareMaterial(cookie, title, content, keyword);
    }
}
