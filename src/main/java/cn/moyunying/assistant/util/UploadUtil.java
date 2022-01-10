package cn.moyunying.assistant.util;

import com.qiniu.util.Auth;
import com.qiniu.util.Base64;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

public class UploadUtil {

    private static final String ak = "b28pnqLa69zIolzE7PtrolGhN_ujdza6bZIHj-hG";
    private static final String sk = "HaC3gZUBOKIim-zQRMp8HiZfcbEdoSXKyaaaRzGq";  // 密钥配置
    private static final Auth auth = Auth.create(ak, sk);
    private static final String bucketname = "moyunying";  // 空间名
    private static final String domain = "http://r58906tpn.hd-bkt.clouddn.com/";

    public static String getUpToken() {
        return auth.uploadToken(bucketname, null, 3600, new StringMap().put("insertOnly", 1));
    }

    public static String put64image(String key, String file64) {
        int l = Base64.decode(file64, 0).length;
        String url = "http://upload.qiniup.com/putb64/" + l + "/key/" + UrlSafeBase64.encodeToString(key);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/octet-stream")
                .addHeader("Authorization", "UpToken " + getUpToken())
                .post(RequestBody.create(null, file64))
                .build();

        OkHttpClient client = new OkHttpClient();
        try {
            Response response = client.newCall(request).execute();
            if (response.message().equals("OK")) {
                return domain + key;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
