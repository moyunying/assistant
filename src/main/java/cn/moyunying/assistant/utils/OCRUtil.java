package cn.moyunying.assistant.utils;

import java.net.URLEncoder;

public class OCRUtil {

    public static String getContent(String path, String languageType) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic";
        try {
            byte[] imgData = FileUtil.readFileByBytes(path);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "language_type=" + languageType + "&image=" + imgParam;

            String accessToken = AuthUtil.getAuth();

            String result = HttpUtil.post(url, accessToken, param);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
