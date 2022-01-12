package cn.moyunying.assistant.util;

import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.istack.NotNull;
import org.java_websocket.WebSocket.READYSTATE;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ClientHandshakeBuilder;
import org.java_websocket.handshake.ServerHandshake;

public class AudioUtil {

    // appid
    private static final String APPID = "f0b9a7d9";

    // appid对应的secret_key
    private static final String SECRET_KEY = "822ad9c8ebcb9897fb1cc44de2a1561a";

    // 请求地址
    private static final String HOST = "rtasr.xfyun.cn/v1/ws";

    private static final String BASE_URL = "ws://" + HOST;

    private static final String ORIGIN = "http://" + HOST;

    // 音频文件夹路径
    private static final String AUDIO_PATH = "D:\\Projects\\IdeaProjects\\assistant\\src\\main\\resources\\audio\\";

    // 每次发送的数据大小 1280 字节
    private static final int CHUNCKED_SIZE = 1280;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.SSS");

    public static String audioTranslate(String fileName, String lang, String targetLang) throws Exception {
        URI url = new URI(BASE_URL + getHandShakeParams(APPID, SECRET_KEY, lang, targetLang));
        DraftWithOrigin draft = new DraftWithOrigin(ORIGIN);
        CountDownLatch handshakeSuccess = new CountDownLatch(1);
        CountDownLatch connectClose = new CountDownLatch(1);
        MyWebSocketClient client = new MyWebSocketClient(url, draft, handshakeSuccess, connectClose);

        while (true) {
            client.connect();

            while (!client.getReadyState().equals(READYSTATE.OPEN)) {
                System.out.println(getCurrentTimeStr() + "\t连接中");
                Thread.sleep(1000);
            }

            // 等待握手成功
            handshakeSuccess.await();
            System.out.println(sdf.format(new Date()) + " 开始发送音频数据");
            // 发送音频
            byte[] bytes = new byte[CHUNCKED_SIZE];
            try (RandomAccessFile raf = new RandomAccessFile(AUDIO_PATH + fileName, "r")) {
                int len = -1;
                long lastTs = 0;
                while ((len = raf.read(bytes)) != -1) {
                    if (len < CHUNCKED_SIZE) {
                        send(client, bytes = Arrays.copyOfRange(bytes, 0, len));
                        break;
                    }

                    long curTs = System.currentTimeMillis();
                    if (lastTs == 0) {
                        lastTs = System.currentTimeMillis();
                    } else {
                        long s = curTs - lastTs;
                        if (s < 40) {
                            System.out.println("error time interval: " + s + " ms");
                        }
                    }
                    send(client, bytes);
                    // 每隔40毫秒发送一次数据
                    Thread.sleep(40);
                }

                // 发送结束标识
                send(client,"{\"end\": true}".getBytes());
                System.out.println(getCurrentTimeStr() + "\t发送结束标识完成");
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 等待连接关闭
            connectClose.await();
            break;
        }

        return client.result;
    }

    // 生成握手参数
    public static String getHandShakeParams(String appId, String secretKey, String lang, String targetLang) {
        String ts = System.currentTimeMillis()/1000 + "";
        String signa = "";
        try {
            signa = AssistantUtil.HmacSHA1Encrypt(Objects.requireNonNull(AssistantUtil.md5(appId + ts)), secretKey);
            return "?appid=" + appId
                    + "&ts=" + ts
                    + "&signa=" + URLEncoder.encode(signa, "UTF-8")
                    + "&lang=" + lang
                    + "&transStrategy=2"
                    + "&targetLang=" + targetLang;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static void send(WebSocketClient client, byte[] bytes) {
        if (client.isClosed()) {
            throw new RuntimeException("client connect closed!");
        }

        client.send(bytes);
    }

    public static String getCurrentTimeStr() {
        return sdf.format(new Date());
    }

    public static class MyWebSocketClient extends WebSocketClient {

        private CountDownLatch handshakeSuccess;
        private CountDownLatch connectClose;
        private String result;

        public MyWebSocketClient(URI serverUri, Draft protocolDraft, CountDownLatch handshakeSuccess, CountDownLatch connectClose) {
            super(serverUri, protocolDraft);
            this.handshakeSuccess = handshakeSuccess;
            this.connectClose = connectClose;
        }

        @Override
        public void onOpen(ServerHandshake handshake) {
            System.out.println(getCurrentTimeStr() + "\t连接建立成功！");
        }

        @Override
        public void onMessage(String msg) {
            JSONObject msgObj = JSON.parseObject(msg);
            String action = msgObj.getString("action");
            if (Objects.equals("started", action)) {
                // 握手成功
                System.out.println(getCurrentTimeStr() + "\t握手成功！sid: " + msgObj.getString("sid"));
                handshakeSuccess.countDown();
            } else if (Objects.equals("result", action)) {
                String r = getContent(msgObj.getString("data"));
                if (r != null) {
                    // 转写结果
                    System.out.println(getCurrentTimeStr() + "\tresult: " + r);
                    result = r;
                }
            } else if (Objects.equals("error", action)) {
                // 连接发生错误
                System.out.println("Error: " + msg);
                System.exit(0);
            }
        }

        @Override
        public void onError(Exception e) {
            System.out.println(getCurrentTimeStr() + "\t连接发生错误：" + e.getMessage() + ", " + new Date());
            e.printStackTrace();
            System.exit(0);
        }

        @Override
        public void onClose(int arg0, String arg1, boolean arg2) {
            System.out.println(getCurrentTimeStr() + "\t链接关闭");
            connectClose.countDown();
        }

        @Override
        public void onMessage(ByteBuffer bytes) {
            try {
                System.out.println(getCurrentTimeStr() + "\t服务端返回：" + new String(bytes.array(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    // 把转写结果解析为句子
    public static String getContent(String message) {
        String result = "";
        try {
            JSONObject messageObj = JSON.parseObject(message);
            result = messageObj.getString("dst");
        } catch (Exception e) {
            return message;
        }

        return result;
    }

    @SuppressWarnings("deprecation")
    public static class DraftWithOrigin extends Draft_17 {

        private String originUrl;

        public DraftWithOrigin(String originUrl) {
            this.originUrl = originUrl;
        }

        @Override
        public Draft copyInstance() {
            return new DraftWithOrigin(originUrl);
        }

        @NotNull
        @Override
        public ClientHandshakeBuilder postProcessHandshakeRequestAsClient(@NotNull ClientHandshakeBuilder request) {
            super.postProcessHandshakeRequestAsClient(request);
            request.put("Origin", originUrl);
            return request;
        }
    }
}
