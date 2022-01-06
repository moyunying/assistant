package cn.moyunying.assistant.service;

import cn.moyunying.assistant.dao.PostMapper;
import cn.moyunying.assistant.dao.UserMapper;
import cn.moyunying.assistant.entity.LoginTicket;
import cn.moyunying.assistant.entity.Post;
import cn.moyunying.assistant.entity.User;
import cn.moyunying.assistant.entity.vo.PostVo;
import cn.moyunying.assistant.util.AssistantUtil;
import cn.moyunying.assistant.util.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PostService {

    @Autowired
    private UserService userService;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserMapper userMapper;

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

    public Map<String, Object> getPosts(int page) {
        Map<String, Object> map = new HashMap<>();

        int limit = 10;
        int offset = (page - 1) * limit;
        map.put("page", page);
        map.put("total", postMapper.selectTotal() / limit + 1);
        List<Post> list = postMapper.selectPosts(offset, limit);

        List<PostVo> posts = null;
        if (list != null) {
            posts = new ArrayList<>();
            for (Post post : list) {
                PostVo postVo = new PostVo();
                User user = userMapper.selectById(post.getUserId());
                postVo.setUsername(user.getUsername());
                postVo.setHeaderUrl(user.getHeaderUrl());
                postVo.setTitle(post.getTitle());
                postVo.setContent(post.getContent());
                postVo.setCreateTime(post.getCreateTime());
                posts.add(postVo);
            }
        }
        map.put("posts", posts);

        map.put("code", 0);
        map.put("msg", "获取帖子列表成功！");
        return map;
    }
}
