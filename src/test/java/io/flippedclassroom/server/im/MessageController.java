package io.flippedclassroom.server.im;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 * 发送消息
 *
 * @author HASEE
 * @create 2018-10-22 14:15
 */
public class MessageController {
    public static void main(String[] args)throws Exception {
        String s="{\n" +
                "\t\"msg\":\"hello\"\n" +
                "}";
        send("owner",1,"1452552057",0,s);
    }
    public static void send(String from,int ope,String to,int type,String body)throws Exception{
        String url="https://api.netease.im/nimserver/msg/sendMsg.action";
        List<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("from", from));
        pairs.add(new BasicNameValuePair("ope", ope+""));
        pairs.add(new BasicNameValuePair("to",to));
        pairs.add(new BasicNameValuePair("type", type+""));
        pairs.add(new BasicNameValuePair("body", body));
        Util.commonHttpRequest(url,pairs);
    }
}