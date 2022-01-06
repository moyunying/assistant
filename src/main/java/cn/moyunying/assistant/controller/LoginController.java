package cn.moyunying.assistant.controller;

import cn.moyunying.assistant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        String username = (String) data.get("username");
        String password = (String) data.get("password");
        return userService.register(username, password);
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public Map<String, Object> login(@RequestBody Map<String, Object> data) {
        String username = (String) data.get("username");
        String password = (String) data.get("password");
        return userService.login(username, password);
    }

    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public Map<String, Object> logout(@RequestBody Map<String, Object> data) {
        String cookie = (String) data.get("cookie");
        return userService.logout(cookie);
    }
}
