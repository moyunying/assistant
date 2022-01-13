package cn.moyunying.assistant;


public class FfmpegTest {

    public static void main(String[] args) {
        String sPath = "D:\\test_1.pcm";
        String tPath = "D:\\test_1.mp3";
        try {
            new FfmpegTest().changeAmrToMp3(sPath, tPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeAmrToMp3(String sourcePath, String targetPath) throws Exception {
//        String webroot = "D:\\ffmpeg\\bin\\ffmpeg.exe";
        Runtime run = null;
        try {
            run = Runtime.getRuntime();
            Process p = run.exec( "D:\\ffmpeg\\bin\\ffmpeg.exe -y -ac 1 -ar 16000 -f s16le -i " + sourcePath + "  -c:a libmp3lame -q:a 2 " + targetPath);
//            Process p=run.exec("D:\\ffmpeg\\bin\\ffmpeg.exe -y -i "+sourcePath+" -acodec pcm_s16le -f s16le -ac 1 -ar 16000 "+targetPath);

            //释放进程
            p.getOutputStream().close();
            p.getInputStream().close();
            p.getErrorStream().close();
            p.waitFor();
            long end = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //run调用lame解码器最后释放内存
            run.freeMemory();
        }

        String f = "fsfdfs.sf";
        System.out.println(f.substring(0, f.lastIndexOf(".")));
    }
}