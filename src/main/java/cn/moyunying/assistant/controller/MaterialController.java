package cn.moyunying.assistant.controller;

import cn.moyunying.assistant.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @RequestMapping(path = "/shareMaterial", method = RequestMethod.POST)
    public Map<String, Object> shareMaterial(@RequestBody Map<String, Object> data) {
        String cookie = (String) data.get("cookie");
        String title = (String) data.get("title");
        String content = (String) data.get("content");
        String format = (String) data.get("format");
        String base64 = (String) data.get("base64");
        return materialService.shareMaterial(cookie, title, content, format, base64);
    }

    @RequestMapping(path = "/getMaterials", method = RequestMethod.GET)
    public Map<String, Object> getMaterials() {
        return materialService.getMaterials();
    }

    @RequestMapping(path = "/getMaterialById", method = RequestMethod.GET)
    public Map<String, Object> getMaterialById(@RequestParam int id){
        return materialService.getMaterialById(id);
    }
}
