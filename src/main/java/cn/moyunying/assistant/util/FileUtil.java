package cn.moyunying.assistant.util;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

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
            p.exitValue();

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


    public static String pcmToMp3(String sourceFileName) {
        String targetFileName = sourceFileName.substring(0, sourceFileName.lastIndexOf(".")) + ".mp3";
        String sourcePath = path + sourceFileName;
        String targetPath = path + targetFileName;

        String cmd = ffmpeg + "  -y -f s16be -ac 2 -ar 16000 -acodec pcm_s16le -i " + sourcePath + " " + targetPath;

        Runtime run = null;
        System.out.println(run);
        try {
            int i = 1;
            run = Runtime.getRuntime();
            do {
                File file = new File(sourcePath);
                System.out.println(file+"1");
                if(file.exists()){
                    System.out.println(file);
                    Process p = run.exec(cmd);

                    //释放进程
                    p.getOutputStream().close();
                    p.getInputStream().close();
                    p.getErrorStream().close();
                    p.waitFor();
                    break;}
                else{
                    try {
                        Thread.sleep(i*1000); //每次循环多等待一秒
                    } catch(InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
                i++;
            }while (i<=6);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //run调用lame解码器最后释放内存
            if (run != null) {
                run.freeMemory();
            }
        }
        System.out.println("语音转换执行完毕！");

        return targetFileName;
    }
}