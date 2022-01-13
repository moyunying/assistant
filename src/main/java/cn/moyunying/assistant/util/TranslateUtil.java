package cn.moyunying.assistant.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class TranslateUtil {

    private static final String pictureUrl = "https://fanyi-api.baidu.com/api/trans/sdk/picture";
    private static final String textUrl = "https://fanyi-api.baidu.com/api/trans/vip/translate";
    private static final String appid = "20211231001043197";
    private static final String secretKey = "_2AsIVXUAHLc7BI1pisv";

    public static Map<String, Object> pictureTranslate(String file64, String from, String to) {
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
                .url(pictureUrl)
                .post(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.body() != null) {
                Map<String, Object> map = new HashMap<>();
                JSONObject data = JSON.parseObject(response.body().string()).getJSONObject("data");
                map.put("text", data.getString("sumDst"));
                map.put("img", data.getString("pasteImg"));
                return map;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (response != null){
                response.close();
            }
        }
    }

    public static String textTranslate(String q, String from, String to) {
        String salt = AssistantUtil.generateUUID().substring(0, 10);
        String sign = AssistantUtil.md5(appid + q + salt + secretKey);

        RequestBody requestBody = new FormBody.Builder()
                .add("q", q)
                .add("from", from)
                .add("to", to)
                .add("appid", appid)
                .add("salt", salt)
                .add("sign", sign)
                .build();
        Request request = new Request.Builder()
                .url(textUrl)
                .post(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.body() != null) {
                Map<String, Object> map = new HashMap<>();
                JSONArray transResult = JSON.parseObject(response.body().string()).getJSONArray("trans_result");
                return transResult.getJSONObject(0).getString("dst");
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (response != null){
                response.close();
            }
        }
    }
}
