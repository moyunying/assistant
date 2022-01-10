package cn.moyunying.assistant.controller;

import cn.moyunying.assistant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/myHome", method = RequestMethod.POST)
    public Map<String, Object> myHome(@RequestBody Map<String, Object> data){
        String cookie = (String) data.get("cookie");
        return userService.myHome(cookie);
    }

    @RequestMapping(path = "/changePassword", method = RequestMethod.POST)
    public Map<String, Object> changePassword(@RequestBody Map<String, Object> data) {
        String cookie = (String) data.get("cookie");
        String password = (String) data.get("password");
        return userService.changePassword(cookie, password);
    }

    @RequestMapping(path = "/changeHeader", method = RequestMethod.POST)
    public Map<String, Object> changeHeader(@RequestBody Map<String, Object> data) {
        String cookie = (String) data.get("cookie");
        String format = (String) data.get("format");
        String base64 = (String) data.get("base64");
        return userService.changeHeader(cookie, format, base64);
    }

    @RequestMapping(path = "/getPopularUsers", method = RequestMethod.GET)
    public Map<String, Object> getPopularUsers(@RequestParam int offset,
                                        @RequestParam int limit) {
        return userService.getPopularUsers(offset, limit);
    }

    @RequestMapping(path = "/recharge",method = RequestMethod.POST)
    public Map<String, Object> rechage(@RequestParam String cookie,int expireMonths){
        return userService.recharge(cookie,expireMonths);
    }
}
