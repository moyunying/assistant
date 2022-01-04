package cn.moyunying.assistant.controller;

import cn.moyunying.assistant.entity.User;
import cn.moyunying.assistant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public Map<String, Object> register(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return userService.register(user);
    }
}
