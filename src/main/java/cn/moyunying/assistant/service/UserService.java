package cn.moyunying.assistant.service;

import cn.moyunying.assistant.dao.LoginTicketMapper;
import cn.moyunying.assistant.dao.UserMapper;
import cn.moyunying.assistant.entity.LoginTicket;
import cn.moyunying.assistant.entity.User;
import cn.moyunying.assistant.util.AssistantConstant;
import cn.moyunying.assistant.util.AssistantUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService implements AssistantConstant {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    public Map<String, Object> register(String username, String password) {
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (StringUtils.isBlank(username)) {
            map.put("code", 1);
            map.put("msg", "账号不能为空!");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("code", 1);
            map.put("msg", "密码不能为空!");
            return map;
        }

        // 验证账号
        User u = userMapper.selectByName(username);
        if (u != null) {
            map.put("code", 1);
            map.put("msg", "该账号已存在！");
            return map;
        }

        // 注册用户
        User user = new User();
        user.setUsername(username);
        user.setSalt(AssistantUtil.generateUUID().substring(0, 5));
        user.setPassword(AssistantUtil.md5(password + user.getSalt()));
        user.setType(1);
        user.setHeaderUrl(String.format("https://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        user.setExpireTime(new Date(System.currentTimeMillis() + MEMBER_EXPIRE_SECONDS * 1000));  // 注册账号即送7天会员
        userMapper.insertUser(user);
        map.put("code", 0);
        map.put("msg", "注册成功！");

        return map;
    }

    public Map<String, Object> login(String username, String password) {
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (StringUtils.isBlank(username)) {
            map.put("code", 1);
            map.put("msg", "账号不能为空!");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("code", 1);
            map.put("msg", "密码不能为空!");
            return map;
        }

        // 验证账号
        User user = userMapper.selectByName(username);
        if (user == null) {
            map.put("code", 1);
            map.put("msg", "该账号不存在!");
            return map;
        }

        // 验证密码
        password = AssistantUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            map.put("code", 1);
            map.put("msg", "密码不正确!");
            return map;
        }

        //检查会员是否到期
        if (user.getExpireTime().before(new Date())) {
            userMapper.updateType(user.getId(), 0);
        }

        // 生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setCookie(AssistantUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpireTime(new Date(System.currentTimeMillis() + LOGIN_EXPIRE_SECONDS * 1000L));
        loginTicketMapper.insertLoginTicket(loginTicket);

        map.put("code", 0);
        map.put("msg", "登录成功！");
        map.put("cookie", loginTicket.getCookie());
        map.put("username", user.getUsername());
        map.put("type", user.getType());
        map.put("headerUrl", user.getHeaderUrl());
        return map;
    }

    public Map<String, Object> logout(String cookie) {
        Map<String, Object> map = new HashMap<>();

        loginTicketMapper.updateStatus(cookie, 1);

        map.put("code", 0);
        map.put("msg", "退出成功！");
        return map;
    }
}
