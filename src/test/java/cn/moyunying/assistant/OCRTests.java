package cn.moyunying.assistant;

import cn.moyunying.assistant.utils.OCRUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = AssistantApplication.class)
public class OCRTests {

    @Test
    public void testOCR() {
        String path = "C:\\Users\\17439\\Downloads\\test.jpg";
        String languageType = "JAP";
        System.out.println(OCRUtil.getContent(path, languageType));
    }
}
