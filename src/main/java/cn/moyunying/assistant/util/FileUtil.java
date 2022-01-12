package cn.moyunying.assistant.util;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class FileUtil {

    private static final String path = "D:\\Projects\\IdeaProjects\\assistant\\src\\main\\resources\\audio";

    public static String saveToLocal(MultipartFile audio) {
        // 获取文件的 名称.扩展名
        String oldName = audio.getOriginalFilename();
        String extensionName = null;
        if (oldName != null) {
            extensionName = oldName.substring(oldName.lastIndexOf('.'));
        }

        // 构建文件名称
        String fileName = System.currentTimeMillis() + extensionName;
        // 构建文件路径
        String filePath = path + File.separator + fileName;
        // 保存文件
        try {
            FileUtils.writeByteArrayToFile(new File(filePath), audio.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileName;
    }
}
