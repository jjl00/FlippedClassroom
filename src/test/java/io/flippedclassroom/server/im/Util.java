package io.flippedclassroom.server.im;

/**
 * 描述:
 * 生成请求参数
 *
 * @author HASEE
 * @create 2018-09-29 13:23
 */
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Util {
    static final String appSecret="851579a07b15";
    static final String appKey="75b59f559e377fbe52c0909f1dfc6b75";
    // 计算并获取CheckSum
    public static String getCheckSum(String appSecret, String nonce, String curTime) {
        return encode("sha1", appSecret + nonce + curTime);
    }

    // 计算并获取md5值
    public static String getMD5(String requestBody) {
        return encode("md5", requestBody);
    }

    private static String encode(String algorithm, String value) {
        if (value == null) {
            return null;
        }
        try {
            MessageDigest messageDigest
                    = MessageDigest.getInstance(algorithm);
            messageDigest.update(value.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }
    private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    static int getNonce(){
        return new Random().nextInt(100);
    }
    static long getCurTime(){
        return System.currentTimeMillis()/1000;
    }

    //设置通用请求头
    public static void setCommonHeader(HttpPost httpPost){
        int Nonce=Util.getNonce();
        long CurTime=Util.getCurTime();
        String CheckSum=Util.getCheckSum(Util.appSecret,Nonce+"",CurTime+"");
        // 设置请求的header
        httpPost.addHeader("AppKey", Util.appKey);
        httpPost.addHeader("Nonce",Nonce+"");
        httpPost.addHeader("CurTime", CurTime+"");
        httpPost.addHeader("CheckSum", CheckSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
    }
    //设置通用httpclient请求
    public static void commonHttpRequest(String url,List<NameValuePair> pairs)throws Exception{
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        setCommonHeader(httpPost);
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, "utf-8"));
        // 执行请求
        HttpResponse response = httpClient.execute(httpPost);
        // 打印执行结果
        System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
    }

    public static void jsonHttpRequest(String url, JSONObject jsonObject)throws Exception{
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        setCommonHeader(httpPost);
        httpPost.setEntity(new StringEntity(jsonObject.toString(),"utf-8"));
        HttpResponse response = httpClient.execute(httpPost);
        System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
    }
}