package cn.moyunying.assistant.util;

import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.util.Base64;

public class TranslateUtil {

    private static final String url = "https://fanyi-api.baidu.com/api/trans/sdk/picture";
    private static final String appid = "20211231001043197";
    private static final String secretKey = "_2AsIVXUAHLc7BI1pisv";

    public static String pictureTranslate(String file64, String from, String to) {
        byte[] image = Base64.getDecoder().decode(file64);
        String salt = AssistantUtil.generateUUID().substring(0, 10);
        String sign = AssistantUtil.md5(appid + DigestUtils.md5DigestAsHex(image) + salt + "APICUID" + "mac" + secretKey);

        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), image);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "image", fileBody)
                .addFormDataPart("from", from)
                .addFormDataPart("to", to)
                .addFormDataPart("appid", appid)
                .addFormDataPart("salt", salt)
                .addFormDataPart("cuid", "APICUID")
                .addFormDataPart("mac", "mac")
                .addFormDataPart("version", "3")
                .addFormDataPart("paste", "1")
                .addFormDataPart("sign", sign)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient();
        try {
            Response response = client.newCall(request).execute();
            return JSON.parseObject(response.body().string()).getJSONObject("data").getString("pasteImg");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
