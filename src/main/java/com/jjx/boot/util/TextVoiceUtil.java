package com.jjx.boot.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jjx.boot.pool.ThreadPoolSingle;
import javazoom.jl.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutorService;

/**
 * @author Administrator
 */
public class TextVoiceUtil {
    private static Logger log = LoggerFactory.getLogger(TextVoiceUtil.class);

    final static String PATH = "voice";

    public static void main(String[] args) {
//        play(text2Voice(AesTool.decrypt("wz6BE2WfJGm0MfcAgZxqQPeUnYmAAlRSMR2wKz6Agzku7ZCxcLRw/uHly87yFg81")));
        ExecutorService service =ThreadPoolSingle.getSingle().getThreadPool();
        service.execute(() -> play("F:\\music\\Right+Now+(Na+Na+Na)+-+Akon.mp3"));
        service.execute(() -> play("F:\\music\\Everything_I_Need(Skylar_Grey).mp3"));
        service.execute(() -> play("F:\\music\\Take+me+hand.mp3"));
    }

    public static void play(String filename) {
        try {
            BufferedInputStream buffer = new BufferedInputStream(new FileInputStream(filename));
            Player player = new Player(buffer);
            player.play();
        } catch (Exception e) {
            log.error("error");
        }
    }

    public static String text2Voice(String text) throws Exception {
        final String appKey = "4E1BG9lTnlSeIf1NQFlrSq6h";

        final String secretKey = "544ca4657ba8002e3dea3ac2f5fdd241";

        /*
         * 发音人选择, 0为普通女声，1为普通男生，3为情感合成-度逍遥，4为情感合成-度丫丫，默认为普通女声
         */
        final int per = 4;
        /*
         * 语速，取值0-9，默认为5中语速
         */
        final int spd = 5;
        /*
         * 音调，取值0-9，默认为5中语调
         */
        final int pit = 5;
        /*
         * 音量，取值0-9，默认为5中音量
         */
        final int vol = 5;
        final String url = "http://tsn.baidu.com/text2audio";
        String cuid = "1234567JAVA";
        TokenHolder holder = new TokenHolder(appKey, secretKey, TokenHolder.ASR_SCOPE);
        holder.resfresh();
        String token = holder.getToken();
        String url2 = url + "?tex=" + ConnUtil.urlEncode(text);
        url2 += "&per=" + per;
        url2 += "&spd=" + spd;
        url2 += "&pit=" + pit;
        url2 += "&vol=" + vol;
        url2 += "&cuid=" + cuid;
        url2 += "&tok=" + token;
        url2 += "&lan=zh&ctp=1";
        HttpURLConnection conn = (HttpURLConnection) new URL(url2).openConnection();
        conn.setConnectTimeout(5000);
        String contentType = conn.getContentType();
        if (contentType.contains("mp3")) {
            byte[] bytes = ConnUtil.getResponseBytes(conn);
            File dir = new File(PATH);
            if (!dir.exists()) {
                log.info(dir.mkdir() + "");
            }
            long t = System.currentTimeMillis();
            String name = PATH + "/" + t + ".mp3";
            File file = new File(name);
            FileOutputStream os = new FileOutputStream(file);
            os.write(bytes);
            os.close();
            return name;
        } else {
            log.info("ERROR: content-type= " + contentType);
            String res = ConnUtil.getResponseString(conn);
            log.info(res);
            return PATH + "/" + "error.mp3";
        }
    }

    static class ConnUtil {
        static String urlEncode(String str) {
            String result = null;
            try {
                result = URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return result;
        }

        static String getResponseString(HttpURLConnection conn) throws IOException {
            return new String(getResponseBytes(conn));
        }

        static byte[] getResponseBytes(HttpURLConnection conn) throws IOException {
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                if (responseCode == 401) {
                    throw new RuntimeException("认证不通过");
                }
                throw new RuntimeException("http response code is" + responseCode);
            }
            InputStream inputStream = conn.getInputStream();
            return getInputStreamContent(inputStream);
        }

        static byte[] getInputStreamContent(InputStream is) throws IOException {
            byte[] b = new byte[1024];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int len;
            while (true) {
                len = is.read(b);
                if (len == -1) {
                    break;
                }
                byteArrayOutputStream.write(b, 0, len);
            }
            return byteArrayOutputStream.toByteArray();
        }
    }

    static class TokenHolder {
        static final String ASR_SCOPE = "audio_voice_assistant_get";
        public static final String TTS_SCOPE = "audio_tts_post";
        private static final String URL = "http://openapi.baidu.com/oauth/2.0/token";
        private String scope;
        private String apiKey;
        private String secretKey;
        private String token;
        private long expiresAt;

        TokenHolder(String apiKey, String secretKey, String scope) {
            this.apiKey = apiKey;
            this.secretKey = secretKey;
            this.scope = scope;
        }

        public String getToken() {
            return token;
        }

        public long getExpiresAt() {
            return expiresAt;
        }

        void resfresh() throws Exception {
            String getTokenURL = URL + "?grant_type=client_credentials&client_id=" + ConnUtil.urlEncode(apiKey) + "&client_secret=" + ConnUtil.urlEncode(secretKey);
            URL url = new URL(getTokenURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            String result = ConnUtil.getResponseString(conn);
            parseJson(result);
        }

        /**
         * @param result token接口获得的result
         */
        private void parseJson(String result) {
            JSONObject json = JSON.parseObject(result);
            if (!json.containsKey("access_token")) {
                throw new RuntimeException("access_token not obtained, " + result);
            }
            if (!json.containsKey("scope")) {
                throw new RuntimeException("scopenot obtained, " + result);
            }
            if (!json.getString("scope").contains(scope)) {
                throw new RuntimeException("scope not exist, " + scope + "," + result);
            }
            token = json.getString("access_token");
            expiresAt = System.currentTimeMillis() + json.getLong("expires_in") * 1000;
        }
    }

}
