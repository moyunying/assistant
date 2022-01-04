package cn.moyunying.assistant.controller;

import cn.moyunying.assistant.entity.User;
import cn.moyunying.assistant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String test() {
        return "Hello World!";
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public Map<String, Object> register(@RequestBody Map<String, Object> data) {
        User user = new User();
        user.setUsername((String) data.get("username"));
        user.setPassword((String) data.get("password"));
        return userService.register(user);
    }
}
