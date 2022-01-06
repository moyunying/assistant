package cn.moyunying.assistant.controller;

import cn.moyunying.assistant.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class PostController {

    @Autowired
    private PostService postService;

    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public Map<String, Object> upload(@RequestBody Map<String, Object> data) {
        String cookie = (String) data.get("cookie");
        String format = (String) data.get("format");
        String base64 = (String) data.get("base64");
        return postService.upload(cookie, format, base64);
    }

    @RequestMapping(path = "/share", method = RequestMethod.POST)
    public Map<String, Object> share(@RequestBody Map<String, Object> data) {
        String cookie = (String) data.get("cookie");
        String title = (String) data.get("title");
        String content = (String) data.get("content");
        return postService.share(cookie, title, content);
    }
}
