package cn.moyunying.assistant;

import cn.moyunying.assistant.controller.LoginController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = AssistantApplication.class)
public class LoginTests {

    @Autowired
    private LoginController loginController;

    @Test
    public void testRegister() {
        Map<String, Object> data = new HashMap<>();
        String username = "zhangsan";
        String password = "123456";
        data.put("username", username);
        data.put("password", password);
        System.out.println(loginController.register(data));
    }
}
