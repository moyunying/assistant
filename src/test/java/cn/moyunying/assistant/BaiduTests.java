package cn.moyunying.assistant;

import cn.moyunying.assistant.util.AuthUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = AssistantApplication.class)
public class BaiduTests {

    @Test
    public void testAuth() {
        System.out.println(AuthUtil.getAuth());
    }
}
