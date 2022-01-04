package cn.moyunying.assistant.service;

import cn.moyunying.assistant.dao.UserMapper;
import cn.moyunying.assistant.entity.User;
import cn.moyunying.assistant.util.AssistantUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (user == null) {
            throw new IllegalArgumentException("参数不能为空！");
        }
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("code", 1);
            map.put("msg", "账号不能为空！");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("code", 1);
            map.put("msg", "密码不能为空！");
            return map;
        }

        // 验证账号
        User u = userMapper.selectByName(user.getUsername());
        if (u != null) {
            map.put("code", 1);
            map.put("msg", "该账号已存在！");
            return map;
        }

        // 注册用户
        user.setSalt(AssistantUtil.generateUUID().substring(0, 5));
        user.setPassword(AssistantUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(1);
        user.setHeaderUrl(String.format("https://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        // 注册账号即送一年会员
        Calendar cal = Calendar.getInstance();
        cal.setTime(user.getCreateTime());  // 设置起时间
        cal.add(Calendar.YEAR, 1);  // 增加一年
        user.setExpireTime(cal.getTime());
        userMapper.insertUser(user);
        map.put("code", 0);
        map.put("msg", "success");

        return map;
    }
}
