package cn.moyunying.assistant.service;

import cn.moyunying.assistant.dao.PostMapper;
import cn.moyunying.assistant.dao.UserMapper;
import cn.moyunying.assistant.entity.LoginTicket;
import cn.moyunying.assistant.entity.Post;
import cn.moyunying.assistant.entity.User;
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
        List<Post> list = postMapper.selectPosts(offset, limit);

        if (list == null) {
            map.put("code", 1);
            map.put("msg", "获取帖子列表失败！");
            return map;
        }

        List<Map<String, Object>> posts = new ArrayList<>();
        for (Post post : list) {
            Map<String, Object> postInfo = new HashMap<>();
            User user = userMapper.selectById(post.getUserId());
            postInfo.put("username", user.getUsername());
            postInfo.put("headerUrl", user.getHeaderUrl());
            postInfo.put("title", post.getTitle());
            postInfo.put("content", post.getContent());
            postInfo.put("creatTime", post.getCreateTime());
            posts.add(postInfo);
        }
        map.put("posts", posts);
        map.put("page", page);
        map.put("total", postMapper.selectTotal(0) / limit + 1);

        map.put("code", 0);
        map.put("msg", "获取帖子列表成功！");
        return map;
    }

    public Map<String, Object> getUserPosts(int userId,int page) {
        Map<String, Object> map = new HashMap<>();


        int limit = 10;
        int offset = (page - 1) * limit;

        List<Post> p = postMapper.selectPostByUserId(userId,offset, limit);

        //用户未发帖
        if (p == null || page>(postMapper.selectTotal(userId) / limit + 1)) {
            map.put("code", 1);
            map.put("msg", "获取帖子列表失败！");
            return map;
        }

        map.put("page", page);
        map.put("total", postMapper.selectTotal(userId) / limit + 1);

        List<Map<String,Object>> posts = new ArrayList<>();
        for(Post post : p)
        {
            Map<String,Object> postvo = new HashMap<>();
            User user = userMapper.selectById(post.getUserId());
            postvo.put("username",user.getUsername());
            postvo.put("headerUrl",user.getHeaderUrl());
            postvo.put("title",post.getTitle());
            postvo.put("content",post.getContent());
            postvo.put("createtime",post.getCreateTime());
            posts.add(postvo);
        }
        map.put("posts", posts);

        map.put("code", 0);
        map.put("msg", "获取帖子列表成功！");
        return map;
    }
}
