package cn.moyunying.assistant.controller;

import cn.moyunying.assistant.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class PostController {

    @Autowired
    private PostService postService;

    @RequestMapping(path = "/share", method = RequestMethod.POST)
    public Map<String, Object> share(@RequestBody Map<String, Object> data) {
        String cookie = (String) data.get("cookie");
        String title = (String) data.get("title");
        String content = (String) data.get("content");
        return postService.share(cookie, title, content);
    }

    @RequestMapping(path = "/getPosts", method = RequestMethod.GET)
    public Map<String, Object> getPosts(@RequestParam int page) {
        return postService.getPosts(page);
    }


    @RequestMapping(path = "/getUserPosts", method = RequestMethod.GET)
    public Map<String, Object> getUserPosts(@RequestParam int userId,int page) {
        return postService.getUserPosts(userId, page);
    }
}
