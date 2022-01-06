package cn.moyunying.assistant.service;

import cn.moyunying.assistant.dao.PostMapper;
import cn.moyunying.assistant.entity.LoginTicket;
import cn.moyunying.assistant.entity.Post;
import cn.moyunying.assistant.util.AssistantUtil;
import cn.moyunying.assistant.util.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class PostService {

    @Autowired
    private UserService userService;

    @Autowired
    private PostMapper postMapper;

    public Map<String, Object> upload(String cookie, String format, String base64) {
        Map<String, Object> map = new HashMap<>();

        LoginTicket loginTicket = userService.isOnline(cookie);
        if (loginTicket == null) {
            map.put("code", 1);
            map.put("msg", "没有登录！");
            return map;
        }

        String key = AssistantUtil.generateUUID() + "." + format;
        String pictureUrl = UploadUtil.put64image(key, base64);
        if (pictureUrl == null) {
            map.put("code", 1);
            map.put("msg", "上传失败！");
            return map;
        }

        map.put("code", 0);
        map.put("msg", "上传成功！");
        map.put("pictureUrl", pictureUrl);
        return map;
    }

    public Map<String, Object> share(String cookie, String title, String content) {
        Map<String, Object> map = new HashMap<>();

        LoginTicket loginTicket = userService.isOnline(cookie);
        if (loginTicket == null) {
            map.put("code", 1);
            map.put("msg", "没有登录！");
            return map;
        }

        Post post = new Post();
        post.setUserId(loginTicket.getUserId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        postMapper.insertPost(post);

        map.put("code", 0);
        map.put("msg", "分享成功！");
        return map;
    }
}
