package cn.moyunying.assistant.service;

import cn.moyunying.assistant.dao.LoginTicketMapper;
import cn.moyunying.assistant.dao.UserMapper;
import cn.moyunying.assistant.entity.LoginTicket;
import cn.moyunying.assistant.entity.User;
import cn.moyunying.assistant.util.AssistantConstant;
import cn.moyunying.assistant.util.AssistantUtil;
import cn.moyunying.assistant.util.UploadUtil;
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
            user.setType(0);
            userMapper.updateType(user.getId(), user.getType());
        }

        LoginTicket loginTicket = loginTicketMapper.selectLoginTicketByUserId(user.getId());
        if (loginTicket == null) {
            loginTicket = new LoginTicket();
            loginTicket.setUserId(user.getId());
            loginTicket.setCookie(AssistantUtil.generateUUID());
            loginTicket.setStatus(0);
            loginTicket.setExpireTime(new Date(System.currentTimeMillis() + LOGIN_EXPIRE_SECONDS * 1000));
            loginTicketMapper.insertLoginTicket(loginTicket);
        }

        map.put("code", 0);
        map.put("msg", "登录成功！");
        map.put("cookie", loginTicket.getCookie());
        map.put("username", user.getUsername());
        map.put("type", user.getType());
        map.put("headerUrl", user.getHeaderUrl());
        map.put("expireTime", user.getExpireTime());
        return map;
    }

    public Map<String, Object> logout(String cookie) {
        Map<String, Object> map = new HashMap<>();

        LoginTicket loginTicket = isOnline(cookie);
        if (loginTicket == null) {
            map.put("code", 1);
            map.put("msg", "没有登录！");
            return map;
        }

        loginTicketMapper.updateStatus(cookie, 1);

        map.put("code", 0);
        map.put("msg", "退出成功！");
        return map;
    }

    //个人主页
    public Map<String, Object> myHome(String cookie){
        Map<String, Object> map = new HashMap<>();

        LoginTicket loginTicket = isOnline(cookie);
        if (loginTicket == null) {
            map.put("code", 1);
            map.put("msg", "没有登录！");
            return map;
        }

        User user = userMapper.selectById(loginTicket.getUserId());

        map.put("code", 0);
        map.put("msg", "个人主页获取成功！");
        map.put("username", user.getUsername());
        map.put("type", user.getType());
        map.put("headerUrl", user.getHeaderUrl());
        map.put("expireTime",user.getExpireTime());
        return map;
    }

    public Map<String, Object> changePassword(String cookie, String password) {
        Map<String, Object> map = new HashMap<>();

        LoginTicket loginTicket = isOnline(cookie);
        if (loginTicket == null) {
            map.put("code", 1);
            map.put("msg", "没有登录！");
            return map;
        }

        User user = userMapper.selectById(loginTicket.getUserId());
        password = AssistantUtil.md5(password + user.getSalt());
        userMapper.updatePassword(user.getId(), password);
        loginTicketMapper.updateStatus(cookie, 1);

        map.put("code", 0);
        map.put("msg", "修改密码成功！");
        return map;
    }

    public Map<String, Object> changeHeader(String cookie, String format, String base64) {
        Map<String, Object> map = new HashMap<>();

        LoginTicket loginTicket = isOnline(cookie);
        if (loginTicket == null) {
            map.put("code", 1);
            map.put("msg", "没有登录！");
            return map;
        }

        String key = AssistantUtil.generateUUID() + "." + format;
        String headerUrl = UploadUtil.put64image(key, base64);
        if (headerUrl == null) {
            map.put("code", 1);
            map.put("msg", "上传失败！");
            return map;
        }

        userMapper.updateHeaderUrl(loginTicket.getUserId(), headerUrl);

        map.put("code", 0);
        map.put("msg", "上传成功！");
        map.put("headerUrl", headerUrl);
        return map;
    }

    public LoginTicket isOnline(String cookie) {
        LoginTicket loginTicket = loginTicketMapper.selectLoginTicketByCookie(cookie);

        if (loginTicket == null) {
            return null;
        }

        if (loginTicket.getExpireTime().before(new Date())) {
            loginTicketMapper.updateStatus(cookie, 1);
            return null;
        }

        return loginTicket;
    }

    public Map<String, Object> getPopularUsers(int offset, int limit) {
        Map<String, Object> map = new HashMap<>();

        List<User> list = userMapper.selectPopularUsers(offset, limit);

        if (list.isEmpty()) {
            map.put("code", 1);
            map.put("msg", "获取热门用户失败！");
            return map;
        }

        List<Map<String, Object>> users = new ArrayList<>();
        for (User user : list) {
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userId", user.getId());
            userInfo.put("username", user.getUsername());
            userInfo.put("headerUrl", user.getHeaderUrl());
            users.add(userInfo);
        }
        map.put("users", users);

        map.put("code", 0);
        map.put("msg", "获取热门用户成功！");
        return map;
    }
}
