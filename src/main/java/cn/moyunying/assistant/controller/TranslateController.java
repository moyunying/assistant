package cn.moyunying.assistant.controller;

import cn.moyunying.assistant.service.TranslateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

@RestController
public class TranslateController {

    private static final String filePath = "D:\\audio\\";

    @Autowired
    private TranslateService translateService;

    @RequestMapping(path = "/pictureTranslate", method = RequestMethod.POST)
    public Map<String, Object> pictureTranslate(@RequestBody Map<String, Object> data) {
        int cc = (int) data.get("cc");
        String base64 = (String) data.get("base64");
        return translateService.pictureTranslate(cc, base64);
    }

    @RequestMapping(path = "/textTranslate", method = RequestMethod.POST)
    public Map<String, Object> textTranslate(@RequestBody Map<String, Object> data) {
        int cc = (int) data.get("cc");
        String text = (String) data.get("text");
        return translateService.textTranslate(cc, text);
    }

    @RequestMapping(path = "/audioTranslate", method = RequestMethod.POST)
    public Map<String, Object> audioTranslate(HttpServletRequest request,
                                              @RequestParam("audio") MultipartFile audio) {
        String cookie = request.getParameter("cookie");
        int cc = Integer.parseInt(request.getParameter("cc"));

        Map<String, Object> map = null;
        try {
            map = translateService.audioTranslate(cookie, cc, audio);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    @RequestMapping(path = "/download", method = RequestMethod.GET)
    public void downloadFile(@RequestParam String fileName, HttpServletResponse response) {
        System.out.println("哈哈");
        File file;
        FileInputStream in = null;
        ServletOutputStream out = null;
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setContentType("audio/mp3");
            file = ResourceUtils.getFile(filePath + fileName);
            in = new FileInputStream(file);
            out = response.getOutputStream();
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        }catch (Exception e){
            System.out.println("下载文件异常!");
        }finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.flush();
                }
                out.close();
            }catch (Exception e){
                System.out.println("流关闭异常!");
            }
        }
    }
}
