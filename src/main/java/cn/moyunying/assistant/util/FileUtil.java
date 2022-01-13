package cn.moyunying.assistant.util;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class FileUtil {

    private static final String path = "D:\\audio\\";

    private static final String ffmpeg = "D:\\ffmpeg\\bin\\ffmpeg.exe";

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
        String filePath = path + fileName;
        // 保存文件
        try {
            FileUtils.writeByteArrayToFile(new File(filePath), audio.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileName;
    }

    public static String mp3ToPcm(String sourceFileName) {
        String targetFileName = sourceFileName.substring(0, sourceFileName.lastIndexOf(".")) + ".pcm";
        String sourcePath = path + sourceFileName;
        String targetPath = path + targetFileName;
        String cmd = ffmpeg + " -y -i " + sourcePath + " -acodec pcm_s16le -f s16le -ac 1 -ar 16000 " + targetPath;

        Runtime run = null;
        try {
            run = Runtime.getRuntime();
            Process p = run.exec(cmd);

            //释放进程
            p.getOutputStream().close();
            p.getInputStream().close();
            p.getErrorStream().close();
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //run调用lame解码器最后释放内存
            if (run != null) {
                run.freeMemory();
            }
        }

        return targetFileName;
    }
}
