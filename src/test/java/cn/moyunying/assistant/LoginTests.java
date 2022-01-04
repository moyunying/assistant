package cn.moyunying.assistant;

import cn.moyunying.assistant.controller.LoginController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = AssistantApplication.class)
public class LoginTests {

    @Autowired
    private LoginController loginController;

    @Test
    public void testRegister() {
        String username = "";
        String password = "";
        System.out.println(loginController.register(username, password));

        username = "lisi";
        System.out.println(loginController.register(username, password));

        password = "12345678";
        System.out.println(loginController.register(username, password));

        username = "zhangsan";
        System.out.println(loginController.register(username, password));
    }
}
